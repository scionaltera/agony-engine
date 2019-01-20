package com.agonyengine.model.map;

import com.agonyengine.model.util.Location;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Deprecated
@Entity
@Table(name = "map_exit")
public class Exit {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String direction;

    @Embedded
    @AssociationOverride(name = "gameMap", joinColumns = @JoinColumn(name = "location_map_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "gameMap", column = @Column(name = "location_map_id")),
        @AttributeOverride(name = "x", column = @Column(name = "location_x")),
        @AttributeOverride(name = "y", column = @Column(name = "location_y"))
    })
    private Location location;

    @Embedded
    @AssociationOverride(name = "gameMap", joinColumns = @JoinColumn(name = "destination_map_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "gameMap", column = @Column(name = "destination_map_id")),
        @AttributeOverride(name = "x", column = @Column(name = "destination_x")),
        @AttributeOverride(name = "y", column = @Column(name = "destination_y"))
    })
    private Location destination;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exit)) return false;
        Exit exit = (Exit) o;
        return Objects.equals(id, exit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
