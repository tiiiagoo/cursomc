package com.tgs.cursomc.services;

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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class AmazonS3Service {
	
	private Logger LOG = LoggerFactory.getLogger(AmazonS3Service.class);
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Value("${s3.bucket}")
	private String bucketName;

	public URI uploadFile(MultipartFile multipartFile){				
		try {
			InputStream  is = multipartFile.getInputStream();
			String fileName = multipartFile.getOriginalFilename();
			String contentType = multipartFile.getContentType();
			return uploadFile(is, fileName, contentType);			
		} catch (IOException e) {
			throw new RuntimeException("Erro de IO: "+e.getMessage());
		}
	}

	private URI uploadFile(InputStream is, String fileName, String contentType) {	
		try {
			ObjectMetadata data = new ObjectMetadata();
			data.setContentType(contentType);
			LOG.info("Iniciando upload");
			amazonS3.putObject(bucketName, fileName, is, data);
			LOG.info("Upload finalizado");	
			return amazonS3.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao converter Url para URI");
		}
	}
}
