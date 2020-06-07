package com.coms309SS6.isuview.controllers;

import com.coms309SS6.isuview.models.*;
import com.coms309SS6.isuview.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Object controller to allow us to see the current status and data in our JPA databases.
 */
@RestController
public class ObjectController {
    @Autowired
    BuildingRepository buildingRepository;

    @Autowired
    FloorRepository floorRepository;

    @Autowired
    IntersectionRepository intersectionRepository;

    @Autowired
    EdgeRepository edgeRepository;

    @Autowired
    RoomRepository roomRepository;

    /**
     * Get mapping to get all edges in the database.
     *
     * @return All edges in DB
     */
    @GetMapping("/api/objects/edges")
    public Iterable<Edge> getEdges() {
        return edgeRepository.findAll();
    }

    /**
     * Get mapping to get all rooms in the database.
     *
     * @return All rooms in DB
     */
    @GetMapping("/api/objects/rooms")
    public Iterable<Room> getRooms() {
        return roomRepository.findAll();
    }

    /**
     * Get mapping to get all intersection in the database.
     *
     * @return All intersections in DB
     */
    @GetMapping("/api/objects/intersections")
    public Iterable<Intersection> getIntersections() {
        return intersectionRepository.findAll();
    }

    /**
     * Get mapping to get all buildings in the database.
     *
     * @return All buildings in DB
     */
    @GetMapping("/api/objects/buildings")
    public Iterable<Building> getBuildings() {
        return buildingRepository.findAll();
    }

    /**
     * Get mapping to get all floors in the database.
     *
     * @return All floors in DB
     */
    @GetMapping("/api/objects/floors")
    public Iterable<Floor> getFloors() {
        return floorRepository.findAll();
    }

    /**
     * Put mapping to completely reset the database.
     * <p>
     * Must be called with a JSON Map that maps "key" to "delete me please!"
     * <p>
     * Only call this if you're absolutely sure you need to clear the DB.
     *
     * @return The buildings in the database
     */
    @PutMapping("/api/objects/resetDatabase")
    public Iterable<Building> resetDatabase(@RequestBody Map<String, String> key) {
        if ("delete me please!".equals(key.get("key"))) {
            edgeRepository.deleteAll();
            roomRepository.deleteAll();
            intersectionRepository.deleteAll();
            floorRepository.deleteAll();
            buildingRepository.deleteAll();
        }
        return buildingRepository.findAll();
    }
}
