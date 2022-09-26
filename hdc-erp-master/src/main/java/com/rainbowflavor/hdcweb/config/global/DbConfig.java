package com.rainbowflavor.hdcweb.config.global;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Getter
@Configuration
@PropertySources({
        @PropertySource(value="file:c:/dev/hdc-erp/dbConfig.properties", ignoreResourceNotFound = true),
        @PropertySource(value="file:${user.home}/Documents/dev/hdc-erp/dbConfig.properties", ignoreResourceNotFound = true)
})
public class DbConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
}
