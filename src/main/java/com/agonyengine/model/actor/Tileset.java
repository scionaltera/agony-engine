package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Tileset {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "tileset")
    @MapKey(name = "index")
    private Map<Integer, Tile> tiles = new HashMap<>();

    @Convert(converter = TilesetFlag.Converter.class)
    private EnumSet<TilesetFlag> flags = EnumSet.noneOf(TilesetFlag.class);

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tile getTile(int index) {
        return tiles.get(index);
    }

    public void setTile(Tile tile) {
        tiles.put(tile.getIndex(), tile);
    }

    public EnumSet<TilesetFlag> getFlags() {
        return flags;
    }

    public void setFlags(EnumSet<TilesetFlag> flags) {
        this.flags = flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tileset)) return false;
        Tileset tileset = (Tileset) o;
        return Objects.equals(getId(), tileset.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
