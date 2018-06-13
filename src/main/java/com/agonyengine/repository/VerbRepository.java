package com.agonyengine.repository;

import com.agonyengine.model.interpret.Verb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerbRepository extends JpaRepository<Verb, String> {
}
