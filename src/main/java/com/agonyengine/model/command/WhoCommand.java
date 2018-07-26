package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class WhoCommand {
    private ActorRepository actorRepository;

    @Inject
    public WhoCommand(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        List<Actor> actors = actorRepository.findBySessionUsernameIsNotNullAndSessionIdIsNotNull(Sort.by(Sort.Direction.ASC, "name"));

        output.append("[dwhite][ [white]Who is Online [dwhite]]");

        actors.forEach(a -> output.append(String.format("[dwhite]%s", a.getName())));

        output.append("");
        output.append(String.format("%d player%s online.", actors.size(), actors.size() == 1 ? "" : "s"));
    }
}
