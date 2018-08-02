package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.service.CommService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Component
public class SayCommand {
    private CommService commService;

    @Inject
    public SayCommand(CommService commService) {
        this.commService = commService;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, QuotedString message) {
        commService.echoToRoom(actor, new GameOutput(String.format("[cyan]%s says '%s[cyan]'", StringUtils.capitalize(actor.getName()), message.getToken())), actor);
        output.append("[cyan]You say '" + message.getToken() + "[cyan]'");
    }
}
