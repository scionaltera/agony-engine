package com.agonyengine.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BitfieldLabel {
    private static Map<Integer, BitfieldLabel> labelMap = new HashMap<>();

    private int index;
    private String name;

    public static String toLabels(Bitfield bitfield) {
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < Long.SIZE; i++) {
            if (bitfield.isSet(i)) {
                if (labelMap.get(i) != null) {
                    labels.add(labelMap.get(i).getName());
                }
            }
        }

        return labels.stream().collect(Collectors.joining(", ")).trim();
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
