package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.ActorEquipped;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Component
public class RemoveCommand {
    private ActorRepository actorRepository;
    private CommService commService;

    @Inject
    public RemoveCommand(ActorRepository actorRepository, CommService commService) {
        this.actorRepository = actorRepository;
        this.commService = commService;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorEquipped equippedBinding) {
        Actor equipped = equippedBinding.getTarget();

        equippedBinding.wearLocation().setEquipment(null);
        equipped.setGameMap(actor.getInventory());

        actorRepository.save(actor);
        actorRepository.save(equipped);

        output.append(String.format("[default]You remove %s[default].", equipped.getName()));
        commService.echo(equipped, new GameOutput(String.format("[default]%s removes you.", actor.getName())));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s removes %s %s[default].",
                actor.getName(),
                actor.getPronoun().getPossessive(),
                equipped.getPlainName())),
            actor, equipped);
    }
}
