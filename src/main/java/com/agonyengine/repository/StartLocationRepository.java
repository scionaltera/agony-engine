package com.agonyengine.repository;

import com.agonyengine.model.map.StartLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StartLocationRepository extends JpaRepository<StartLocation, UUID> {
}
