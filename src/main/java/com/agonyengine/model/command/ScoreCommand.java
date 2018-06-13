package com.agonyengine.model.command;

import com.agonyengine.model.stomp.GameOutput;
import org.springframework.stereotype.Component;

@Component
public class ScoreCommand {
    public void invoke(GameOutput output) {
        output.append("[dwhite]Not implemented yet.");
    }
}
