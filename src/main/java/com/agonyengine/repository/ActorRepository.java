package com.agonyengine.repository;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.GameMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {
    Actor findByConnectionSessionUsernameAndConnectionSessionId(String sessionUsername, String sessionId);
    List<Actor> findByGameMap(GameMap gameMap);
    List<Actor> findByGameMapAndXAndY(GameMap gameMap, Integer x, Integer y);
    List<Actor> findByConnectionAccount(String account);
    List<Actor> findByConnectionIsNotNullAndGameMapIsNotNull(Sort sort);
    List<Actor> findByConnectionDisconnectedDateIsBeforeAndGameMapIsNotNull(Date cutoff);
}
