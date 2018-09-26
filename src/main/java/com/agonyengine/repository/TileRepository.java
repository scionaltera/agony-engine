package com.agonyengine.repository;

import com.agonyengine.model.actor.Tile;
import com.agonyengine.model.actor.Tileset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TileRepository extends JpaRepository<Tile, Byte> {
    List<Tile> findByTileset(Tileset tileset);
}
