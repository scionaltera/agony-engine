package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
public class LookCommand {
    private ActorRepository actorRepository;

    @Inject
    public LookCommand(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void invoke(Actor actor, GameOutput output) {
        List<Actor> actors = actorRepository.findByGameMapAndXAndY(actor.getGameMap(), actor.getX(), actor.getY());

        output.append("[black]You see nothing but the inky void swirling around you.");

        actors.stream()
            .filter(target -> !actor.equals(target))
            .forEach(target -> output.append(String.format("[green]%s is here.", target.getName())));
    }
}
