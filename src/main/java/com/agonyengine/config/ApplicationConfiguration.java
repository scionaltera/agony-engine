package com.agonyengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

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
}
