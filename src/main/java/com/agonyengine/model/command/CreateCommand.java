package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.QuotedString;
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
public class CreateCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCommand.class);

    private CommService commService;
    private ActorRepository actorRepository;

    @Inject
    public CreateCommand(CommService commService, ActorRepository actorRepository) {
        this.commService = commService;
        this.actorRepository = actorRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, QuotedString itemName) {
        Actor item = new Actor();

        item.setName(itemName.getToken());
        item.setGameMap(actor.getGameMap());
        item.setX(actor.getX());
        item.setY(actor.getY());

        item = actorRepository.save(item);

        output.append(String.format("[yellow]You create %s[yellow].", item.getName()));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[yellow]%s has created %s[yellow]!", StringUtils.capitalize(actor.getName()), item.getName())),
            actor, item);

        LOGGER.info("{} has created an item: {} ({})", actor.getName(), item.getName(), item.getId());
    }
}
