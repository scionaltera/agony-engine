package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.command.LookCommand;
import com.agonyengine.model.command.SayCommand;
import com.agonyengine.model.interpret.ActorSameRoom;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.interpret.Verb;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.VerbRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

public class InvokerServiceTest {
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private VerbRepository verbRepository;

    @Mock
    private Verb lookVerb;

    @Mock
    private Verb sayVerb;

    @Mock
    private LookCommand lookCommand;

    @Mock
    private SayCommand sayCommand;

    @Mock
    private ActorSameRoom actorSameRoom;

    @Mock
    private QuotedString quotedString;

    @Mock
    private Actor actor;

    @Mock
    private Actor target;

    @Mock
    private GameOutput output;

    private InvokerService invokerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(lookVerb.getBean()).thenReturn("lookCommand");
        when(sayVerb.getBean()).thenReturn("sayCommand");
        when(applicationContext.getBean(eq("lookCommand"))).thenReturn(lookCommand);
        when(applicationContext.getBean(eq("sayCommand"))).thenReturn(sayCommand);
        when(applicationContext.getBean(eq(QuotedString.class))).thenReturn(quotedString);
        when(applicationContext.getBean(eq(ActorSameRoom.class))).thenReturn(actorSameRoom);
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("LOOK")))
            .thenReturn(Optional.of(lookVerb));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("SAY")))
            .thenReturn(Optional.of(sayVerb));
        when(quotedString.bind(eq(actor), anyString())).thenReturn(true);
        when(quotedString.getText()).thenReturn("foo");
        when(actorSameRoom.bind(eq(actor), anyString())).thenReturn(true);
        when(actorSameRoom.getTarget()).thenReturn(target);

        invokerService = new InvokerService(applicationContext, verbRepository);
    }

    @Test
    public void testInvokeNoArgs() {
        invokerService.invoke(actor, output, null, Collections.singletonList("LOOK"));

        verify(lookCommand).invoke(eq(actor), eq(output));
    }

    @Test
    public void testInvokeQuotedString() {
        UserInput input = new UserInput();
        QuotedString quoted = new QuotedString();

        input.setInput("say foo");
        quoted.bind(actor, input.getInput());

        invokerService.invoke(actor, output, input, Arrays.asList("SAY", "FOO"));

        verify(sayCommand).invoke(any(Actor.class), any(GameOutput.class), any(QuotedString.class));
    }
}
