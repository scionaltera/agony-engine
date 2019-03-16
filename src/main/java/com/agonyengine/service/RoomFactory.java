package com.agonyengine.service;

import com.agonyengine.model.map.Direction;
import com.agonyengine.model.map.Room;
import com.agonyengine.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@Component
public class RoomFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomFactory.class);

    private Random random;
    private RoomRepository roomRepository;

    public RoomFactory(
        Random random,
        RoomRepository roomRepository) {

        this.random = random;
        this.roomRepository = roomRepository;
    }

    public Optional<Room> get(Long x, Long y, Long z) {
        return roomRepository.findByLocationXAndLocationYAndLocationZ(x, y, z);
    }

    public Room build() {
        return roomRepository.save(new Room());
    }

    public Room getOrBuild(Room from, Direction to) {
        Room room = internalGetOrBuild(
            from.getLocation().getX() + to.getX(),
            from.getLocation().getY() + to.getY(),
            from.getLocation().getZ() + to.getZ());

        room.getExits().add(to.toOpposite());

        return roomRepository.save(room);
    }

    public Room getOrBuild(Long x, Long y, Long z) {
        Room room = internalGetOrBuild(x, y, z);

        if (room.getId() == null) {
            return roomRepository.save(room);
        }

        return room;
    }

    private Room internalGetOrBuild(Long x, Long y, Long z) {
        return roomRepository
            .findByLocationXAndLocationYAndLocationZ(x, y, z)
            .orElseGet(() -> {
                Room r = new Room();

                r.getLocation().setX(x);
                r.getLocation().setY(y);
                r.getLocation().setZ(z);

                LOGGER.debug("Created new Room at {}", r.getLocation());

                Arrays
                    .stream(Direction.values())
                    .forEach(direction -> {
                        if (random.nextBoolean()) {
                            r.getExits().add(direction);

                            // If there is a neighbor in the direction we just added an exit to
                            // make sure it has a reciprocal exit back to us so we don't create one way exits.
                            roomRepository
                                .findByLocationXAndLocationYAndLocationZ(
                                    r.getLocation().getX() + direction.getX(),
                                    r.getLocation().getY() + direction.getY(),
                                    r.getLocation().getZ() + direction.getZ())
                                .ifPresent(neighbor -> {
                                    neighbor.getExits().add(direction.toOpposite());

                                    roomRepository.save(neighbor);
                                });
                        }
                });

                return r;
        });
    }
}
