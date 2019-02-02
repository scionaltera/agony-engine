package com.agonyengine.model.command.impl;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.command.binding.ActorSameRoom;
import com.agonyengine.model.map.Direction;
import com.agonyengine.model.map.Room;
import com.agonyengine.stomp.model.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.util.FormattingUtils;
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

    @Inject
    public LookCommand(
        ActorRepository actorRepository,
        RoomRepository roomRepository,
        CommService commservice) {

        this.actorRepository = actorRepository;
        this.roomRepository = roomRepository;
        this.commService = commservice;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        // TODO could commands declare the capabilities they require via an annotation, so that the invoker could perform this check?
        if (actor.getCreatureInfo() == null || !actor.getCreatureInfo().hasCapability(SEE)) {
            output.append("[default]Alas, you are unable to see.");
            return;
        }

        if (actor.getRoomId() == null) {
            output.append("[black]You are floating in the void. There is nothing to see here.");
            return;
        }

        Room room = roomRepository.findById(actor.getRoomId()).orElse(null);

        if (room == null) {
            output.append("[black]You are floating in the void. There is nothing to see here.");
            return;
        }

        output.append("[yellow]Smooth Gray Stones");
        output.append(FormattingUtils.softWrap("[default]The sky is black and featureless. Ambient light shines dimly but " +
            "its source is unclear. The floor is made of unnaturally smooth gray stones packed together with nearly " +
            "perfect hairline seams running off in every direction. " +
            "In some places the stone gives way to a yawning black void. It seems like you could fall forever from " +
            "one of these precipices. Time itself feels like it has stopped in this place. Or perhaps, it never started."));

        output.append(room.getExits().stream()
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
