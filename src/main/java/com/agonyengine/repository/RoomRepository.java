package com.agonyengine.repository;

import com.agonyengine.model.map.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByLocationXAndLocationYAndLocationZ(Long x, Long y, Long z);
}
