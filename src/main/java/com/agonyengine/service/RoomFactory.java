package com.agonyengine.service;

import com.agonyengine.model.map.Direction;
import com.agonyengine.model.map.Room;
import com.agonyengine.repository.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class RoomFactory {
    private Random random;
    private RoomRepository roomRepository;

    public RoomFactory(Random random, RoomRepository roomRepository) {
        this.random = random;
        this.roomRepository = roomRepository;
    }

    public Room build() {
        return roomRepository.save(new Room());
    }

    public Room getOrBuild(Room from, Direction to) {
        Room room = internalGetOrBuild(
            from.getLocation().getX() + to.getX(),
            from.getLocation().getY() + to.getY(),
            from.getLocation().getZ() + to.getZ());

        // if the "from" room doesn't have an exit into the new room, add one
        if (!from.getExits().contains(to)) {
            from.getExits().add(to);

            roomRepository.save(from);
        }

        // make sure the new room has an exit into the original room too
        Direction opposite = Direction.valueOf(to.getOpposite().toUpperCase());

        if (!room.getExits().contains(opposite)) {
            room.getExits().add(opposite);
        }

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

                Arrays
                    .stream(Direction.values())
                    .forEach(direction -> {
                        if (random.nextBoolean()) {
                            r.getExits().add(direction);
                        }
                });

                return r;
        });
    }
}
