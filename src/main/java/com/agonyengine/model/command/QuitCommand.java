package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;

@Component
public class QuitCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuitCommand.class);

    private ActorRepository actorRepository;
    private InvokerService invokerService;
    private CommService commService;

    @Inject
    public QuitCommand(ActorRepository actorRepository, InvokerService invokerService, CommService commService) {
        this.actorRepository = actorRepository;
        this.invokerService = invokerService;
        this.commService = commService;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, QuotedString quotedString) {
        if (!"NOW".equals(quotedString.getToken())) {
            output.append("[red]For safety, you must type \"quit now\" to quit the game.");
            return;
        }

        // TODO: keep your inventory and reattach it when you come back
        // for now, just drop it all on the floor to avoid SQL constraint violations
        actorRepository.findByGameMap(actor.getInventory())
            .forEach(item -> invokerService.invoke(actor, output, null, Arrays.asList("drop", item.getName())));

        output.append(String.format("[yellow]Goodbye, %s!", actor.getName()));
        output.append("<script type=\"text/javascript\">setTimeout(function() { window.location=\"/account\"; }, 1000);</script>");

        LOGGER.info("{} has quit ({})", actor.getName(), actor.getRemoteIpAddress());

        commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s disappears in a puff of smoke!", StringUtils.capitalize(actor.getName()))), actor);

        actorRepository.delete(actor);
    }
}
