package com.agonyengine.repository;

import com.agonyengine.model.actor.Tile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TileRepository extends JpaRepository<Tile, Byte> {
}
