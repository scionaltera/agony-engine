package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ScoreCommand {
    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        output.append("[dcyan][ [cyan]Your Score [dcyan]]");
        output.append(String.format("[cyan]Name: [dcyan]%s", actor.getName()));
        output.append(String.format("[cyan]Pronouns: [dcyan]%s/%s", actor.getPronoun().getSubject(), actor.getPronoun().getObject()));
    }
}
