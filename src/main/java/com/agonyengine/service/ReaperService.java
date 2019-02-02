package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.stomp.model.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Component
public class ReaperService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaperService.class);

    private ActorRepository actorRepository;
    private CommService commService;

    @Inject
    public ReaperService(
        ActorRepository actorRepository,
        CommService commService) {

        this.actorRepository = actorRepository;
        this.commService = commService;
    }

    @Scheduled(fixedRate = 600000L)
    public void reapLinkDeadActors() {
        LOGGER.debug("Querying for link-dead players to reap...");

        List<Actor> actors = actorRepository.findByConnectionDisconnectedDateIsBeforeAndRoomIdIsNotNull(new Date(System.currentTimeMillis() - (1000 * 60 * 30)));

        actors.forEach(actor -> {
            LOGGER.info("Reaping link-dead player: {}", actor.getName());

            commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s disappears in a puff of smoke!", actor.getName())), actor);
            actor.setRoomId(null);
        });

        actorRepository.saveAll(actors);
    }
}
