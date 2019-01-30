package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.map.Direction;
import com.agonyengine.model.map.Room;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;

import static com.agonyengine.model.actor.BodyPartCapability.WALK;

public class MoveCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveCommand.class);

    private Direction direction;
    private ActorRepository actorRepository;
    private RoomRepository roomRepository;
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
        this.roomRepository = applicationContext.getBean("roomRepository", RoomRepository.class);
        this.invokerService = applicationContext.getBean("invokerService", InvokerService.class);
        this.commService = applicationContext.getBean("commService", CommService.class);
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        // TODO could commands declare the capabilities they require via an annotation, so that the invoker could perform this check?
        if (actor.getCreatureInfo() == null || !actor.getCreatureInfo().hasCapability(WALK)) {
            output.append("[default]Alas, you are unable to walk.");
            return;
        }

        Room currentRoom;
        Room destinationRoom;

        try {
            currentRoom = roomRepository
                .findById(actor.getRoomId())
                .orElseThrow(() -> new NullPointerException("[black]You are floating in the void, and unable to move!"));

            if (!currentRoom.getExits().contains(direction)) {
                LOGGER.trace("Unable to move Actor {} because room has no exit to the {}", actor.getId(), direction.getName());
                throw new NullPointerException("[default]Alas, you cannot go that way.");
            }

            destinationRoom = roomRepository
                .findByLocationXAndLocationYAndLocationZ(
                    currentRoom.getLocation().getX() + direction.getX(),
                    currentRoom.getLocation().getY() + direction.getY(),
                    currentRoom.getLocation().getZ()
                ).orElseThrow(() -> {
                    LOGGER.trace("Unable to move Actor {} because destination room to the {} does not yet exist", actor.getId(), direction.getName());
                    return new NullPointerException("[default]Alas, you cannot go that way.");
                });
        } catch (NullPointerException e) {
            output.append(e.getMessage());
            return;
        }

        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s leaves to the %s.", StringUtils.capitalize(actor.getName()), direction.getName())),
            actor);

        actor.setRoomId(destinationRoom.getId());

        actorRepository.save(actor);

        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s arrives from the %s.", StringUtils.capitalize(actor.getName()), direction.getOpposite())),
            actor);

        invokerService.invoke(actor, output, null, Collections.singletonList("look"));
    }
}
