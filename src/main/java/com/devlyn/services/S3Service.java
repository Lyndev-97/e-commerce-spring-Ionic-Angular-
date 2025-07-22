package com.devlyn.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {

    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private S3Client s3Client; // Alterado de AmazonS3 para S3Client

    @Value("${s3.bucket}")
    private String bucketName;

    public URI uploadFile(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            InputStream is = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();
            return uploadFile(is, fileName, contentType);
        } catch (IOException e) {
            throw new RuntimeException("Erro de IO: " + e.getMessage());
        }
    }

    public URI uploadFile(InputStream is, String fileName, String contentType) {
        try {
            // Configura os metadados do objeto
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            // Realiza o upload do arquivo
            LOG.info("Iniciando upload para o bucket {} com a chave {}", bucketName, fileName);
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(is, is.available()));
            LOG.info("Upload finalizado");

            // Gera a URL do objeto
            String objectUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", 
                    bucketName, s3Client.serviceClientConfiguration().region().id(), fileName);
            return URI.create(objectUrl);

        } catch (S3Exception e) {
            throw new RuntimeException("Erro ao realizar upload para o S3: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Erro de IO ao processar o arquivo: " + e.getMessage());
        }
    }
}