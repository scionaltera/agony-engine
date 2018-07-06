package com.agonyengine.repository;

import com.agonyengine.model.actor.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameMapRepository extends JpaRepository<GameMap, UUID> {
}
