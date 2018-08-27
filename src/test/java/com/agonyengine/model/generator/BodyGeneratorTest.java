package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.BodyPartCapability;
import com.agonyengine.model.actor.BodyPartGroup;
import com.agonyengine.model.actor.BodyPartTemplate;
import com.agonyengine.model.actor.WearLocation;
import com.agonyengine.repository.BodyPartGroupRepository;
import com.agonyengine.repository.BodyPartRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.agonyengine.model.actor.WearLocation.FOOT;
import static com.agonyengine.model.actor.WearLocation.LEG_LOWER;
import static com.agonyengine.model.actor.WearLocation.LEG_UPPER;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class BodyGeneratorTest {
    @Mock
    private BodyPartRepository bodyPartRepository;

    @Mock
    private BodyPartGroupRepository bodyPartGroupRepository;

    private BodyGenerator bodyGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(bodyPartRepository.save(any(BodyPart.class))).thenAnswer(i -> {
            BodyPart part = i.getArgument(0);

            if (part.getId() == null) {
                part.setId(UUID.randomUUID());
            }

            return part;
        });

        when(bodyPartRepository.saveAll(anyIterable())).thenAnswer(i -> {
            Iterable<BodyPart> iterable = i.getArgument(0);

            iterable.forEach(part -> {
                if (part.getId() == null) {
                    part.setId(UUID.randomUUID());
                }
            });

            List<BodyPart> result = new ArrayList<>();

            iterable.forEach(result::add);

            return result;
        });

        bodyGenerator = new BodyGenerator(bodyPartRepository, bodyPartGroupRepository);
    }

    @Test
    public void testGenerate() {
        BodyPartGroup bodyPartGroup = new BodyPartGroup();
        BodyPartTemplate leftUpperLeg = new BodyPartTemplate();
        BodyPartTemplate leftLowerLeg = new BodyPartTemplate();
        BodyPartTemplate leftFoot = new BodyPartTemplate();

        leftUpperLeg.setId(UUID.randomUUID());
        leftUpperLeg.setName("left upper leg");
        leftUpperLeg.setWearLocation(LEG_UPPER);

        leftLowerLeg.setId(UUID.randomUUID());
        leftLowerLeg.setName("left lower leg");
        leftLowerLeg.setWearLocation(LEG_LOWER);
        leftLowerLeg.setConnection("NAME:left upper leg");

        leftFoot.setId(UUID.randomUUID());
        leftFoot.setName("left foot");
        leftFoot.setWearLocation(FOOT);
        leftFoot.setCapabilities("WALK");
        leftFoot.setConnection("WL:LEG_LOWER");

        bodyPartGroup.getBodyPartTemplates().add(leftUpperLeg);
        bodyPartGroup.getBodyPartTemplates().add(leftLowerLeg);
        bodyPartGroup.getBodyPartTemplates().add(leftFoot);

        when(bodyPartGroupRepository.findByName(eq("human"))).thenReturn(bodyPartGroup);

        List<BodyPart> parts = bodyGenerator.generate("human");

        assertTrue(parts
            .stream()
            .anyMatch(part ->
                part.getCapabilities().isSet(BodyPartCapability.WALK.ordinal())
                && part.getWearLocation() == WearLocation.FOOT
                && "left foot".equals(part.getName())
                && LEG_LOWER == part.getConnection().getWearLocation()
            ));

        assertTrue(parts
            .stream()
            .anyMatch(part ->
                part.getWearLocation() == WearLocation.LEG_LOWER
                    && "left lower leg".equals(part.getName())
                    && LEG_UPPER == part.getConnection().getWearLocation()
            ));
    }

    @Test
    public void testGenerateUnsupportedConnectionMarkup() {
        BodyPartGroup bodyPartGroup = new BodyPartGroup();
        BodyPartTemplate leftUpperLeg = new BodyPartTemplate();
        BodyPartTemplate leftLowerLeg = new BodyPartTemplate();

        leftUpperLeg.setId(UUID.randomUUID());
        leftUpperLeg.setName("left upper leg");
        leftUpperLeg.setWearLocation(LEG_UPPER);

        leftLowerLeg.setId(UUID.randomUUID());
        leftLowerLeg.setName("left lower leg");
        leftLowerLeg.setWearLocation(LEG_LOWER);
        leftLowerLeg.setConnection("FOO:bar");

        bodyPartGroup.getBodyPartTemplates().add(leftUpperLeg);
        bodyPartGroup.getBodyPartTemplates().add(leftLowerLeg);

        when(bodyPartGroupRepository.findByName(eq("human"))).thenReturn(bodyPartGroup);

        List<BodyPart> parts = bodyGenerator.generate("human");

        assertTrue(parts
            .stream()
            .anyMatch(part ->
                part.getWearLocation() == WearLocation.LEG_LOWER
                    && "left lower leg".equals(part.getName())
                    && null == part.getConnection()
            ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMissingTemplate() {
        bodyGenerator.generate("foo");
    }
}
