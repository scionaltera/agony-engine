package com.agonyengine.repository;

import com.agonyengine.model.command.binding.Verb;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerbRepository extends JpaRepository<Verb, String> {
    Optional<Verb> findFirstByNameIgnoreCaseStartingWith(Sort sort, String name);
}
