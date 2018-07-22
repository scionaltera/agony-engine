package com.agonyengine.model.interpret;

import com.agonyengine.model.actor.Actor;

public interface ArgumentBinding {
    boolean bind(Actor actor, String token);
    String getToken();
}
