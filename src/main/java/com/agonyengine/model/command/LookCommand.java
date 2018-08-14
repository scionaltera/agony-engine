package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.ActorSameRoom;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Component
public class LookCommand {
    private ActorRepository actorRepository;
    private CommService commService;
    private List<Direction> directions;

    @Inject
    public LookCommand(ActorRepository actorRepository, CommService commservice, List<Direction> directions) {
        this.actorRepository = actorRepository;
        this.commService = commservice;
        this.directions = directions;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        List<Actor> actors = actorRepository.findByGameMapAndXAndY(actor.getGameMap(), actor.getX(), actor.getY());

        // TODO game maps will need names
        output.append(String.format("[yellow](%d, %d) %s",
            actor.getX(),
            actor.getY(),
            actor.getGameMap().getId()));

        output.append(String.format("[black](tile=0x%s) You see nothing but the inky void swirling around you.",
            Integer.toHexString(Byte.toUnsignedInt(actor.getGameMap().getTile(actor.getX(), actor.getY())))));

        output.append(directions.stream()
            .filter(direction -> actor.getGameMap().hasTile(actor.getX() + direction.getX(), actor.getY() + direction.getY()))
            .map(Direction::getName)
            .collect(joining(" ", "[cyan]Exits: ", "")));

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
