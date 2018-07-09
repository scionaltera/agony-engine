package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import org.springframework.stereotype.Component;

@Component
public class LookCommand {
    public void invoke(Actor actor, GameOutput output) {
        output.append("[black]You see nothing but the inky void swirling around you.");
    }
}
