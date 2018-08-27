package com.agonyengine.repository;

import com.agonyengine.model.actor.BodyPartGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BodyPartGroupRepository extends JpaRepository<BodyPartGroup, UUID> {
    BodyPartGroup findByName(String name);
}
