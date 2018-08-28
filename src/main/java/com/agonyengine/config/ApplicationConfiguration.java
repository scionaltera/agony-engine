package com.agonyengine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;
import java.util.UUID;

@EnableScheduling
@Configuration
public class ApplicationConfiguration {
    private Date bootDate = new Date();

    @Value("${agonyengine.maps.default}")
    private UUID defaultMapId;

    @Value("${agonyengine.tilesets.inventory}")
    private UUID inventoryTilesetId;

    @Bean(name = "applicationVersion")
    public String applicationVersion() {
        return ApplicationConfiguration.class.getPackage().getImplementationVersion();
    }

    @Bean(name = "applicationBootDate")
    public Date applicationBootDate() {
        return bootDate;
    }

    @Bean
    public UUID defaultMapId() {
        return defaultMapId;
    }

    @Bean
    public UUID inventoryTilesetId() {
        return inventoryTilesetId;
    }
}
