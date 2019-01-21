package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.ActorInventory;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;

@Component
public class DropCommand {
    private CommService commService;
    private InvokerService invokerService;
    private ActorRepository actorRepository;

    @Inject
    public DropCommand(CommService commService, InvokerService invokerService, ActorRepository actorRepository) {
        this.commService = commService;
        this.invokerService = invokerService;
        this.actorRepository = actorRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorInventory itemBinding) {
        Actor item = itemBinding.getTarget();
        GameOutput itemLook = new GameOutput();

        output.append(String.format("[default]You drop %s[default].", item.getName()));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s drops %s[default].", StringUtils.capitalize(actor.getName()), item.getName())),
            actor, item);

        item.setRoomId(actor.getRoomId());

        actorRepository.save(item);

        itemLook.append(String.format("%s drops you.", StringUtils.capitalize(actor.getName())));
        invokerService.invoke(item, itemLook, null, Collections.singletonList("look"));
        commService.echo(item, itemLook);
    }
}
