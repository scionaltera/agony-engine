package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.BodyPartCapability;
import com.agonyengine.model.actor.WearLocation;
import com.agonyengine.repository.BodyPartRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static com.agonyengine.model.actor.WearLocation.LEG_LOWER;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.when;

public class BodyGeneratorTest {
    @Mock
    private BodyPartRepository bodyPartRepository;

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

            return iterable;
        });

        bodyGenerator = new BodyGenerator(bodyPartRepository);
    }

    @Test
    public void testGenerate() {
        List<BodyPart> parts = bodyGenerator.generate("humanoid");

        assertTrue(parts
            .stream()
            .anyMatch(part ->
                part.getCapabilities().isSet(BodyPartCapability.WALK.ordinal())
                && part.getWearLocation() == WearLocation.FOOT
                && "left foot".equals(part.getName())
                && LEG_LOWER == part.getConnection().getWearLocation()
            ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMissingTemplate() {
        bodyGenerator.generate("foo");
    }
}
