package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Component
public class CommService {
    private ActorRepository actorRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Inject
    public CommService(ActorRepository actorRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.actorRepository = actorRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void echo(Actor target, GameOutput message) {
        if (target.getConnection() == null || target.getConnection().getSessionId() == null) {
            return;
        }

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();

        headerAccessor.setSessionId(target.getConnection().getSessionId());

        addPrompt(message);

        simpMessagingTemplate.convertAndSendToUser(target.getConnection().getSessionUsername(), "/queue/output", message, headerAccessor.getMessageHeaders());
    }

    public void echoToRoom(Actor source, GameOutput message, Actor... exclude) {
        List<Actor> excludeList = Arrays.asList(exclude);

        addPrompt(message);

        actorRepository.findByGameMapAndXAndY(source.getGameMap(), source.getX(), source.getY())
            .stream()
            .filter(t -> t.getConnection().getSessionId() != null || t.getConnection().getSessionUsername() != null)
            .filter(t -> !excludeList.contains(t))
            .forEach(t -> {
                SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();

                headerAccessor.setSessionId(t.getConnection().getSessionId());

                simpMessagingTemplate.convertAndSendToUser(t.getConnection().getSessionUsername(), "/queue/output", message, headerAccessor.getMessageHeaders());
            });
    }

    private void addPrompt(GameOutput output) {
        output.append("");
        output.append("[default]> ");
    }
}
