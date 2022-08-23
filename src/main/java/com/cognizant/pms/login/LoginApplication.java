package com.cognizant.pms.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.*")
@EnableSwagger2
public class LoginApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(LoginApplication.class, args);
    }

}
