package com.afrain.lojaonline.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.afrain.lojaonline.services.exceptions.FileException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	public URI uploadFile(MultipartFile multipartFile) {

		try {
			String fileName = multipartFile.getOriginalFilename();// PEGA O NOME DO ARQUIVO
			InputStream is = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();// TIPO DE ARQUIVO ENVIADO EX. IMG, TEXTO, ETC.
			return uploadFile(fileName, is, contentType);
		} catch (IOException e) {
			throw new FileException("Erro de ID: " + e.getMessage());
		}

	}

	public URI uploadFile(String fileName, InputStream is, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("INICIANDO UPLOAD");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("UPLOAD FINALIZADO");

			return s3client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL par URI! ");
		}
	}

}
