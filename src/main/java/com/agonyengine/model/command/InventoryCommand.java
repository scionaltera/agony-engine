package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
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

        actorRepository.findByGameMap(actor.getInventory())
            .forEach(item -> output.append("[default]" + item.getName()));
    }
}
