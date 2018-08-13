package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WhoCommandTest {
    private static final int MOCK_ACTORS = 5;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private Actor actor;

    @Mock
    private GameOutput output;

    private List<Actor> onlineActors = new ArrayList<>();

    private WhoCommand whoCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for (int i = 0; i < MOCK_ACTORS; i++) {
            Actor mockActor = mock(Actor.class);

            when(mockActor.getName()).thenReturn("Actor-" + i);

            onlineActors.add(mockActor);
        }

        when(actor.getName()).thenReturn("Kadne");

        onlineActors.add(actor);

        when(actorRepository.findBySessionUsernameIsNotNullAndSessionIdIsNotNullAndGameMapIsNotNull(any(Sort.class))).thenReturn(onlineActors);

        whoCommand = new WhoCommand(actorRepository);
    }

    @Test
    public void testInvoke() {
        whoCommand.invoke(actor, output);

        verify(actorRepository).findBySessionUsernameIsNotNullAndSessionIdIsNotNullAndGameMapIsNotNull(any(Sort.class));

        verify(output).append(contains("Who is Online"));

        for (int i = 0; i < MOCK_ACTORS; i++) {
            verify(output).append(contains("Actor-" + i));
        }

        verify(output).append(contains("6 players online."));
    }

    @Test
    public void testInvokeSinglePlayer() {
        onlineActors.clear();
        onlineActors.add(actor);

        whoCommand.invoke(actor, output);

        verify(actorRepository).findBySessionUsernameIsNotNullAndSessionIdIsNotNullAndGameMapIsNotNull(any(Sort.class));

        verify(output).append(contains("Who is Online"));
        verify(output).append(contains("Kadne"));
        verify(output).append(contains("1 player online."));
    }

    @Test
    public void testInvokeLinkDead() {
        when(onlineActors.get(0).getDisconnectedDate()).thenReturn(new Date());

        whoCommand.invoke(actor, output);

        verify(actorRepository).findBySessionUsernameIsNotNullAndSessionIdIsNotNullAndGameMapIsNotNull(any(Sort.class));

        verify(output).append(contains("Who is Online"));

        for (int i = 0; i < MOCK_ACTORS; i++) {
            verify(output).append(contains("Actor-" + i));
        }

        verify(output, times(1)).append(contains("LINK DEAD"));
        verify(output).append(contains("6 players online."));
    }
}
