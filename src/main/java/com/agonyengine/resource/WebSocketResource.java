package com.agonyengine.resource;

import com.agonyengine.model.actor.PlayerActorTemplate;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.PlayerActorTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class WebSocketResource {
    static final String SPRING_SESSION_ID_KEY = "SPRING.SESSION.ID";

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketResource.class);

    private String applicationVersion;
    private Date applicationBootDate;
    private SessionRepository sessionRepository;
    private PlayerActorTemplateRepository playerActorTemplateRepository;
    private List<String> greeting;

    @Inject
    public WebSocketResource(
        String applicationVersion,
        Date applicationBootDate,
        SessionRepository sessionRepository,
        PlayerActorTemplateRepository playerActorTemplateRepository) {

        this.applicationVersion = applicationVersion;
        this.applicationBootDate = applicationBootDate;
        this.sessionRepository = sessionRepository;
        this.playerActorTemplateRepository = playerActorTemplateRepository;

        InputStream greetingInputStream = WebSocketResource.class.getResourceAsStream("/greeting.txt");
        BufferedReader greetingReader = new BufferedReader(new InputStreamReader(greetingInputStream));

        greeting = greetingReader.lines().collect(Collectors.toList());
    }

    @SubscribeMapping("/queue/output")
    public GameOutput onSubscribe() {
        GameOutput output = new GameOutput();

        greeting.forEach(line -> {
            if (line.startsWith("*")) {
                output.append(line.substring(1));
            } else {
                output.append(line.replace(" ", "&nbsp;"));
            }
        });

        output.append("[dwhite]Server status:");
        output.append("[dwhite]&nbsp;&nbsp;Version: [white]v" + applicationVersion);
        output.append("[dwhite]&nbsp;&nbsp;Last boot: [white]" + applicationBootDate);
        output.append("[dyellow]A relentless grinding rattles your very soul as [red]The Agony Engine " +
            "[dyellow]carries out its barbarous task...");
        output.append("");
        output.append("[dwhite]> ");

        return output;
    }

    @MessageMapping("/input")
    @SendToUser(value = "/queue/output", broadcast = false)
    public GameOutput onInput(Principal principal, UserInput input, Message<byte[]> message) {
        Session session = getSession(message);

        if (session == null) {
            LOGGER.error("Unable to find session for authenticated user: {}", principal.getName());

            // Without a session the user probably won't see this, but it's worth a try anyway...
            return new GameOutput(
                "[red]ERROR: Could not find your HTTP session!",
                "[red]Please try logging out, clearing cookies, restarting your browser and logging back in.",
                "[red]The administrators have been notified of the problem.");
        }

        PlayerActorTemplate pat = playerActorTemplateRepository
            .findById(UUID.fromString(session.getAttribute("actor")))
            .orElse(null);

        LOGGER.info("Input: " + input.getInput());

        return new GameOutput((pat != null ? pat.getGivenName() : "Someone") + ": " + input.getInput());
    }

    private Session getSession(Message<byte[]> message) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);
        String sessionId = (String)headerAccessor.getSessionAttributes().get(SPRING_SESSION_ID_KEY);

        return sessionRepository.findById(sessionId);
    }
}
