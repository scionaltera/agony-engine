package com.agonyengine.model.actor;

import com.agonyengine.util.FormattingUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Tile {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;
    private int index;
    private String roomTitle;
    private String roomDescription;

    @ManyToOne
    private Tileset tileset;

    @Convert(converter = TileFlag.Converter.class)
    private EnumSet<TileFlag> flags = EnumSet.noneOf(TileFlag.class);

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getRoomDescription() {
        return FormattingUtils.softWrap(roomDescription);
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public void setTileset(Tileset tileset) {
        this.tileset = tileset;
    }

    public EnumSet<TileFlag> getFlags() {
        return flags;
    }

    public void setFlags(EnumSet<TileFlag> flags) {
        this.flags = flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return id == tile.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
