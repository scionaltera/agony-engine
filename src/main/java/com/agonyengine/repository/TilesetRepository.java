package com.agonyengine.repository;

import com.agonyengine.model.actor.Tileset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TilesetRepository extends JpaRepository<Tileset, UUID> {
}
