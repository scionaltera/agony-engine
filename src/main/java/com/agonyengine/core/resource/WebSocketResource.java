package com.agonyengine.core.resource;

import com.agonyengine.core.model.actor.PlayerActorTemplate;
import com.agonyengine.core.model.stomp.GameOutput;
import com.agonyengine.core.model.stomp.UserInput;
import com.agonyengine.core.repository.PlayerActorTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.UUID;

@Controller
public class WebSocketResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketResource.class);

    private SessionRepository sessionRepository;
    private PlayerActorTemplateRepository playerActorTemplateRepository;

    @Inject
    public WebSocketResource(
        SessionRepository sessionRepository,
        PlayerActorTemplateRepository playerActorTemplateRepository) {

        this.sessionRepository = sessionRepository;
        this.playerActorTemplateRepository = playerActorTemplateRepository;
    }

    @SubscribeMapping("/queue/output")
    public GameOutput onSubscribe() {
        LOGGER.info("Subscribe");

        return new GameOutput("Subscribed!");
    }

    @MessageMapping("/input")
    @SendToUser(value = "/queue/output", broadcast = false)
    public GameOutput onInput(UserInput input, Message<byte[]> message) {
        Session session = getSession(message);
        PlayerActorTemplate pat = playerActorTemplateRepository
            .findById(UUID.fromString(session.getAttribute("actor")))
            .orElse(null);

        LOGGER.info("Input: " + input.getInput());

        return new GameOutput((pat != null ? pat.getGivenName() : "Someone") + ": " + input.getInput());
    }

    private Session getSession(Message<byte[]> message) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);
        String sessionId = (String)headerAccessor.getSessionAttributes().get("SPRING.SESSION.ID");

        return sessionRepository.findById(sessionId);
    }
}
