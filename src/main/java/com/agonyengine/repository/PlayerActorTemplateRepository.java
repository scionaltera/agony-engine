package com.agonyengine.repository;

import com.agonyengine.model.actor.PlayerActorTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlayerActorTemplateRepository extends JpaRepository<PlayerActorTemplate, UUID> {
    List<PlayerActorTemplate> findByAccount(String account);
}
