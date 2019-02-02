package com.agonyengine.model.command.impl;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.stomp.model.GameOutput;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class EquipmentCommand {
    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        if (actor.getCreatureInfo() == null) {
            output.append("[default]You don't have any equipment.");
            return;
        }

        output.append("[default]Your Equipment:");

        actor.getCreatureInfo().getBodyParts().stream()
            .filter(part -> part.getWearLocation() != null)
            .forEach(part -> output.append(String.format("&nbsp;&nbsp;%s - %s",
                part.getName(),
                part.getArmor() == null ? "" : part.getArmor().getName() )));
    }
}
