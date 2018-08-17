package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;

import static com.agonyengine.model.actor.BodyPartCapability.WALK;

public class MoveCommand {
    private Direction direction;
    private ActorRepository actorRepository;
    private InvokerService invokerService;
    private CommService commService;
    private ApplicationContext applicationContext;

    public MoveCommand(Direction direction, ApplicationContext applicationContext) {
        this.direction = direction;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    void postConstruct() {
        this.actorRepository = applicationContext.getBean("actorRepository", ActorRepository.class);
        this.invokerService = applicationContext.getBean("invokerService", InvokerService.class);
        this.commService = applicationContext.getBean("commService", CommService.class);
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        // TODO could commands declare the capabilities they require via an annotation, so that the invoker could perform this check?
        if (!actor.getCreatureInfo().hasCapability(WALK)) {
            output.append("[default]Alas, you are unable to walk.");
            return;
        }

        int newX = actor.getX() + direction.getX();
        int newY = actor.getY() + direction.getY();

        if (!actor.getGameMap().hasTile(newX, newY)) {
            output.append("[default]Alas, you cannot go that way.");
            return;
        }

        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s leaves to the %s.", StringUtils.capitalize(actor.getName()), direction.getName())),
            actor);

        actor.setX(newX);
        actor.setY(newY);

        actorRepository.save(actor);

        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s arrives from the %s.", StringUtils.capitalize(actor.getName()), direction.getOpposite())),
            actor);

        invokerService.invoke(actor, output, null, Collections.singletonList("look"));
    }
}
