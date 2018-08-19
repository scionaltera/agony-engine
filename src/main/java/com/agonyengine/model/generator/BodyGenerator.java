package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.util.Bitfield;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.agonyengine.model.actor.BodyPartCapability.HOLD;
import static com.agonyengine.model.actor.BodyPartCapability.SPEAK;
import static com.agonyengine.model.actor.BodyPartCapability.WALK;

@Component
public class BodyGenerator {
    public static final String HUMANOID_TEMPLATE = "humanoid";

    private static final Map<String, Map<String, Bitfield>> templates = new HashMap<>();

    // TODO need to define templates in configuration and load them in here rather than hard coded
    private static Map<String, Bitfield> humanoid() {
        Map<String, Bitfield> template = new HashMap<>();

        template.put("head", new Bitfield());
        template.put("left ear", new Bitfield());
        template.put("right ear", new Bitfield());
        template.put("left eye", new Bitfield());
        template.put("right eye", new Bitfield());
        template.put("nose", new Bitfield());
        template.put("mouth", new Bitfield());
        template.put("teeth", new Bitfield());
        template.put("tongue", new Bitfield(SPEAK));
        template.put("neck", new Bitfield());
        template.put("torso", new Bitfield());
        template.put("body", new Bitfield());
        template.put("left arm", new Bitfield());
        template.put("right arm", new Bitfield());
        template.put("left hand", new Bitfield(HOLD));
        template.put("right hand", new Bitfield(HOLD));
        template.put("left finger", new Bitfield());
        template.put("right finger", new Bitfield());
        template.put("left leg", new Bitfield(WALK));
        template.put("right leg", new Bitfield(WALK));
        template.put("left foot", new Bitfield());
        template.put("right foot", new Bitfield());

        return template;
    }

    static {
        templates.put("humanoid", humanoid());
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
