package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.repository.ActorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.inject.Inject;

@Component
public class StompDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StompDisconnectListener.class);

    private ActorRepository actorRepository;

    @Inject
    public StompDisconnectListener(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Actor actor = actorRepository.findBySessionUsernameAndSessionId(
            headerAccessor.getUser().getName(),
            event.getSessionId());

        if (actor == null) {
            return;
        }

        LOGGER.info("{} has disconnected from the game", actor.getName());

        actorRepository.delete(actor);
    }
}
