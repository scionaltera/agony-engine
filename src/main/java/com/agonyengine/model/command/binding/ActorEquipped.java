package com.agonyengine.model.command.binding;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPart;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(scopeName = "prototype")
public class ActorEquipped implements ArgumentBinding {
    private String token;
    private Actor target;
    private List<BodyPart> wearLocations;

    @Transactional
    @Override
    public boolean bind(Actor actor, String token) {
        this.token = token;

        if (actor.getCreatureInfo() == null) {
            return false;
        }

        actor.getCreatureInfo().getBodyParts().stream()
            .filter(part -> part.getArmor() != null)
            .filter(part -> Arrays.stream(part.getArmor().getNameTokens()).anyMatch(word -> word.toUpperCase().startsWith(token.toUpperCase())))
            .findFirst()
            .ifPresent(wearLocation -> {
                target = wearLocation.getArmor();

                wearLocations = actor.getCreatureInfo().getBodyParts().stream()
                    .filter(part -> target.equals(part.getArmor()))
                    .collect(Collectors.toList());
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

    public List<BodyPart> getWearLocations() {
        return wearLocations;
    }
}
