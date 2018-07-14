package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CommService {
    private SimpMessagingTemplate simpMessagingTemplate;

    @Inject
    public CommService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void echo(Actor target, GameOutput message) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();

        headerAccessor.setSessionId(target.getSessionId());

        simpMessagingTemplate.convertAndSendToUser(target.getSessionUsername(), "/queue/output", message, headerAccessor.getMessageHeaders());
    }
}
