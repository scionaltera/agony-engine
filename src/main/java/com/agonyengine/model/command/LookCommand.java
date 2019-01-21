package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.ActorSameRoom;
import com.agonyengine.model.map.Direction;
import com.agonyengine.model.map.Room;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.service.CommService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static com.agonyengine.model.actor.BodyPartCapability.SEE;
import static java.util.stream.Collectors.joining;

@Component
public class LookCommand {
    private ActorRepository actorRepository;
    private RoomRepository roomRepository;
    private CommService commService;
    private List<Direction> directions;

    @Inject
    public LookCommand(
        ActorRepository actorRepository,
        RoomRepository roomRepository,
        CommService commservice,
        List<Direction> directions) {

        this.actorRepository = actorRepository;
        this.roomRepository = roomRepository;
        this.commService = commservice;
        this.directions = directions;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        // TODO could commands declare the capabilities they require via an annotation, so that the invoker could perform this check?
        if (actor.getCreatureInfo() == null || !actor.getCreatureInfo().hasCapability(SEE)) {
            output.append("[default]Alas, you are unable to see.");
            return;
        }

        Room room = roomRepository.findById(actor.getRoomId()).orElse(null);

        if (room == null) {
            output.append("[black]You are floating in the void. There is nothing to see here.");
            return;
        }

        output.append(String.format("[yellow]%s", "A Room"));
        output.append(String.format("[default]%s", "This is a placeholder for the room description."));

        output.append(directions.stream()
            .filter(direction -> roomRepository.findByLocationXAndLocationYAndLocationZ(
                room.getLocation().getX() + direction.getX(),
                room.getLocation().getY() + direction.getY(),
                room.getLocation().getZ() + direction.getZ())
                .isPresent())
            .map(Direction::getName)
            .collect(joining(" ", "[cyan]Exits: ", "")));

        List<Actor> actors = actorRepository.findByRoomId(actor.getRoomId());

        actors.stream()
            .filter(target -> !actor.equals(target))
            .forEach(target -> output.append(String.format("[green]%s is here.%s",
                StringUtils.capitalize(target.getName()),
                target.getConnection() == null || target.getConnection().getDisconnectedDate() == null ? "" : " [yellow][[dred]LINK DEAD[yellow]]")));
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorSameRoom target) {
        output.append(String.format("You look at %s.", target.getTarget().getName()));
        commService.echo(target.getTarget(), new GameOutput(String.format("%s looks at you.", StringUtils.capitalize(actor.getName()))));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("%s looks at %s.", StringUtils.capitalize(actor.getName()), target.getTarget().getName())),
            actor, target.getTarget());
    }
}
