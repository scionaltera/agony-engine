package com.agonyengine.config;

import com.agonyengine.model.command.Direction;
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

    @Bean(name = "northDirection")
    public Direction northDirection() {
        return new Direction("north", "south", 0, 1, 0);
    }

    @Bean(name = "eastDirection")
    public Direction eastDirection() {
        return new Direction("east", "west", 1, 0, 0);
    }

    @Bean(name = "southDirection")
    public Direction southDirection() {
        return new Direction("south", "north", 0, -1, 0);
    }

    @Bean(name = "westDirection")
    public Direction westDirection() {
        return new Direction("west", "east", -1, 0, 0);
    }

    @Bean(name = "northCommand")
    public MoveCommand northCommand() {
        return new MoveCommand(northDirection(), applicationContext);
    }

    @Bean(name = "eastCommand")
    public MoveCommand eastCommand() {
        return new MoveCommand(eastDirection(), applicationContext);
    }

    @Bean(name = "southCommand")
    public MoveCommand southCommand() {
        return new MoveCommand(southDirection(), applicationContext);
    }

    @Bean(name = "westCommand")
    public MoveCommand westCommand() {
        return new MoveCommand(westDirection(), applicationContext);
    }
}
