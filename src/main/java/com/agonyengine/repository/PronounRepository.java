package com.agonyengine.repository;

import com.agonyengine.model.actor.Pronoun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PronounRepository extends JpaRepository<Pronoun, String> {
}
