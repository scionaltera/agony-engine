package com.agonyengine.model.actor;

import com.agonyengine.util.NameUtils;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Actor {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    private String name;

    @ManyToOne
    private Pronoun pronoun;

    @OneToOne
    private Connection connection;

    @ManyToOne
    private GameMap gameMap;
    private Integer x;
    private Integer y;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private GameMap inventory;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String[] getNameTokens() {
        return name.split(" ");
    }

    public String getName() {
        if (connection != null) { // TODO for NPC support add: || npcData != null
            return name;
        }

        return NameUtils.aoran(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pronoun getPronoun() {
        return pronoun;
    }

    public void setPronoun(Pronoun pronoun) {
        this.pronoun = pronoun;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public GameMap getInventory() {
        return inventory;
    }

    public void setInventory(GameMap inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;
        Actor actor = (Actor) o;
        return Objects.equals(getId(), actor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
