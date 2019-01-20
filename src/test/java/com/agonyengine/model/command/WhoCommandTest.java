package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.Connection;
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
    private Connection connection;

    @Mock
    private GameOutput output;

    private List<Actor> onlineActors = new ArrayList<>();
    private List<Connection> actorConnections = new ArrayList<>();

    private WhoCommand whoCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for (int i = 0; i < MOCK_ACTORS; i++) {
            Actor mockActor = mock(Actor.class);
            Connection mockConnection = mock(Connection.class);

            when(mockActor.getName()).thenReturn("Actor-" + i);
            when(mockActor.getConnection()).thenReturn(mockConnection);

            onlineActors.add(mockActor);
            actorConnections.add(mockConnection);
        }

        when(actor.getConnection()).thenReturn(connection);
        when(actor.getName()).thenReturn("Kadne");

        onlineActors.add(actor);

        when(actorRepository.findByConnectionIsNotNullAndRoomIdIsNotNull(any(Sort.class))).thenReturn(onlineActors);

        whoCommand = new WhoCommand(actorRepository);
    }

    @Test
    public void testInvoke() {
        whoCommand.invoke(actor, output);

        verify(actorRepository).findByConnectionIsNotNullAndRoomIdIsNotNull(any(Sort.class));

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

        verify(actorRepository).findByConnectionIsNotNullAndRoomIdIsNotNull(any(Sort.class));

        verify(output).append(contains("Who is Online"));
        verify(output).append(contains("Kadne"));
        verify(output).append(contains("1 player online."));
    }

    @Test
    public void testInvokeLinkDead() {
        when(actorConnections.get(0).getDisconnectedDate()).thenReturn(new Date());

        whoCommand.invoke(actor, output);

        verify(actorRepository).findByConnectionIsNotNullAndRoomIdIsNotNull(any(Sort.class));

        verify(output).append(contains("Who is Online"));

        for (int i = 0; i < MOCK_ACTORS; i++) {
            verify(output).append(contains("Actor-" + i));
        }

        verify(output, times(1)).append(contains("LINK DEAD"));
        verify(output).append(contains("6 players online."));
    }
}
