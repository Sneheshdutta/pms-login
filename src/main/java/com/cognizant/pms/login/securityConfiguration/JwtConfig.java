package com.cognizant.pms.login.securityConfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig {
    private String secret = "secret";
    private long validity = 30;

    public String getSecret() {
        return secret;
    }

    public long getValidity() {
        return validity;
    }

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
}