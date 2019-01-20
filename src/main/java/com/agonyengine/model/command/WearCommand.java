package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.WearLocation;
import com.agonyengine.model.interpret.ActorInventory;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WearCommand {
    private ActorRepository actorRepository;
    private CommService commService;

    @Inject
    public WearCommand(ActorRepository actorRepository, CommService commService) {
        this.actorRepository = actorRepository;
        this.commService = commService;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, ActorInventory itemBinding) {
        Actor item = itemBinding.getTarget();

        if (actor.getCreatureInfo() == null) { // TODO make ArgumentBinding for this
            output.append("[default]Only creatures can wear equipment.");
            return;
        }

        if (item.getItemInfo() == null) { // TODO make ArgumentBinding for this
            output.append("[default]That's not something you can wear.");
            return;
        }

        List<BodyPart> wearLocations = new ArrayList<>();

        for (WearLocation wearLocation : WearLocation.values()) {
            if (item.getItemInfo().getWearLocations().contains(wearLocation)) {
                List<BodyPart> parts = actor.getCreatureInfo().getBodyParts().stream()
                    .filter(part -> part.getArmor() == null)
                    .filter(part -> part.getWearLocation() != null)
                    .filter(part -> part.getWearLocation().ordinal() == wearLocation.getIndex())
                    .collect(Collectors.toList());

                if (parts.isEmpty()) {
                    output.append("You need to remove some other equipment first.");
                    return;
                }

                if (item.getItemInfo().isUseAllSlots()) {
                    wearLocations.addAll(parts);
                } else {
                    wearLocations.add(parts.get(0));
                }
            }
        }

        if (wearLocations.isEmpty()) {
            output.append("That doesn't seem like you can wear it.");
            return;
        }

        wearLocations.forEach(part -> part.setArmor(item));
        item.setGameMap(null);

        actorRepository.save(actor);
        actorRepository.save(item);

        output.append(String.format("[default]You wear %s[default].", item.getName()));
        commService.echo(item, new GameOutput(String.format("[default]%s wears you.", actor.getName())));
        commService.echoToRoom(
            actor,
            new GameOutput(String.format("[default]%s wears %s[default].",
                actor.getName(),
                item.getName())),
            actor, item);
    }
}
