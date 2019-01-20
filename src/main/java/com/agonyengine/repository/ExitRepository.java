package com.agonyengine.repository;

import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.map.Exit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Deprecated
public interface ExitRepository extends JpaRepository<Exit, UUID> {
    Exit findByDirectionAndLocationGameMapAndLocationXAndLocationY(String direction, GameMap gameMap, Integer x, Integer y);
}
