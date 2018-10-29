package com.agonyengine.model.actor;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class CreatureInfo {
    public static final int BODY_VERSION = 1;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "creature_info_id")
    private List<BodyPart> bodyParts = new ArrayList<>();
    private int bodyVersion;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(List<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public int getBodyVersion() {
        return bodyVersion;
    }

    public void setBodyVersion(int bodyVersion) {
        this.bodyVersion = bodyVersion;
    }

    public boolean hasCapability(BodyPartCapability capability) {
        return bodyParts.stream()
            .anyMatch(part -> part.getCapabilities().contains(capability));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreatureInfo)) return false;
        CreatureInfo that = (CreatureInfo) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
