package com.afrain.lojaonline.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.afrain.lojaonline.services.exceptions.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		String extensaoArquivo = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());// PEGA A EXTENCAO DO
																								// ARQUIVO
		if (!"png".equals(extensaoArquivo) && !"jpg".equals(extensaoArquivo)) {
			throw new FileException("ERRO: Somente imagens PNG e JPG são emitidas!");
		}

		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if ("png".equals(extensaoArquivo)) {
				img = pngToJpg(img);
			}
			return img;
		} catch (IOException e) {
			throw new FileException("ERRO: Erro ao ler arquivo!");
		}

	}

	//CONVERTE IMAGEM PNG P/ JPG
	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		return jpgImage;
	}

	public InputStream getInputStream(BufferedImage img, String extension) {

		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (IOException e) {
			throw new FileException("ERRO: Erro ao ler arquivo!");
		}

	}
	
	//RECORTA IMAGEM
	public BufferedImage cropSquare(BufferedImage img) {
		int min = (img.getHeight() <= img.getWidth() ? img.getHeight() : img.getWidth());
		return Scalr.crop(
				img, 
				(img.getWidth()/2) - (min/2),
				(img.getHeight()/2) - (min/2), 
				min,
				min);
	}
	
	public BufferedImage resize(BufferedImage img, int size) {
		return Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, size);
	}

}
