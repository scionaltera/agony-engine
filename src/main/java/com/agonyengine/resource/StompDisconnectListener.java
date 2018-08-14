package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.inject.Inject;

import java.util.Date;

@Component
public class StompDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StompDisconnectListener.class);

    private ActorRepository actorRepository;
    private CommService commService;

    @Inject
    public StompDisconnectListener(ActorRepository actorRepository,
                                   CommService commService) {

        this.actorRepository = actorRepository;
        this.commService = commService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        Actor actor = actorRepository.findByConnectionSessionUsernameAndConnectionSessionId(
            headerAccessor.getUser().getName(),
            event.getSessionId());

        if (actor == null) {
            return;
        }

        LOGGER.info("{} has disconnected ({})", actor.getName(), actor.getConnection().getRemoteIpAddress());

        commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s has disconnected.", actor.getName())), actor);

        actor.getConnection().setDisconnectedDate(new Date());
        actorRepository.save(actor);
    }
}
