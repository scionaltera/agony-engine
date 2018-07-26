package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class SayCommand {
    private CommService commService;
    private ActorRepository actorRepository;

    @Inject
    public SayCommand(CommService commService, ActorRepository actorRepository) {
        this.commService = commService;
        this.actorRepository = actorRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, QuotedString message) {
        List<Actor> actors = actorRepository.findByGameMapAndXAndY(actor.getGameMap(), actor.getX(), actor.getY());

        actors.forEach(target -> {
           if (target.equals(actor)) {
               output.append("[cyan]You say '" + message.getToken() + "[cyan]'");
           } else {
               GameOutput formatted = new GameOutput(String.format("[cyan]%s says '%s[cyan]'", actor.getName(), message.getToken()));

               commService.echo(target, formatted);
           }
        });
    }
}
