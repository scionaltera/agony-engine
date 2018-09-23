package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

@Entity
public class BodyPart {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    private String name;

    @Convert(converter = BodyPartCapability.Converter.class)
    private EnumSet<BodyPartCapability> capabilities = EnumSet.noneOf(BodyPartCapability.class);

    @Enumerated(EnumType.STRING)
    private WearLocation wearLocation;

    @OneToOne(fetch = FetchType.LAZY)
    private BodyPart connection;

    @ManyToOne
    private Actor armor;

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

    public EnumSet<BodyPartCapability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(EnumSet<BodyPartCapability> capabilities) {
        this.capabilities = capabilities;
    }

    public WearLocation getWearLocation() {
        return wearLocation;
    }

    public void setWearLocation(WearLocation wearLocation) {
        this.wearLocation = wearLocation;
    }

    public BodyPart getConnection() {
        return connection;
    }

    public void setConnection(BodyPart connection) {
        this.connection = connection;
    }

    public Actor getArmor() {
        return armor;
    }

    public void setArmor(Actor armor) {
        this.armor = armor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BodyPart)) return false;
        BodyPart bodyPart = (BodyPart) o;
        return Objects.equals(id, bodyPart.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
