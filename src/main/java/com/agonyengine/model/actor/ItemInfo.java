package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

@Entity
public class ItemInfo {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    @Convert(converter = WearLocation.Converter.class)
    private EnumSet<WearLocation> wearLocations = EnumSet.noneOf(WearLocation.class);
    private boolean useAllSlots = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EnumSet<WearLocation> getWearLocations() {
        return wearLocations;
    }

    public void setWearLocations(EnumSet<WearLocation> wearLocations) {
        this.wearLocations = wearLocations;
    }

    public boolean isUseAllSlots() {
        return useAllSlots;
    }

    public void setUseAllSlots(boolean useAllSlots) {
        this.useAllSlots = useAllSlots;
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
