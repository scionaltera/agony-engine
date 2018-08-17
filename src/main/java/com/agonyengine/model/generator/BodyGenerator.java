package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.BodyPartCapability;
import com.agonyengine.model.util.Bitfield;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BodyGenerator {
    public static final String HUMANOID_TEMPLATE = "humanoid";
    public static final String ANIMAL_TEMPLATE = "animal";

    private static final Map<String, Map<String, Bitfield>> templates = new HashMap<>();

    private static Map<String, Bitfield> humanoid() {
        Map<String, Bitfield> template = new HashMap<>();

        template.put("head", new Bitfield());
        template.put("torso", new Bitfield());
        template.put("left arm", new Bitfield());
        template.put("right arm", new Bitfield());
        template.put("left leg", new Bitfield(BodyPartCapability.CAN_WALK));
        template.put("right leg", new Bitfield(BodyPartCapability.CAN_WALK));

        return template;
    }

    private static Map<String, Bitfield> animal() {
        Map<String, Bitfield> template = new HashMap<>();

        template.put("head", new Bitfield());
        template.put("torso", new Bitfield());
        template.put("front left leg", new Bitfield(BodyPartCapability.CAN_WALK));
        template.put("front right leg", new Bitfield(BodyPartCapability.CAN_WALK));
        template.put("back left leg", new Bitfield(BodyPartCapability.CAN_WALK));
        template.put("back right leg", new Bitfield(BodyPartCapability.CAN_WALK));
        template.put("tail", new Bitfield());

        return template;
    }

    static {
        templates.put("humanoid", humanoid());
        templates.put("animal", animal());
    }

    public List<BodyPart> generate(String templateName) {
        Map<String, Bitfield> template = templates.get(templateName);

        if (template == null) {
            throw new IllegalArgumentException("No such template!");
        }

        List<BodyPart> bodyParts = new ArrayList<>();

        template.keySet().forEach(k -> {
            BodyPart part = new BodyPart();

            part.setName(k);
            part.setCapabilities(template.get(k));

            bodyParts.add(part);
        });

        return bodyParts;
    }
}
