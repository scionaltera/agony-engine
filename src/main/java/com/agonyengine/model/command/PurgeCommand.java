package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.ActorInventory;
import com.agonyengine.model.interpret.ActorSameRoom;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Component
public class PurgeCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurgeCommand.class);

    private CommService commService;
    private ActorRepository actorRepository;

    @Inject
    public PurgeCommand(CommService commService, ActorRepository actorRepository) {
        this.commService = commService;
        this.actorRepository = actorRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorSameRoom itemBinding) {
        Actor item = itemBinding.getTarget();

        if (item.getConnection() != null) {
            output.append("Sorry, you cannot purge other players.");
            return;
        }

        output.append(String.format("[yellow]You purge %s[yellow].", item.getName()));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[yellow]%s has purged %s[yellow]!", StringUtils.capitalize(actor.getName()), item.getName())),
            actor, item);

        LOGGER.info("{} has purged an item: {} ({})", actor.getName(), item.getName(), item.getId());

        actorRepository.delete(item);
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorInventory itemBinding) {
        Actor item = itemBinding.getTarget();

        if (item.getConnection() != null) {
            output.append("Sorry, you cannot purge other players.");
            return;
        }

        output.append(String.format("[yellow]You purge %s[yellow].", item.getName()));

        LOGGER.info("{} has purged an item: {} ({})", actor.getName(), item.getName(), item.getId());

        actorRepository.delete(item);
    }
}
