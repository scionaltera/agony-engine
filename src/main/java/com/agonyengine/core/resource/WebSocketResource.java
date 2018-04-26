package com.agonyengine.core.resource;

import com.agonyengine.core.model.stomp.GameOutput;
import com.agonyengine.core.model.stomp.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketResource.class);

    @SubscribeMapping("/queue/output")
    public GameOutput onSubscribe() {
        LOGGER.info("Subscribe");

        return new GameOutput("Subscribed!");
    }

    @MessageMapping("/input")
    @SendToUser(value = "/queue/output", broadcast = false)
    public GameOutput onInput(UserInput input) {
        LOGGER.info("Input: " + input.getInput());

        return new GameOutput(": " + input.getInput());
    }
}
