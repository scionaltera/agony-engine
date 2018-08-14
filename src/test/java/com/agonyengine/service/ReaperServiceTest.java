package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.Connection;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ReaperServiceTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private CommService commService;

    @Captor
    private ArgumentCaptor<Date> dateArgumentCaptor;

    private List<Actor> actors = new ArrayList<>();

    private ReaperService reaperService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for (int i = 0; i < 5; i++) {
            Actor actor = mock(Actor.class);
            Connection connection = mock(Connection.class);

            when(actor.getConnection()).thenReturn(connection);
            when(actor.getName()).thenReturn("Actor-" + i);
            when(connection.getDisconnectedDate()).thenReturn(new Date(0L));

            actors.add(actor);
        }

        when(actorRepository.findByConnectionDisconnectedDateIsBeforeAndGameMapIsNotNull(any(Date.class))).thenReturn(actors);

        reaperService = new ReaperService(actorRepository, commService);
    }

    @Test
    public void testReap() {
        reaperService.reapLinkDeadActors();

        verify(actorRepository).findByConnectionDisconnectedDateIsBeforeAndGameMapIsNotNull(dateArgumentCaptor.capture());
        verify(actorRepository).saveAll(actors);

        actors.forEach(actor -> {
            verify(commService).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));
            verify(actor).setGameMap(isNull());
        });

        Date cutoff = dateArgumentCaptor.getValue();

        assertTrue(cutoff.before(new Date()));
    }
}
