package com.agonyengine.repository;

import com.agonyengine.model.actor.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
}
