package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class ItemInfo {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemInfo)) return false;
        ItemInfo itemInfo = (ItemInfo) o;
        return Objects.equals(id, itemInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
