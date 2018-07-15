package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.InvokerService;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

public class MoveCommand {
    private Direction direction;
    private ActorRepository actorRepository;
    private InvokerService invokerService;
    private ApplicationContext applicationContext;

    public MoveCommand(Direction direction, ApplicationContext applicationContext) {
        this.direction = direction;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void postConstruct() {
        this.actorRepository = applicationContext.getBean("actorRepository", ActorRepository.class);
        this.invokerService = applicationContext.getBean("invokerService", InvokerService.class);
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        int newX = actor.getX() + direction.getX();
        int newY = actor.getY() + direction.getY();

        if (!actor.getGameMap().hasTile(newX, newY)) {
            output.append("Alas, you cannot go that way.");
            return;
        }

        actor.setX(newX);
        actor.setY(newY);

        actorRepository.save(actor);

        invokerService.invoke("lookCommand", actor, output);
    }
}
