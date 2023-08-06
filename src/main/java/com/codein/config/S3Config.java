package com.codein.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
public class S3Config {
    @Bean
    public S3Client defaultS3Client() {
            return S3Client.builder()
                    .region(Region.AP_NORTHEAST_2)
                    .credentialsProvider(ProfileCredentialsProvider.create("s3"))
                    .build();
    }


}
