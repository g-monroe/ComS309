package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Building;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Building Repository for Building Entity
 */
public interface BuildingRepository extends CrudRepository<Building, String> {
    Optional<Building> findByBuildingName(String buildingName);
}