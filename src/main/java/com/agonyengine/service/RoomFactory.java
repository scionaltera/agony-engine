package com.agonyengine.service;

import com.agonyengine.model.map.Biome;
import com.agonyengine.model.map.Direction;
import com.agonyengine.model.map.Location;
import com.agonyengine.model.map.Room;
import com.agonyengine.model.map.Zone;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.repository.ZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class RoomFactory {
    public static int ROOMS_PER_ZONE = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomFactory.class);

    private Random random;
    private ZoneRepository zoneRepository;
    private RoomRepository roomRepository;
    private BiomeService biomeService;

    public RoomFactory(
        Random random,
        ZoneRepository zoneRepository,
        RoomRepository roomRepository,
        BiomeService biomeService) {

        this.random = random;
        this.zoneRepository = zoneRepository;
        this.roomRepository = roomRepository;
        this.biomeService = biomeService;
    }

    public Room build() {
        return roomRepository.save(new Room());
    }

    public Room getOrBuild(Room from, Direction to) {
        Room room = internalGetOrBuild(
            from.getLocation().getX() + to.getX(),
            from.getLocation().getY() + to.getY(),
            from.getLocation().getZ() + to.getZ());

        if (room.getZone() == null) {
            Biome fromBiome = biomeService.computeBiome(from.getLocation().getX(), from.getLocation().getY());
            Biome toBiome = biomeService.computeBiome(room.getLocation().getX(), room.getLocation().getY());

            if (fromBiome.equals(toBiome)) {
                room.setZone(from.getZone());
            } else {
                Zone zone = zoneRepository.save(new Zone());

                LOGGER.debug("Created new Zone {}", zone.getId());

                room.setZone(zone);
            }
        }

        room.getExits().add(to.toOpposite());

        return roomRepository.save(room);
    }

    public Room getOrBuild(Long x, Long y, Long z) {
        Room room = internalGetOrBuild(x, y, z);

        if (room.getZone() == null) {
            Zone zone = zoneRepository.save(new Zone());

            LOGGER.debug("Created new Zone {}", zone.getId());

            room.setZone(zone);
        }

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
