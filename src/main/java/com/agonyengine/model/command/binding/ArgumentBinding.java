package com.agonyengine.model.command.binding;

import com.agonyengine.model.actor.Actor;

public interface ArgumentBinding {
    boolean bind(Actor actor, String token);
    String getToken();
}
