package com.agonyengine.model.interpret;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPart;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
@Scope(scopeName = "prototype")
public class ActorEquipped implements ArgumentBinding {
    private String token;
    private Actor target;
    private BodyPart wearLocation;

    @Transactional
    @Override
    public boolean bind(Actor actor, String token) {
        this.token = token;

        if (actor.getCreatureInfo() == null) {
            return false;
        }

        actor.getCreatureInfo().getBodyParts().stream()
            .filter(part -> part.getEquipment() != null)
            .filter(part -> Arrays.stream(part.getEquipment().getNameTokens()).anyMatch(word -> word.toUpperCase().startsWith(token.toUpperCase())))
            .findFirst()
            .ifPresent(wearLocation -> {
                target = wearLocation.getEquipment();
                this.wearLocation = wearLocation;
            });

        return target != null;
    }

    @Override
    public String getToken() {
        return token;
    }

    public Actor getTarget() {
        return target;
    }

    public BodyPart wearLocation() {
        return wearLocation;
    }
}
