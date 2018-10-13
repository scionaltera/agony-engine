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
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.agonyengine.model.actor.GameMap.CURRENT_MAP_VERSION;
import static com.agonyengine.model.actor.GameMap.NO_UPDATE_VERSION;
import static com.agonyengine.model.actor.TileFlag.IMPASSABLE;
import static com.agonyengine.model.actor.TileFlag.WILDERNESS;
import static com.agonyengine.model.generator.MapGenerator.MAP_MIN_WIDTH;
import static org.junit.Assert.*;
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

    private List<Tile> wilderness;
    private Tile impassable;

    private MapGenerator mapGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        List<Tile> fakeTiles = generateTiles();
        wilderness = fakeTiles.stream()
            .filter(tile -> tile.getFlags().contains(WILDERNESS))
            .collect(Collectors.toList());
        impassable = fakeTiles.stream()
            .filter(tile -> tile.getFlags().contains(IMPASSABLE))
            .collect(Collectors.toList())
        .get(0);

        when(gameMapRepository.save(any(GameMap.class))).thenAnswer(i -> {
            GameMap map = i.getArgument(0);

            map.setId(UUID.randomUUID());

            return map;
        });

        when(tileRepository.findByTileset(any(Tileset.class))).thenReturn(fakeTiles);

        mapGenerator = new MapGenerator(gameMapRepository, tileRepository);
    }

    @Test
    public void testGenerateMap() {
        GameMap map = mapGenerator.generateMap(tileset);

        assertNotNull(map.getId());
        assertEquals(CURRENT_MAP_VERSION, map.getVersion());

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getWidth(); y++) {
                assertNotNull(map.getTile(x, y));
            }
        }
    }

    @Test
    public void testSquareOverlapsMapBoundary() {
        GameMap map = mapGenerator.createBlankMap(tileset);

        mapGenerator.drawSquare(map, impassable, 0, 0, 2);
    }

    @Test
    public void testCircleOverlapsMapBoundary() {
        GameMap map = mapGenerator.createBlankMap(tileset);

        mapGenerator.drawCircle(map, impassable, 0, 0, 2);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testUpdateMapWillNotUpdateCurrentVersion() {
        GameMap map = spy(mapGenerator.createBlankMap(tileset));

        map.setVersion(CURRENT_MAP_VERSION);

        GameMap savedMap = mapGenerator.updateMap(map);

        assertEquals(map, savedMap);
        assertEquals(CURRENT_MAP_VERSION, map.getVersion());
        verify(map, never()).setTile(anyInt(), anyInt(), anyByte());
        verify(gameMapRepository, never()).save(any(GameMap.class));
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testUpdateMapWillNotUpdateNoUpdateVersion() {
        GameMap map = spy(mapGenerator.createBlankMap(tileset));

        map.setVersion(NO_UPDATE_VERSION);

        GameMap savedMap = mapGenerator.updateMap(map);

        assertEquals(map, savedMap);
        assertEquals(NO_UPDATE_VERSION, map.getVersion());
        verify(map, never()).setTile(anyInt(), anyInt(), anyByte());
        verify(gameMapRepository, never()).save(any(GameMap.class));
    }

    @Test
    public void testUpdateMap() {
        GameMap map = spy(mapGenerator.createBlankMap(tileset));

        map.setVersion(CURRENT_MAP_VERSION - 1);

        GameMap savedMap = mapGenerator.updateMap(map);

        assertEquals(map, savedMap);
        assertEquals(CURRENT_MAP_VERSION, map.getVersion());
        verify(gameMapRepository).save(eq(map));
    }

    @Test
    public void testFloodFillTouchesAllTiles() {
        GameMap map = mapGenerator.createBlankMap(tileset);

        mapGenerator.floodFill(map, wilderness);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getWidth(); y++) {
                assertTrue(map.getTile(x, y).getIndex() != 0);
            }
        }
    }

    @Test
    public void testFloodFillIsWithinBounds() {
        final Random mockRandom = mock(Random.class);
        final int mapAdditionalWidth = 7;

        when(mockRandom.nextInt(anyInt())).thenReturn(mapAdditionalWidth);

        MapGenerator mapGenerator = new MapGenerator(gameMapRepository, tileRepository, mockRandom);
        GameMap map = mapGenerator.createBlankMap(tileset);

        assertEquals(MAP_MIN_WIDTH + mapAdditionalWidth, map.getWidth());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testFloodFillTileShuffle() {
        GameMap map = mapGenerator.createBlankMap(tileset);

        mapGenerator.floodFill(map, wilderness);

        wilderness.forEach(tile -> verify(tile, atLeastOnce()).getIndex());
    }

    @Test
    public void testSquare() {
        GameMap map = mapGenerator.createBlankMap(tileset);

        mapGenerator.drawSquare(map, impassable, 5, 5, 2);

        int count = 0;

        for (int x = 3; x <= 7; x++) {
            for (int y = 3; y <= 7; y++) {
                assertTrue(map.getTile(x, y).getFlags().contains(IMPASSABLE));
                count++;
            }
        }

        assertEquals(25, count);
    }

    @Test
    public void testCircle() {
        GameMap map = mapGenerator.createBlankMap(tileset);

        mapGenerator.floodFill(map, wilderness);
        mapGenerator.drawCircle(map, impassable, 5, 5, 2);

        int count = 0;

        for (int x = 3; x <= 7; x++) {
            for (int y = 3; y <= 7; y++) {
                if (mapGenerator.distance(5, 5, x, y) <= 2) {
                    assertTrue(map.getTile(x, y).getFlags().contains(IMPASSABLE));
                    count++;
                } else {
                    assertFalse(map.getTile(x, y).getFlags().contains(IMPASSABLE));
                }
            }
        }

        assertEquals(13, count);
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
