package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.CreatureInfo;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.generator.BodyGenerator;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.resource.exception.NoSuchActorException;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static com.agonyengine.model.actor.WearLocation.LEG_LOWER;

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
    private InvokerService invokerService;
    private CommService commService;
    private BodyGenerator bodyGenerator;
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
        InvokerService invokerService,
        CommService commService,
        BodyGenerator bodyGenerator) {

        this.applicationVersion = applicationVersion;
        this.applicationBootDate = applicationBootDate;
        this.defaultMapId = defaultMapId;
        this.inputTokenizer = inputTokenizer;
        this.gameMapRepository = gameMapRepository;
        this.sessionRepository = sessionRepository;
        this.actorRepository = actorRepository;
        this.invokerService = invokerService;
        this.commService = commService;
        this.bodyGenerator = bodyGenerator;

        InputStream greetingInputStream = WebSocketResource.class.getResourceAsStream("/greeting.txt");
        BufferedReader greetingReader = new BufferedReader(new InputStreamReader(greetingInputStream));

        greeting = greetingReader.lines().collect(Collectors.toList());
    }

    @Transactional
    @SubscribeMapping("/queue/output")
    public GameOutput onSubscribe(Principal principal, Message<byte[]> message, @Header("actor") String actorId) {
        Session session = getSpringSession(message);
        GameOutput output = new GameOutput();
        UUID actorUuid = UUID.fromString(actorId);
        Actor actor = actorRepository.findById(UUID.fromString(actorId))
            .orElseThrow(() -> new NoSuchActorException("Actor not found: " + actorUuid.toString()));

        if (actor.getInventory() == null) {
            GameMap inventoryMap = new GameMap();

            inventoryMap.setWidth(1);
            inventoryMap.setTiles(new byte[] { (byte)0xFF });

            inventoryMap = gameMapRepository.save(inventoryMap);

            actor.setInventory(inventoryMap);
        }

        // TEMPORARY
        // Delete any first generation bodies and generate new ones.
        // This block can be removed once all first generation bodies are upgraded in prod.
        if (actor.getCreatureInfo() != null && actor.getCreatureInfo().getBodyParts().stream().noneMatch(part -> LEG_LOWER == part.getWearLocation())) {
            actor.setCreatureInfo(null);
            actor = actorRepository.save(actor);
        }

        if (actor.getCreatureInfo() == null) {
            CreatureInfo creatureInfo = new CreatureInfo();

            creatureInfo.setBodyParts(bodyGenerator.generate(BodyGenerator.HUMANOID_TEMPLATE));

            actor.setCreatureInfo(creatureInfo);
        }

        actor.getConnection().setRemoteIpAddress(session.getAttribute("remoteIpAddress"));

        if (actor.getGameMap() == null) {
            GameMap defaultMap = gameMapRepository.getOne(defaultMapId);

            actor.setGameMap(defaultMap);
            actor.setX(0);
            actor.setY(0);

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

            commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s appears in a puff of smoke!", actor.getName())), actor);

            LOGGER.info("{} has connected ({})", actor.getName(), actor.getConnection().getRemoteIpAddress());
        } else {
            commService.echo(actor, handleReconnectedSession(new GameOutput()));
            commService.echoToRoom(actor, new GameOutput(String.format("[yellow]%s has reconnected.", actor.getName())), actor);

            LOGGER.info("{} has reconnected ({})", actor.getName(), actor.getConnection().getRemoteIpAddress());
        }

        actor.getConnection().setSessionUsername(principal.getName());
        actor.getConnection().setSessionId(getStompSessionId(message));
        actor.getConnection().setDisconnectedDate(null);

        actor = actorRepository.save(actor);

        // LOOK, then prompt
        invokerService.invoke(actor, output, null, Collections.singletonList("look"));

        output.append("");
        output.append("[dwhite]> ");

        return output;
    }

    @Transactional
    @MessageMapping("/input")
    @SendToUser(value = "/queue/output", broadcast = false)
    public GameOutput onInput(Principal principal, UserInput input, Message<byte[]> message) {
        GameOutput output = new GameOutput();
        Actor actor = actorRepository.findByConnectionSessionUsernameAndConnectionSessionId(principal.getName(), getStompSessionId(message));

        if (actor == null) {
            return handleReconnectedSession(output);
        }

        List<List<String>> sentences = inputTokenizer.tokenize(input.getInput());

        if (sentences.size() > 0) {
            List<String> tokens = sentences.get(0);

            try {
                invokerService.invoke(actor, output, input, tokens);
            } catch (Exception e) {
                output.append("[red]" + e.getMessage());
                LOGGER.error(e.getMessage(), e);
            }
        }

        output
            .append("")
            .append("[dwhite]> ");

        return output;
    }

    private Session getSpringSession(Message<byte[]> message) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);
        String sessionId;

        if (headerAccessor.getSessionAttributes() != null) {
            sessionId = (String) headerAccessor.getSessionAttributes().get(SPRING_SESSION_ID_KEY);

            return sessionRepository.findById(sessionId);
        }

        return null;
    }

    private String getStompSessionId(Message<byte[]> message) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);

        return headerAccessor.getSessionId();
    }

    private GameOutput handleReconnectedSession(GameOutput output) {
        output.append("[yellow]Your connection has been reconnected in another browser!");
        output.append("<script type=\"text/javascript\">setTimeout(function() { window.location=\"/account\"; }, 1000);</script>");

        return output;
    }
}
