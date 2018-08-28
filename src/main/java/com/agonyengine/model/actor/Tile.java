package com.agonyengine.model.actor;

import com.agonyengine.model.util.Bitfield;
import com.agonyengine.util.FormattingUtils;
import org.hibernate.annotations.Type;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    @Embedded
    @AttributeOverride(name = "bits", column = @Column(name = "flags"))
    private Bitfield flags;

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

    public Bitfield getFlags() {
        return flags;
    }

    public void setFlags(Bitfield flags) {
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
