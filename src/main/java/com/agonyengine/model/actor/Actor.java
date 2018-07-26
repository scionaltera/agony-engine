package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Actor {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    @ManyToOne
    @Type(type = "pg-uuid")
    private PlayerActorTemplate actorTemplate;

    private String name;

    private String sessionUsername;
    private String sessionId;
    private String remoteIpAddress;
    private Date disconnectedDate = null;

    @ManyToOne
    private GameMap gameMap;
    private Integer x;
    private Integer y;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PlayerActorTemplate getActorTemplate() {
        return actorTemplate;
    }

    public void setActorTemplate(PlayerActorTemplate actorTemplate) {
        this.actorTemplate = actorTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionUsername() {
        return sessionUsername;
    }

    public void setSessionUsername(String sessionUsername) {
        this.sessionUsername = sessionUsername;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public void setRemoteIpAddress(String remoteIpAddress) {
        this.remoteIpAddress = remoteIpAddress;
    }

    public Date getDisconnectedDate() {
        return disconnectedDate;
    }

    public void setDisconnectedDate(Date disconnectedDate) {
        this.disconnectedDate = disconnectedDate;
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
