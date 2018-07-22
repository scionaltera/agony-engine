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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvokerServiceTest {
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private VerbRepository verbRepository;

    @Mock
    private Actor invoker;

    @Mock
    private Actor target;

    @Mock
    private GameOutput output;

    @Mock
    private UserInput input;

    @Mock
    private Verb lookVerb;

    @Mock
    private Verb sayVerb;

    @Mock
    private LookCommand lookCommand;

    @Mock
    private SayCommand sayCommand;

    @Mock
    private QuotedString quotedString;

    @Mock
    private ActorSameRoom actorSameRoom;

    private InvokerService invokerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(lookVerb.getBean()).thenReturn("lookCommand");
        when(sayVerb.getBean()).thenReturn("sayCommand");

        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(
            any(Sort.class),
            eq("LOOK")
        )).thenReturn(Optional.of(lookVerb));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(
            any(Sort.class),
            eq("SAY")
        )).thenReturn(Optional.of(sayVerb));

        when(applicationContext.getBean(eq(QuotedString.class))).thenReturn(quotedString);
        when(applicationContext.getBean(eq(ActorSameRoom.class))).thenReturn(actorSameRoom);
        when(applicationContext.getBean(eq("lookCommand"))).thenReturn(lookCommand);
        when(applicationContext.getBean(eq("sayCommand"))).thenReturn(sayCommand);

        invokerService = new InvokerService(applicationContext, verbRepository);
    }

    @Test
    public void testInvokeInvalidGrammar() {
        invokerService.invoke(invoker, output, input, Arrays.asList("LOOK", "FOO", "BAR", "BAZ"));

        verify(lookCommand, never()).invoke(any(Actor.class), any(GameOutput.class));
    }

    @Test
    public void testInvokeNoArgs() {
        invokerService.invoke(invoker, output, input, Collections.singletonList("LOOK"));

        verify(lookCommand).invoke(eq(invoker), eq(output));
    }

    @Test
    public void testInvokeOneTarget() {
        when(actorSameRoom.bind(eq(invoker), eq("MORGAN"))).thenReturn(true);
        when(actorSameRoom.getTarget()).thenReturn(target);
        when(actorSameRoom.getToken()).thenReturn("MORGAN");

        invokerService.invoke(invoker, output, input, Arrays.asList("LOOK", "MORGAN"));

        verify(lookCommand).invoke(eq(invoker), eq(output), eq(actorSameRoom));
    }

    @Test
    public void testInvokeOneTargetQuoting() {
        when(sayVerb.isQuoting()).thenReturn(true);
        when(input.getInput()).thenReturn("say This is a string.");
        when(quotedString.bind(eq(invoker), eq("This is a string."))).thenReturn(true);
        when(quotedString.getText()).thenReturn("This is a string.");
        when(quotedString.getToken()).thenReturn("This is a string.");

        invokerService.invoke(invoker, output, input, Arrays.asList("SAY", "This is a string."));

        verify(sayCommand).invoke(eq(invoker), eq(output), eq(quotedString));
    }

    @Test
    public void testInvokeOneTargetNotFound() {
        when(actorSameRoom.bind(eq(invoker), eq("MORGAN"))).thenReturn(false);
        when(actorSameRoom.getTarget()).thenReturn(null);
        when(actorSameRoom.getToken()).thenReturn("MORGAN");

        invokerService.invoke(invoker, output, input, Arrays.asList("LOOK", "MORGAN"));

        verify(lookCommand, never()).invoke(eq(invoker), eq(output), eq(actorSameRoom));
    }
}
