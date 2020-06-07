package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Floor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Floor Repository for Floor Entity
 */
public interface FloorRepository extends CrudRepository<Floor, String> {
    Optional<Floor> findByBuildingIdAndFloorNumber(String buildingId, int floorNumber);

    List<Floor> findByBuildingId(String buildingId);
}