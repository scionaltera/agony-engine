package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.BodyPartCapability;
import com.agonyengine.model.actor.BodyPartGroup;
import com.agonyengine.model.actor.BodyPartTemplate;
import com.agonyengine.model.util.Bitfield;
import com.agonyengine.repository.BodyPartGroupRepository;
import com.agonyengine.repository.BodyPartRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BodyGenerator {
    public static final String HUMAN_TEMPLATE = "humanoid";
    public static final String HUMAN_MOUTH = "human_mouth";
    public static final String TWO_EYES = "two_eyes";
    public static final String TWO_EARS = "two_ears";
    public static final String TWO_ARMS = "two_arms";
    public static final String TWO_LEGS = "two_legs";

    private BodyPartRepository bodyPartRepository;
    private BodyPartGroupRepository bodyPartGroupRepository;

    @Inject
    public BodyGenerator(
        BodyPartRepository bodyPartRepository,
        BodyPartGroupRepository bodyPartGroupRepository) {

        this.bodyPartRepository = bodyPartRepository;
        this.bodyPartGroupRepository = bodyPartGroupRepository;
    }

    @Transactional
    public List<BodyPart> generate(String ... templateNames) {
        Map<BodyPartTemplate, BodyPart> bodyParts = new HashMap<>();

        Arrays.stream(templateNames).forEach(templateName -> bodyParts.putAll(generate(templateName)));

        // link them together
        bodyParts.forEach((template, part) -> {
            if (template.getConnection() != null && part.getConnection() == null) {
                bodyParts.values().stream()
                    .filter(p -> p != part)
                    .filter(p -> {
                        if (template.getConnection().startsWith("WL:")) {
                            return p.getWearLocation() != null && template.getConnection().substring(3).equals(p.getWearLocation().name());
                        } else if (template.getConnection().startsWith("NAME:")) {
                            return template.getConnection().substring(5).equals(p.getName());
                        }

                        return false;
                    })
                    .findFirst()
                    .ifPresent(part::setConnection);
            }
        });

        return bodyPartRepository.saveAll(bodyParts.values());
    }

    private Map<BodyPartTemplate, BodyPart> generate(String templateName) {
        BodyPartGroup group = bodyPartGroupRepository.findByName(templateName);

        if (group == null) {
            throw new IllegalArgumentException("No such body part group: " + templateName);
        }

        Map<BodyPartTemplate, BodyPart> bodyParts = new HashMap<>();
        List<BodyPartTemplate> bodyPartTemplates = group.getBodyPartTemplates();

        bodyPartTemplates.forEach(template -> {
            BodyPart part = new BodyPart();
            Bitfield capabilities = new Bitfield();

            if (!StringUtils.isEmpty(template.getCapabilities())) {
                Arrays.stream(template.getCapabilities().split(",")).forEach(cap -> capabilities.set(BodyPartCapability.valueOf(cap).ordinal()));
            }

            part.setName(template.getName());
            part.setCapabilities(capabilities);
            part.setWearLocation(template.getWearLocation());

            bodyParts.put(template, part);
        });

        return bodyParts;
    }
}
