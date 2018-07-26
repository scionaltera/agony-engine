package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReaperServiceTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private CommService commService;

    private List<Actor> actors = new ArrayList<>();

    private ReaperService reaperService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for (int i = 0; i < 5; i++) {
            Actor actor = mock(Actor.class);

            when(actor.getName()).thenReturn("Actor-" + i);
            when(actor.getDisconnectedDate()).thenReturn(new Date(0L));

            actors.add(actor);
        }

        when(actorRepository.findByDisconnectedDateIsBefore(any(Date.class))).thenReturn(actors);

        reaperService = new ReaperService(actorRepository, commService);
    }

    @Test
    public void testReap() {
        reaperService.reapLinkDeadActors();

        verify(actorRepository).deleteAll(actors);
    }
}
