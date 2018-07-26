package com.agonyengine.repository;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.PlayerActorTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {
    Actor findBySessionUsernameAndSessionId(String sessionUsername, String sessionId);
    List<Actor> findByGameMapAndXAndY(GameMap gameMap, Integer x, Integer y);
    Optional<Actor> findByActorTemplate(PlayerActorTemplate playerActorTemplate);
    List<Actor> findBySessionUsernameIsNotNullAndSessionIdIsNotNull(Sort sort);
}
