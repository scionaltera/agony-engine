package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPartCapability;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.util.Bitfield;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScoreCommand {
    @Transactional
    public void invoke(Actor actor, GameOutput output) {
        output.append("[dcyan][ [cyan]Your Score [dcyan]]");
        output.append(String.format("[cyan]Name: [dcyan]%s", actor.getName()));
        output.append(String.format("[cyan]Pronouns: [dcyan]%s/%s", actor.getPronoun().getSubject(), actor.getPronoun().getObject()));

        if (actor.getCreatureInfo() != null) {
            output.append("[cyan]Body Parts:");
            actor.getCreatureInfo().getBodyParts().forEach(part -> output.append(String.format("&nbsp;&nbsp;[dcyan]%s [cyan]- [dcyan]%s",
                part.getName(),
                capabilitiesList(part.getCapabilities()))));
        }
    }

    private String capabilitiesList(Bitfield bitfield) {
        List<String> capabilities = new ArrayList<>();

        for (int i = 0; i < Long.SIZE; i++) {
            if (bitfield.isSet(i)) {
                capabilities.add(BodyPartCapability.forIndex(i).getName());
            }
        }

        return capabilities.stream().collect(Collectors.joining(", "));
    }
}
