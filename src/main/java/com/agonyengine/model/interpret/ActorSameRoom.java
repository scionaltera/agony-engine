package com.agonyengine.model.interpret;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.repository.ActorRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Component
@Scope(scopeName = "prototype")
public class ActorSameRoom implements ArgumentBinding {
    private ActorRepository actorRepository;
    private String token;
    private Actor target;

    @Inject
    public ActorSameRoom(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Override
    public boolean bind(Actor actor, String token) {
        this.token = token;
        target = actorRepository.findByGameMapAndXAndY(actor.getGameMap(), actor.getX(), actor.getY())
            .stream()
            .filter(t -> !t.equals(actor) && t.getName().toUpperCase().startsWith(token.toUpperCase()))
            .findFirst()
            .orElse(null);

        return target != null;
    }

    @Override
    public String getToken() {
        return token;
    }

    public Actor getTarget() {
        return target;
    }

    public static String getSyntaxDescription() {
        return "person in same room";
    }
}
