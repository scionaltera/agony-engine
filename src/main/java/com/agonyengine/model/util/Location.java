package com.agonyengine.model.util;

import com.agonyengine.model.actor.GameMap;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Deprecated
@Embeddable
public class Location {
    @ManyToOne
    private GameMap gameMap;
    private Integer x;
    private Integer y;

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
}
