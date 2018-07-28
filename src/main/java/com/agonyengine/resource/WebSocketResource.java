package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.PlayerActorTemplate;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.repository.PlayerActorTemplateRepository;
import com.agonyengine.resource.exception.NoSuchActorException;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
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
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class WebSocketResource {
    static final String SPRING_SESSION_ID_KEY = "SPRING.SESSION.ID";

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketResource.class);

    private String applicationVersion;
    private Date applicationBootDate;
    private UUID defaultMapId;
    private InputTokenizer inputTokenizer;
    private GameMapRepository gameMapRepository;
    private SessionRepository sessionRepository;
    private ActorRepository actorRepository;
    private PlayerActorTemplateRepository playerActorTemplateRepository;
    private InvokerService invokerService;
    private CommService commService;
    private List<String> greeting;

    @Inject
    public WebSocketResource(
        String applicationVersion,
        Date applicationBootDate,
        UUID defaultMapId,
        InputTokenizer inputTokenizer,
        GameMapRepository gameMapRepository,
        SessionRepository sessionRepository,
        ActorRepository actorRepository,
        PlayerActorTemplateRepository playerActorTemplateRepository,
        InvokerService invokerService,
        CommService commService) {

        this.applicationVersion = applicationVersion;
        this.applicationBootDate = applicationBootDate;
        this.defaultMapId = defaultMapId;
        this.inputTokenizer = inputTokenizer;
        this.gameMapRepository = gameMapRepository;
        this.sessionRepository = sessionRepository;
        this.actorRepository = actorRepository;
        this.playerActorTemplateRepository = playerActorTemplateRepository;
        this.invokerService = invokerService;
        this.commService = commService;

        InputStream greetingInputStream = WebSocketResource.class.getResourceAsStream("/greeting.txt");
        BufferedReader greetingReader = new BufferedReader(new InputStreamReader(greetingInputStream));

        greeting = greetingReader.lines().collect(Collectors.toList());
    }

    @Transactional
    @SubscribeMapping("/queue/output")
    public GameOutput onSubscribe(Principal principal, Message<byte[]> message) {
        Session session = getSpringSession(message);
        GameOutput output = new GameOutput();
        UUID actorTemplateId = UUID.fromString(session.getAttribute("actor_template"));
        PlayerActorTemplate pat = playerActorTemplateRepository
            .findById(actorTemplateId)
            .orElseThrow(() -> new NoSuchActorException("Player Actor Template not found: " + actorTemplateId.toString()));
        Actor actor = actorRepository.findByActorTemplate(pat)
            .orElse(null);

        if (actor == null) {
            GameMap defaultMap = gameMapRepository.getOne(defaultMapId);

            actor = new Actor();

            actor.setActorTemplate(pat);
            actor.setName(pat.getGivenName());
            actor.setSessionUsername(principal.getName());
            actor.setSessionId(getStompSessionId(message));
            actor.setRemoteIpAddress(session.getAttribute("remoteIpAddress"));
            actor.setGameMap(defaultMap);
            actor.setX(0);
            actor.setY(0);

            actor = actorRepository.save(actor);

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

            LOGGER.info("{} has connected ({})", actor.getName(), actor.getRemoteIpAddress());

            commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s appears in a puff of smoke!", actor.getName())), actor);
        } else {
            actor.setDisconnectedDate(null);
            actor.setSessionUsername(principal.getName());
            actor.setSessionId(getStompSessionId(message));
            actor.setRemoteIpAddress(session.getAttribute("remoteIpAddress"));

            actor = actorRepository.save(actor);

            LOGGER.info("{} has reconnected ({})", actor.getName(), actor.getRemoteIpAddress());

            commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s has reconnected.", actor.getName())), actor);
        }

        invokerService.invoke(actor, output, null, Collections.singletonList("look"));

        output.append("");
        output.append("[dwhite]> ");

        return output;
    }

    @Transactional
    @MessageMapping("/input")
    @SendToUser(value = "/queue/output", broadcast = false)
    public GameOutput onInput(Principal principal, UserInput input, Message<byte[]> message) {
        Actor actor = actorRepository.findBySessionUsernameAndSessionId(principal.getName(), getStompSessionId(message));
        GameOutput output = new GameOutput();
        List<List<String>> sentences = inputTokenizer.tokenize(input.getInput());
        List<String> tokens = sentences.get(0);

        try {
            invokerService.invoke(actor, output, input, tokens);
        } catch (Exception e) {
            output.append("[red]" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        output
            .append("")
            .append("[dwhite]> ");

        return output;
    }

    private Session getSpringSession(Message<byte[]> message) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);
        String sessionId = (String)headerAccessor.getSessionAttributes().get(SPRING_SESSION_ID_KEY);

        return sessionRepository.findById(sessionId);
    }

    private String getStompSessionId(Message<byte[]> message) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);

        return headerAccessor.getSessionId();
    }
}
