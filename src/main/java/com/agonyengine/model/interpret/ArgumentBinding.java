package com.agonyengine.model.interpret;

import com.agonyengine.model.actor.Actor;

import javax.transaction.Transactional;

public interface ArgumentBinding {
    @Transactional
    boolean bind(Actor actor, String token);
}
