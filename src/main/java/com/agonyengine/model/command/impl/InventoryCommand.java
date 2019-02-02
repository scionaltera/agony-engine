package com.agonyengine.model.command.impl;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.stomp.model.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Component
public class InventoryCommand {
    private ActorRepository actorRepository;

    @Inject
    public InventoryCommand(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        output.append("[default]Items in your inventory:");

        actorRepository.findByRoomId(actor.getInventoryId())
            .forEach(item -> output.append("[default]" + item.getName()));
    }
}
