package com.agonyengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@EnableScheduling
@Configuration
public class ApplicationConfiguration {
    private Date bootDate = new Date();

    @Bean(name = "applicationVersion")
    public String applicationVersion() {
        return ApplicationConfiguration.class.getPackage().getImplementationVersion();
    }

    @Bean(name = "applicationBootDate")
    public Date applicationBootDate() {
        return bootDate;
    }

    @Bean(name = "random")
    public Random getRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong();
    }
}
