package com.agonyengine.stomp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameOutput {
    private List<String> output = new ArrayList<>();

    public GameOutput() {
        // this method intentionally left blank
    }

    public GameOutput(String... messages) {
        Arrays.stream(messages).forEach(this::append);
    }

    public GameOutput append(String message) {
        output.add(message);

        return this;
    }

    public List<String> getOutput() {
        return output;
    }
}
