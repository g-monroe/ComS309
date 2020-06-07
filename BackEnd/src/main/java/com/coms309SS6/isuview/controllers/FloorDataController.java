package com.coms309SS6.isuview.controllers;

import com.coms309SS6.isuview.models.*;
import com.coms309SS6.isuview.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller to handle storing data from the mapping software into the JPA database.
 */
@RestController
public class FloorDataController {

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
     * Put mapping on /api/floordata to accept data in the format specified in the ISU Floor Mapper application into our JPA persistence layer.
     * <p>
     * This will overwrite all objects linked to an existing floor object with the same building name and floor number.
     *
     * @param floorData A JSON Map in the format specified in the ISU Floor Mapper software
     * @return The Floor object linked to all the data that was just added
     */
    @PutMapping("/api/floordata")
    public Floor handleFloorData(@RequestBody Map<String, Object> floorData) {
        // set up/find building
        Building building;
        String buildingName = (String) floorData.get("Building");
        Optional<Building> existingBuilding = buildingRepository.findByBuildingName(buildingName);
        if (existingBuilding.isPresent()) {
            building = existingBuilding.get();
        } else {
            building = new Building("B-" + buildingName, buildingName, 0, 0, new ArrayList<>());
        }

        Integer floorNumber = Integer.parseInt((String) floorData.get("Floor"));
        if (!building.getFloors().contains(floorNumber)) {
            building.addFloor(floorNumber);
        }
        buildingRepository.save(building);

        // set up our current floor, delete it if current data exists
        Floor floor;
        Optional<Floor> existingFloor = floorRepository.findByBuildingIdAndFloorNumber(building.getId(), floorNumber);
        if (existingFloor.isPresent()) {
            floor = existingFloor.get();
            removeFloorData(floor.getId());
        } else {
            floor = new Floor("F-" + buildingName + floorNumber.toString(), building.getId(), floorNumber);
            floorRepository.save(floor);
        }
        // set up our intersections and rooms
        Map<String, Map<String, String>> vectorMap = ((ArrayList<Map<String, Map<String, String>>>) floorData.get("Vectors")).get(0);
        for (Map.Entry<String, Map<String, String>> vector : vectorMap.entrySet()) {
            Intersection i = new Intersection();

            i.setId("I-" + floorNumber.toString() + "-" + vector.getKey());
            i.setX(Float.parseFloat(vector.getValue().get("X")));
            i.setY(Float.parseFloat(vector.getValue().get("Y")));
            i.setFloorId(floor.getId());

            intersectionRepository.save(i);

            if (vector.getValue().get("Type").equals("R")) {
                Room r = new Room();

                String roomNumber = vector.getValue().get("Data");
                r.setId("R-" + buildingName + roomNumber);
                r.setFloorId(floor.getId());
                r.setRoomNumber(roomNumber);
                r.setIntersectionId(i.getId());

                roomRepository.save(r);
            }
        }

        // set up edges
        Map<String, Map<String, String>> edgeMap = ((ArrayList<Map<String, Map<String, String>>>) floorData.get("Edges")).get(0);
        for (Map.Entry<String, Map<String, String>> edge : edgeMap.entrySet()) {
            Edge e = new Edge();

            e.setId("E-" + buildingName + floorNumber.toString() + "-" + edge.getKey());
            e.setIntersection1Id("I-" + floorNumber.toString() + "-" + edge.getValue().get("1"));
            e.setIntersection2Id("I-" + floorNumber.toString() + "-" + edge.getValue().get("2"));
            e.setFloorId(floor.getId());

            edgeRepository.save(e);
        }

        return floorRepository.findByBuildingIdAndFloorNumber(building.getId(), floorNumber).get();
    }

    /**
     * Helper method to remove all objects linked to a specific floor.
     *
     * @param floorId Floor ID to be reset.
     */
    private void removeFloorData(String floorId) {
        edgeRepository.findByFloorId(floorId).forEach((edge) -> edgeRepository.delete(edge));
        roomRepository.findByFloorId(floorId).forEach((room) -> roomRepository.delete(room));
        intersectionRepository.findByFloorId(floorId).forEach((intersection) -> intersectionRepository.delete(intersection));
    }
}
