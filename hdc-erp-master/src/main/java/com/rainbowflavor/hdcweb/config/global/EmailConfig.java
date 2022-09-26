package com.rainbowflavor.hdcweb.config.global;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Getter
@Configuration
@PropertySources({
        @PropertySource(value="file:c:/dev/hdc-erp/emailConfig.properties", ignoreResourceNotFound = true),
        @PropertySource(value="file:${user.home}/Documents/dev/hdc-erp/emailConfig.properties", ignoreResourceNotFound = true)
})
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.main.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String required;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private String connectiontimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private String timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private String writetimeout;
}
