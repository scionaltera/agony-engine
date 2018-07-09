package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
public class SayCommand {
    private SimpMessagingTemplate simpMessagingTemplate;
    private ActorRepository actorRepository;

    @Inject
    public SayCommand(SimpMessagingTemplate simpMessagingTemplate, ActorRepository actorRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.actorRepository = actorRepository;
    }

    public void invoke(Actor actor, GameOutput output, QuotedString message) {
        List<Actor> actors = actorRepository.findByGameMapAndXAndY(actor.getGameMap(), actor.getX(), actor.getY());

        actors.forEach(target -> {
           if (target.equals(actor)) {
               output.append("[cyan]You say '" + message.getText() + "[cyan]'");
           } else {
               // TODO need a database model for dynamically defined channels, for now assume everything is a say
               GameOutput formatted = target.onChannel(actor, message.getText());

               // TODO this stuff will need to be pulled out into a generic utility since it will be used every time we push messages into other browsers
               SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();

               headerAccessor.setSessionId(target.getSessionId());
               headerAccessor.setLeaveMutable(true);

               simpMessagingTemplate.convertAndSendToUser(target.getSessionUsername(), "/queue/output", formatted, headerAccessor.getMessageHeaders());
           }
        });
    }
}
