package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.WearLocation;
import com.agonyengine.model.util.Bitfield;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.agonyengine.model.actor.BodyPartCapability.HOLD;
import static com.agonyengine.model.actor.BodyPartCapability.SPEAK;
import static com.agonyengine.model.actor.BodyPartCapability.WALK;
import static com.agonyengine.model.actor.WearLocation.*;

@Component
public class BodyGenerator {
    public static final String HUMANOID_TEMPLATE = "humanoid";

    private static final Map<String, Map<String, BodyTemplate>> templates = new HashMap<>();

    private static class BodyTemplate {
        private Bitfield capabilities;
        private WearLocation wearLocation;

        public BodyTemplate(Bitfield capabilities, WearLocation wearLocation) {
            this.capabilities = capabilities;
            this.wearLocation = wearLocation;
        }

        public Bitfield getCapabilities() {
            return capabilities;
        }

        public WearLocation getWearLocation() {
            return wearLocation;
        }
    }

    // TODO need to define templates in configuration and load them in here rather than hard coded
    private static Map<String, BodyTemplate> humanoid() {
        Map<String, BodyTemplate> template = new HashMap<>();

        template.put("head", new BodyTemplate(new Bitfield(), HEAD));
        template.put("left ear", new BodyTemplate(new Bitfield(), EAR));
        template.put("right ear", new BodyTemplate(new Bitfield(), EAR));
        template.put("left eye", new BodyTemplate(new Bitfield(), EYE));
        template.put("right eye", new BodyTemplate(new Bitfield(), EYE));
        template.put("nose", new BodyTemplate(new Bitfield(), NOSE));
        template.put("mouth", new BodyTemplate(new Bitfield(), null));
        template.put("teeth", new BodyTemplate(new Bitfield(), null));
        template.put("tongue", new BodyTemplate(new Bitfield(SPEAK), TONGUE));
        template.put("neck", new BodyTemplate(new Bitfield(), NECK));
        template.put("body", new BodyTemplate(new Bitfield(), BODY));
        template.put("left arm", new BodyTemplate(new Bitfield(), ARM));
        template.put("right arm", new BodyTemplate(new Bitfield(), ARM));
        template.put("left hand", new BodyTemplate(new Bitfield(HOLD), HAND));
        template.put("right hand", new BodyTemplate(new Bitfield(HOLD), HAND));
        template.put("left finger", new BodyTemplate(new Bitfield(), FINGER));
        template.put("right finger", new BodyTemplate(new Bitfield(), FINGER));
        template.put("left leg", new BodyTemplate(new Bitfield(WALK), LEG));
        template.put("right leg", new BodyTemplate(new Bitfield(WALK), LEG));
        template.put("left foot", new BodyTemplate(new Bitfield(), FOOT));
        template.put("right foot", new BodyTemplate(new Bitfield(), FOOT));

        return template;
    }

    static {
        templates.put("humanoid", humanoid());
    }

    public List<BodyPart> generate(String templateName) {
        Map<String, BodyTemplate> template = templates.get(templateName);

        if (template == null) {
            throw new IllegalArgumentException("No such template!");
        }

        List<BodyPart> bodyParts = new ArrayList<>();

        template.keySet().forEach(k -> {
            BodyPart part = new BodyPart();

            part.setName(k);
            part.setCapabilities(template.get(k).getCapabilities());
            part.setWearLocation(template.get(k).getWearLocation());

            bodyParts.add(part);
        });

        return bodyParts;
    }
}
