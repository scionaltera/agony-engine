package com.agonyengine.model.command;

import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.stomp.GameOutput;
import org.springframework.stereotype.Component;

@Component
public class SayCommand {
    public void invoke(GameOutput output, QuotedString message) {
        output.append("[cyan]You say '" + message.getText() + "[cyan]'");
    }
}
