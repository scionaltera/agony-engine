package com.agonyengine.model.generator;

import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.Tile;
import com.agonyengine.model.actor.Tileset;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.repository.TileRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.agonyengine.model.actor.TileFlag.IMPASSABLE;
import static com.agonyengine.model.actor.TileFlag.WILDERNESS;
import static com.agonyengine.model.generator.MapGenerator.MAP_MIN_WIDTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MapGeneratorTest {
    @Mock
    private GameMapRepository gameMapRepository;

    @Mock
    private TileRepository tileRepository;

    @Mock
    private Tileset tileset;

    private List<Tile> fakeTiles;

    private MapGenerator mapGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        fakeTiles = generateTiles();

        when(gameMapRepository.save(any(GameMap.class))).thenAnswer(i -> {
            GameMap map = i.getArgument(0);

            map.setId(UUID.randomUUID());

            return map;
        });

        when(tileRepository.findByTileset(any(Tileset.class))).thenReturn(fakeTiles);

        mapGenerator = new MapGenerator(gameMapRepository, tileRepository);
    }

    @Test
    public void testGeneratorTouchesAllTiles() {
        GameMap map = mapGenerator.generateMap(tileset);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getWidth(); y++) {
                assertTrue(map.getTile(x, y).getIndex() != 0);
            }
        }

        verify(gameMapRepository).save(eq(map));
    }

    @Test
    public void testMapWidthIsWithinBounds() {
        final Random mockRandom = mock(Random.class);
        final int mapAdditionalWidth = 7;

        when(mockRandom.nextInt(anyInt())).thenReturn(mapAdditionalWidth);

        MapGenerator mapGenerator = new MapGenerator(gameMapRepository, tileRepository, mockRandom);
        GameMap map = mapGenerator.generateMap(tileset);

        assertEquals(MAP_MIN_WIDTH + mapAdditionalWidth, map.getWidth());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testWildernessTileShuffle() {
        GameMap map = mapGenerator.generateMap(tileset);

        fakeTiles.stream()
            .filter(tile -> tile.getFlags().contains(WILDERNESS))
            .forEach(tile -> verify(tile, atLeastOnce()).getIndex());

        fakeTiles.stream()
            .filter(tile -> !tile.getFlags().contains(WILDERNESS))
            .forEach(tile -> verify(tile, never()).getIndex());

        verify(gameMapRepository).save(eq(map));
    }

    private List<Tile> generateTiles() {
        List<Tile> allTiles = new ArrayList<>();
        Tile impassable = mock(Tile.class);
        Tile wilderness1 = mock(Tile.class);
        Tile wilderness2 = mock(Tile.class);
        Tile wilderness3 = mock(Tile.class);

        when(impassable.getFlags()).thenReturn(EnumSet.of(IMPASSABLE));
        when(wilderness1.getFlags()).thenReturn(EnumSet.of(WILDERNESS));
        when(wilderness2.getFlags()).thenReturn(EnumSet.of(WILDERNESS));
        when(wilderness3.getFlags()).thenReturn(EnumSet.of(WILDERNESS));

        when(impassable.getIndex()).thenReturn(1);
        when(wilderness1.getIndex()).thenReturn(2);
        when(wilderness2.getIndex()).thenReturn(3);
        when(wilderness3.getIndex()).thenReturn(4);

        when(tileset.getTile(1)).thenReturn(impassable);
        when(tileset.getTile(2)).thenReturn(wilderness1);
        when(tileset.getTile(3)).thenReturn(wilderness2);
        when(tileset.getTile(4)).thenReturn(wilderness3);

        allTiles.add(impassable);
        allTiles.add(wilderness1);
        allTiles.add(wilderness2);
        allTiles.add(wilderness3);

        return allTiles;
    }
}
