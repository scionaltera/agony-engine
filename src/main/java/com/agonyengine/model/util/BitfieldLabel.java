package com.agonyengine.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BitfieldLabel {
    private static Map<Integer, BitfieldLabel> labelMap = new HashMap<>();

    private int index;
    private String name;

    public static String toLabels(Bitfield bitfield) {
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < Long.SIZE; i++) {
            if (bitfield.isSet(i) && labelMap.containsKey(i)) {
                labels.add(labelMap.get(i).getName());
            }
        }

        return String.join(", ", labels).trim();
    }

    protected BitfieldLabel(int index, String name) {
        this.index = index;
        this.name = name;

        labelMap.put(this.index, this);
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
