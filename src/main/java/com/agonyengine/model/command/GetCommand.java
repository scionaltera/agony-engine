package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.ActorSameRoom;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;

import static com.agonyengine.model.actor.BodyPartCapability.HOLD;

@Component
public class GetCommand {
    private CommService commService;
    private InvokerService invokerService;
    private ActorRepository actorRepository;

    @Inject
    public GetCommand(CommService commService, InvokerService invokerService, ActorRepository actorRepository) {
        this.commService = commService;
        this.invokerService = invokerService;
        this.actorRepository = actorRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorSameRoom itemBinding) {
        if (actor.getCreatureInfo() == null || !actor.getCreatureInfo().hasCapability(HOLD)) {
            output.append("[default]Alas, you are unable to hold items.");
            return;
        }

        Actor item = itemBinding.getTarget();
        GameOutput itemLook = new GameOutput();

        output.append(String.format("[default]You get %s[default].", item.getName()));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s gets %s[default].", StringUtils.capitalize(actor.getName()), item.getName())),
            actor, item);

        item.setGameMap(actor.getInventory());
        item.setX(0);
        item.setY(0);
        item.setRoomId(null);

        actorRepository.save(item);

        itemLook.append(String.format("%s gets you.", StringUtils.capitalize(actor.getName())));
        invokerService.invoke(item, itemLook, null, Collections.singletonList("look"));
        commService.echo(item, itemLook);
    }
}
