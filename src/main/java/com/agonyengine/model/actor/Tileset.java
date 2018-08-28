package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
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

    @OneToMany
    @JoinColumn(name = "tileset")
    @MapKey(name = "index")
    private Map<Integer, Tile> tiles = new HashMap<>();

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
