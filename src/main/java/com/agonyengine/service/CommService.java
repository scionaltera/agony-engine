package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
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

        addPrompt(message);

        simpMessagingTemplate.convertAndSendToUser(target.getConnection().getSessionUsername(), "/queue/output", message);
    }

    public void echoToRoom(Actor source, GameOutput message, Actor... exclude) {
        List<Actor> excludeList = Arrays.asList(exclude);

        addPrompt(message);

        actorRepository.findByRoomId(source.getRoomId())
            .stream()
            .filter(t -> t.getConnection() != null)
            .filter(t -> !excludeList.contains(t))
            .forEach(t -> simpMessagingTemplate.convertAndSendToUser(t.getConnection().getSessionUsername(), "/queue/output", message));
    }

    private void addPrompt(GameOutput output) {
        output.append("");
        output.append("[default]> ");
    }
}
