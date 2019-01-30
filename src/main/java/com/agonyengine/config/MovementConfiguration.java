package com.agonyengine.config;

import com.agonyengine.model.map.Direction;
import com.agonyengine.model.command.MoveCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class MovementConfiguration {
    private ApplicationContext applicationContext;

    @Inject
    public MovementConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "northCommand")
    public MoveCommand northCommand() {
        return new MoveCommand(Direction.NORTH, applicationContext);
    }

    @Bean(name = "eastCommand")
    public MoveCommand eastCommand() {
        return new MoveCommand(Direction.EAST, applicationContext);
    }

    @Bean(name = "southCommand")
    public MoveCommand southCommand() {
        return new MoveCommand(Direction.SOUTH, applicationContext);
    }

    @Bean(name = "westCommand")
    public MoveCommand westCommand() {
        return new MoveCommand(Direction.WEST, applicationContext);
    }
}
