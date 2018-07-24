package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.inject.Inject;

import static com.agonyengine.resource.WebSocketResource.SPRING_SESSION_ID_KEY;

@Component
public class StompDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StompDisconnectListener.class);

    private ActorRepository actorRepository;
    private SessionRepository sessionRepository;
    private CommService commService;

    @Inject
    public StompDisconnectListener(ActorRepository actorRepository,
                                   SessionRepository sessionRepository,
                                   CommService commService) {

        this.actorRepository = actorRepository;
        this.sessionRepository = sessionRepository;
        this.commService = commService;
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

        String sessionId = (String)headerAccessor.getSessionAttributes().get(SPRING_SESSION_ID_KEY);
        Session session = sessionRepository.findById(sessionId);

        LOGGER.info("{} has disconnected ({})", actor.getName(), session.getAttribute("remoteIpAddress"));

        commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s disappears in a puff of smoke!", actor.getName())), actor);

        actorRepository.delete(actor);
    }
}
