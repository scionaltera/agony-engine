package com.agonyengine.repository;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {
    Actor findBySessionUsername(String sessionUsername);
    List<Actor> findByGameMapAndXAndY(GameMap gameMap, Integer x, Integer y);
}
