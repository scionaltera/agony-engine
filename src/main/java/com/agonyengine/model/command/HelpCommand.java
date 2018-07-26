package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.VerbRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Component
public class HelpCommand {
    private VerbRepository verbRepository;

    @Inject
    public HelpCommand(VerbRepository verbRepository) {
        this.verbRepository = verbRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        output.append("[dwhite][ [white]Command List [dwhite]]");
        verbRepository
            .findAll(Sort.by(Sort.Direction.ASC,"priority", "name"))
            .forEach(v -> output.append("[dwhite]" + v.getName().toUpperCase()));
    }
}
