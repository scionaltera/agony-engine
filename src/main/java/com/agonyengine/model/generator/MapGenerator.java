package com.agonyengine.model.generator;

import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.Tile;
import com.agonyengine.model.actor.TileFlag;
import com.agonyengine.model.actor.Tileset;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.repository.TileRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class MapGenerator {
    private static final int MAP_ADDITIONAL_WIDTH = 15;

    static final int MAP_MIN_WIDTH = 10;

    private final Random RANDOM;

    private GameMapRepository gameMapRepository;
    private TileRepository tileRepository;

    @Inject
    public MapGenerator(GameMapRepository gameMapRepository, TileRepository tileRepository) {
        this.gameMapRepository = gameMapRepository;
        this.tileRepository = tileRepository;

        RANDOM = new Random();
    }

    MapGenerator(
        GameMapRepository gameMapRepository,
        TileRepository tileRepository,
        Random random) {

        this.gameMapRepository = gameMapRepository;
        this.tileRepository = tileRepository;
        RANDOM = random;
    }

    public GameMap generateMap(Tileset tileset) {
        int width = MAP_MIN_WIDTH + RANDOM.nextInt(MAP_ADDITIONAL_WIDTH);
        GameMap map = new GameMap(width, new byte[width * width]);

        map.setTileset(tileset);
        floodFill(map, tileset);

        return gameMapRepository.save(map);
    }

    private void floodFill(GameMap map, Tileset tileset) {
        List<Tile> tiles = tileRepository.findByTileset(tileset);
        List<Tile> wilderness = tiles.stream()
            .filter(tile -> tile.getFlags().contains(TileFlag.WILDERNESS))
            .collect(Collectors.toList());

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getWidth(); y++) {
                Collections.shuffle(wilderness);
                map.setTile(x, y, (byte)wilderness.get(0).getIndex());
            }
        }
    }
}
