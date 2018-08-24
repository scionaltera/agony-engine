package com.agonyengine.repository;

import com.agonyengine.model.actor.BodyPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BodyPartRepository extends JpaRepository<BodyPart, UUID> {
}
