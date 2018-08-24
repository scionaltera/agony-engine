package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.WearLocation;
import com.agonyengine.model.util.Bitfield;
import com.agonyengine.repository.BodyPartRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
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
        private String connection;

        public BodyTemplate(Bitfield capabilities, WearLocation wearLocation, String connection) {
            this.capabilities = capabilities;
            this.wearLocation = wearLocation;
            this.connection = connection;
        }

        public Bitfield getCapabilities() {
            return capabilities;
        }

        public WearLocation getWearLocation() {
            return wearLocation;
        }

        public String getConnection() {
            return connection;
        }
    }

    // TODO need to define templates in configuration and load them in here rather than hard coded
    private static Map<String, BodyTemplate> humanoid() {
        Map<String, BodyTemplate> template = new HashMap<>();

        template.put("upper body", new BodyTemplate(new Bitfield(), BODY_UPPER, null));
        template.put("lower body", new BodyTemplate(new Bitfield(), BODY_LOWER, "upper body"));
        template.put("neck", new BodyTemplate(new Bitfield(), NECK, "upper body"));
        template.put("head", new BodyTemplate(new Bitfield(SPEAK), HEAD, "neck"));
        template.put("right upper arm", new BodyTemplate(new Bitfield(), ARM_UPPER, "upper body"));
        template.put("left upper arm", new BodyTemplate(new Bitfield(), ARM_UPPER, "upper body"));
        template.put("right lower arm", new BodyTemplate(new Bitfield(), ARM_LOWER, "right upper arm"));
        template.put("left lower arm", new BodyTemplate(new Bitfield(), ARM_LOWER, "left upper arm"));
        template.put("right wrist", new BodyTemplate(new Bitfield(), WRIST, "right lower arm"));
        template.put("left wrist", new BodyTemplate(new Bitfield(), WRIST, "left lower arm"));
        template.put("right hand", new BodyTemplate(new Bitfield(HOLD), HAND, "right wrist"));
        template.put("left hand", new BodyTemplate(new Bitfield(HOLD), HAND, "left wrist"));
        template.put("right upper leg", new BodyTemplate(new Bitfield(), LEG_UPPER, "lower body"));
        template.put("left upper leg", new BodyTemplate(new Bitfield(), LEG_UPPER, "lower body"));
        template.put("right lower leg", new BodyTemplate(new Bitfield(), LEG_LOWER, "right upper leg"));
        template.put("left lower leg", new BodyTemplate(new Bitfield(), LEG_LOWER, "left upper leg"));
        template.put("right foot", new BodyTemplate(new Bitfield(WALK), FOOT, "right lower leg"));
        template.put("left foot", new BodyTemplate(new Bitfield(WALK), FOOT, "left lower leg"));

        return template;
    }

    static {
        templates.put("humanoid", humanoid());
    }

    private BodyPartRepository bodyPartRepository;

    @Inject
    public BodyGenerator(BodyPartRepository bodyPartRepository) {
        this.bodyPartRepository = bodyPartRepository;
    }

    @Transactional
    public List<BodyPart> generate(String templateName) {
        Map<String, BodyTemplate> template = templates.get(templateName);

        if (template == null) {
            throw new IllegalArgumentException("No such template!");
        }

        List<BodyPart> bodyParts = new ArrayList<>();
        Map<BodyTemplate, BodyPart> partMap = new HashMap<>();

        // build the parts
        template.keySet().forEach(k -> {
            BodyPart part = new BodyPart();

            part.setName(k);
            part.setCapabilities(template.get(k).getCapabilities());
            part.setWearLocation(template.get(k).getWearLocation());

            part = bodyPartRepository.save(part);

            bodyParts.add(part);
            partMap.put(template.get(k), part);
        });

        // link them together
        partMap.forEach((t, part) -> {
            if (t.getConnection() != null && part.getConnection() == null) {
                bodyParts.stream()
                    .filter(p -> p != part)
                    .filter(p -> t.getConnection().equals(p.getName()))
                    .findFirst()
                    .ifPresent(part::setConnection); // orElseThrow?
            }
        });

        return bodyPartRepository.saveAll(bodyParts);
    }
}
