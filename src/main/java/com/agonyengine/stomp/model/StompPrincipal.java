package com.agonyengine.stomp.model;

import java.security.Principal;

/**
 * Borrowed from:
 * https://stackoverflow.com/questions/37853727/where-user-comes-from-in-convertandsendtouser-works-in-sockjsspring-websocket?rq=1
 */
public class StompPrincipal implements Principal {
    private String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
