package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.interpret.ActorInventory;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class WearCommand {
    private ActorRepository actorRepository;
    private CommService commService;

    @Inject
    public WearCommand(ActorRepository actorRepository, CommService commService) {
        this.actorRepository = actorRepository;
        this.commService = commService;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorInventory itemBinding) {
        Actor item = itemBinding.getTarget();

        if (actor.getCreatureInfo() == null) { // TODO make ArgumentBinding for this
            output.append("[default]Only creatures can wear equipment.");
            return;
        }

        if (item.getItemInfo() == null) { // TODO make ArgumentBinding for this
            output.append("[default]You may only wear items as equipment.");
            return;
        }

        Optional<BodyPart> wearLocationOptional = actor.getCreatureInfo().getBodyParts().stream()
            .filter(part -> part.getEquipment() == null)
            .findFirst();

        if (!wearLocationOptional.isPresent()) {
            output.append("[default]You have nowhere to wear that.");
            return;
        }

        BodyPart wearLocation = wearLocationOptional.get();

        wearLocation.setEquipment(item);
        item.setGameMap(null);

        actorRepository.save(actor);
        actorRepository.save(item);

        output.append(String.format("[default]You wear %s[default].", item.getName()));
        commService.echo(item, new GameOutput(String.format("[default]%s wears you.", actor.getName())));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s wears %s[default].",
                actor.getName(),
                item.getName())),
            actor, item);
    }
}
