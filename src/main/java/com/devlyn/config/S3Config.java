package com.devlyn.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${aws.access_key_id}")
	private String awsId;

	@Value("${aws.secret_access_key}")
	private String awsKey;

	@Value("${s3.region}")
	private String region;
	
	@Value("${s3.endpoint:}") // Opcional: permite configurar endpoint (útil para locais ou mocks)
    private String endpoint;

	@PostConstruct
	public void validateProperties() {
	    if (awsId == null || awsId.isBlank()) {
	        throw new IllegalArgumentException("AWS Access Key ID must not be null or empty");
	    }
	    if (awsKey == null || awsKey.isBlank()) {
	        throw new IllegalArgumentException("AWS Secret Access Key must not be null or empty");
	    }
	    if (region == null || region.isBlank()) {
	        throw new IllegalArgumentException("AWS Region must not be null or empty");
	    }
	}

    @Bean
    public S3Client s3client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsId, awsKey);

        S3ClientBuilder builder = S3Client.builder()
        		.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        		.region(Region.of(region));

        if (!endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
}
