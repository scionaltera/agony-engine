package com.agonyengine.model.actor;

import com.agonyengine.model.util.Bitfield;
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
public class ItemInfo {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    @Embedded
    @AttributeOverride(name = "bits", column = @Column(name = "wear_locations"))
    private Bitfield wearLocations;
    private boolean useAllSlots = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Bitfield getWearLocations() {
        return wearLocations;
    }

    public void setWearLocations(Bitfield wearLocations) {
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
