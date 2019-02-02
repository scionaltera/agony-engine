package com.agonyengine.service;

import com.agonyengine.model.map.Direction;
import com.agonyengine.model.map.Room;
import com.agonyengine.repository.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoomFactoryTest {
    @Mock
    private Random random;

    @Mock
    private RoomRepository roomRepository;

    private RoomFactory roomFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        standardMocks();

        roomFactory = new RoomFactory(random, roomRepository);
    }

    @Test
    public void testBuild() {
        Room room = roomFactory.build();

        assertNull(room.getLocation().getX());
        assertNull(room.getLocation().getY());
        assertNull(room.getLocation().getZ());
        assertTrue(room.getExits().isEmpty());

        verifyZeroInteractions(random);
        verify(roomRepository).save(eq(room));
    }

    @Test
    public void testBuildAtCoords() {
        Room room = roomFactory.getOrBuild(0L, 0L, 0L);

        assertEquals(0L, (long)room.getLocation().getX());
        assertEquals(0L, (long)room.getLocation().getY());
        assertEquals(0L, (long)room.getLocation().getZ());
        assertTrue(room.getExits().contains(Direction.NORTH));
        assertTrue(room.getExits().contains(Direction.EAST));

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    public void testBuildInDirectionWithoutReciprocalExit() {
        Room origin = generateStartRoom();

        when(roomRepository.findByLocationXAndLocationYAndLocationZ(0L, 0L, 0L)).thenReturn(Optional.of(origin));

        Room destination = roomFactory.getOrBuild(origin, Direction.NORTH);

        assertEquals(Direction.NORTH.getX(), (long)destination.getLocation().getX());
        assertEquals(Direction.NORTH.getY(), (long)destination.getLocation().getY());
        assertEquals(Direction.NORTH.getZ(), (long)destination.getLocation().getZ());

        assertTrue(destination.getExits().contains(Direction.NORTH)); // "random" exit
        assertTrue(destination.getExits().contains(Direction.EAST));  // "random" exit
        assertTrue(destination.getExits().contains(Direction.SOUTH)); // factory-added exit
        assertFalse(destination.getExits().contains(Direction.WEST)); // shouldn't be set

        verify(roomRepository).save(eq(destination));
        verify(roomRepository, never()).save(eq(origin));
    }

    @Test
    public void testBuildInDirectionWithReciprocalExit() {
        Room origin = generateStartRoom();

        when(roomRepository.findByLocationXAndLocationYAndLocationZ(0L, 0L, 0L)).thenReturn(Optional.of(origin));

        Room destination = roomFactory.getOrBuild(origin, Direction.WEST);

        assertEquals(Direction.WEST.getX(), (long)destination.getLocation().getX());
        assertEquals(Direction.WEST.getY(), (long)destination.getLocation().getY());
        assertEquals(Direction.WEST.getZ(), (long)destination.getLocation().getZ());

        assertTrue(origin.getExits().contains(Direction.WEST)); // factory-added exit

        assertTrue(destination.getExits().contains(Direction.NORTH)); // "random" exit
        assertTrue(destination.getExits().contains(Direction.EAST));  // "random" exit
        assertFalse(destination.getExits().contains(Direction.SOUTH)); // shouldn't be set
        assertFalse(destination.getExits().contains(Direction.WEST)); // shouldn't be set

        verify(roomRepository).save(eq(destination));
        verify(roomRepository).save(eq(origin));
    }

    private void standardMocks() {
        when(random.nextBoolean()).thenReturn(true, true, false);

        when(roomRepository.save(any(Room.class))).thenAnswer(i -> {
            Room room = i.getArgument(0);

            if (room.getId() == null) {
                room.setId(UUID.randomUUID());
            }

            return room;
        });
    }

    private Room generateStartRoom() {
        Room start = roomFactory.getOrBuild(0L, 0L, 0L);

        reset(roomRepository, random);

        standardMocks();

        return start;
    }
}
