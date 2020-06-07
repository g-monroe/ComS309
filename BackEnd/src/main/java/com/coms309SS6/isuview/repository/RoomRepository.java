package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Room Repository for Room Entity
 */
public interface RoomRepository extends CrudRepository<Room, String> {
    List<Room> findByFloorId(String floorId);

    List<Room> findByRoomNumber(String roomNumber);

    Optional<Room> findByFloorIdAndRoomNumber(String floorId, String roomNumber);

    Optional<Room> findByIntersectionId(String intersectionId);
}