package com.afrain.lojaonline.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.afrain.lojaonline.domain.Cidade;
import com.afrain.lojaonline.domain.Cliente;
import com.afrain.lojaonline.domain.Endereco;
import com.afrain.lojaonline.domain.enums.Perfil;
import com.afrain.lojaonline.domain.enums.TipoCliente;
import com.afrain.lojaonline.dto.ClienteDTO;
import com.afrain.lojaonline.dto.ClienteNewDTO;
import com.afrain.lojaonline.repositories.CidadeRepository;
import com.afrain.lojaonline.repositories.ClienteRepository;
import com.afrain.lojaonline.repositories.EnderecoRepository;
import com.afrain.lojaonline.security.UserSS;
import com.afrain.lojaonline.services.exceptions.AuthorizationException;
import com.afrain.lojaonline.services.exceptions.DataIntegrityException;
import com.afrain.lojaonline.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private BCryptPasswordEncoder cryptSenha;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private int size;

	public Cliente find(Integer id) {

		UserSS usuarioLogado = UserService.retornaUsuarioLogado();
		if (usuarioLogado == null || !usuarioLogado.hasRole(Perfil.ADMIN) && !id.equals(usuarioLogado.getId())) {
			throw new AuthorizationException("Acesso Negado!");
		}

		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateCliente(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma Cliente que possui Pedidos relacionados!");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();

	}
	
	public Cliente findByEmail(String email) {
		UserSS usuarioLogado = UserService.retornaUsuarioLogado();
		if (usuarioLogado == null || !usuarioLogado.hasRole(Perfil.ADMIN) && !email.equals(usuarioLogado.getUsername())) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + usuarioLogado.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
		
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}

	public Cliente fromNewDTO(ClienteNewDTO objNewDTO) {
		Cliente cli = new Cliente(null, objNewDTO.getNome(), objNewDTO.getEmail(), objNewDTO.getCpfOuCnpj(),
				TipoCliente.toEnum(objNewDTO.getTipo()), cryptSenha.encode(objNewDTO.getSenha()));
		Cidade cid = cidadeRepository.findAll().get(objNewDTO.getCidadeId());
		Endereco end = new Endereco(null, objNewDTO.getLogradouro(), objNewDTO.getNumero(), objNewDTO.getComplemento(),
				objNewDTO.getBairro(), objNewDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objNewDTO.getTelefone1());
		if (objNewDTO.getTelefone2() != null) {
			cli.getTelefones().add(objNewDTO.getTelefone2());
		}
		if (objNewDTO.getTelefone3() != null) {
			cli.getTelefones().add(objNewDTO.getTelefone3());
		}

		return cli;
	}

	private void updateCliente(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {

		UserSS usuario = UserService.retornaUsuarioLogado();
		if (usuario == null) {
			throw new AuthorizationException("Acesso negado!");
		}

		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + usuario.getId() + ".jpg";

		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}

}
