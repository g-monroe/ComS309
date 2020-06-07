package com.coms309SS6.isuview.controllers;

import com.coms309SS6.isuview.models.Building;
import com.coms309SS6.isuview.models.Floor;
import com.coms309SS6.isuview.models.Intersection;
import com.coms309SS6.isuview.models.Room;
import com.coms309SS6.isuview.repository.BuildingRepository;
import com.coms309SS6.isuview.repository.FloorRepository;
import com.coms309SS6.isuview.repository.IntersectionRepository;
import com.coms309SS6.isuview.repository.RoomRepository;
import com.coms309SS6.isuview.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * REST Controller to allow the frontend app to request and receive routes within buildings.
 */
@RestController
public class RouteController {

    @Autowired
    IntersectionRepository intersectionRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BuildingRepository buildingRepository;

    @Autowired
    FloorRepository floorRepository;

    @Autowired
    RouteService routeService;

    //Accepts to /api/routing?startRoom=0123&endRoom=0124

    /**
     * @param startRoom Start room to path from
     * @param endRoom   End room to path to
     * @return List of intersections in the path
     * @deprecated Get mapping to get a route of intersections between rooms in a building.
     * Deprecated because this doesn't make the shortest path.
     */
    @GetMapping("/api/routing")
    public List<Intersection> handleRouteData(@RequestParam(value = "startRoom") Integer startRoom,
                                              @RequestParam(value = "endRoom") Integer endRoom) {
        List<Room> start = roomRepository.findByRoomNumber(startRoom.toString());
        if (start.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<Intersection> startIntersection = intersectionRepository.findById(start.get(0).getIntersectionId());
        if (!startIntersection.isPresent()) {
            return Collections.emptyList();
        }

        List<Room> end = roomRepository.findByRoomNumber(endRoom.toString());
        if (end.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<Intersection> endIntersection = intersectionRepository.findById(end.get(0).getIntersectionId());
        if (!endIntersection.isPresent()) {
            return Collections.emptyList();
        }

        HashSet<String> visited = new HashSet<>();

        List<Intersection> route = routeService.depthFirstSearch(startIntersection.get(), endIntersection.get(), null, 0, visited);

        if (route == null) {
            return Collections.emptyList();
        }

        return route;
    }

    //Accepts to /api/routingv2?startRoom=0123&endRoom=0124

    /**
     * @param startRoom Start room to path from
     * @param endRoom   End room to path to
     * @return List of intersections in the path
     * @deprecated Get mapping to get a route of intersections between rooms in a building using Djikstra's algorithm.
     * Deprecated because this doesn't account for separate buildings.
     */
    @GetMapping("/api/routingv2")
    public List<Intersection> handleRouteDataDjikstra(@RequestParam(value = "startRoom") Integer startRoom,
                                                      @RequestParam(value = "endRoom") Integer endRoom) {
        List<Room> start = roomRepository.findByRoomNumber(startRoom.toString());
        if (start.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<Intersection> startIntersection = intersectionRepository.findById(start.get(0).getIntersectionId());
        if (!startIntersection.isPresent()) {
            return Collections.emptyList();
        }

        List<Room> end = roomRepository.findByRoomNumber(endRoom.toString());
        if (end.isEmpty()) {
            return Collections.emptyList();
        }

        Optional<Intersection> endIntersection = intersectionRepository.findById(end.get(0).getIntersectionId());
        if (!endIntersection.isPresent()) {
            return Collections.emptyList();
        }

        Map<Intersection, Intersection> prev = routeService.floorDjikstra(startIntersection.get());

        return RouteService.getPathFromGraph(prev, startIntersection.get(), endIntersection.get());
    }

    //Accepts to /api/routingv3?start=Gilman,1656&end=Pearson,1130

    /**
     * Get mapping to get multiple legs of a route between rooms, possibly between different buildings.
     * <p>
     * Separates the list of routes by legs, so for a route from Pearson 2155 to Atanasoff 125 it would make a route
     * from Pearson 2155 to the stairs, Pearson first floor stairs to the door, and the Atanasoff door to 125.
     *
     * @param start The starting building and room number. separated by a comma.
     * @param end   The ending building and room number, separated by a comma.
     * @return The list of legs for the route, with each leg containing the building name, floor number, and list of intersections in the leg.
     */
    @GetMapping("/api/routingv3")
    public List<Map<String, Object>> handleRouteDataBetweenBuildings(@RequestParam(value = "start") String start,
                                                                     @RequestParam(value = "end") String end) {
        // get our starting room
        String[] startArgs = start.split(",");
        if (startArgs.length < 2) return Collections.emptyList();

        Optional<Building> startBuilding = buildingRepository.findByBuildingName(startArgs[0]);
        if (!startBuilding.isPresent()) return Collections.emptyList();

        List<Floor> startFloors = floorRepository.findByBuildingId(startBuilding.get().getId());
        String startRoomNumber = startArgs[1];
        Room startRoom = null;
        Floor startFloor = null;

        for (Floor floor : startFloors) {
            Optional<Room> room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), startRoomNumber);
            if (room.isPresent()) {
                startFloor = floor;
                startRoom = room.get();
                break;
            }
        }

        if (startRoom == null) return Collections.emptyList();

        Intersection startIntersection = intersectionRepository.findById(startRoom.getIntersectionId()).get();

        // get our ending room
        String[] endArgs = end.split(",");
        if (endArgs.length < 2) return Collections.emptyList();

        Optional<Building> endBuilding = buildingRepository.findByBuildingName(endArgs[0]);
        if (!endBuilding.isPresent()) return Collections.emptyList();

        List<Floor> endFloors = floorRepository.findByBuildingId(endBuilding.get().getId());
        String endRoomNumber = endArgs[1];
        Room endRoom = null;
        Floor endFloor = null;

        for (Floor floor : endFloors) {
            Optional<Room> room = roomRepository.findByFloorIdAndRoomNumber(floor.getId(), endRoomNumber);
            if (room.isPresent()) {
                endFloor = floor;
                endRoom = room.get();
                break;
            }
        }

        if (endRoom == null) return Collections.emptyList();

        Intersection endIntersection = intersectionRepository.findById(endRoom.getIntersectionId()).get();

        // get our 'legs' of the journey
        // note: currently ignores staircase cases
        List<Map<String, Object>> legs = new ArrayList<>();

        if (startBuilding.get().getId().equals(endBuilding.get().getId())) {
            // we're mapping room to room in the same building
            Map<Intersection, Intersection> prev = routeService.floorDjikstra(startIntersection);
            List<Intersection> path = RouteService.getPathFromGraph(prev, startIntersection, endIntersection);
            Map<String, Object> leg = new HashMap<>();
            leg.put("building", startBuilding.get().getBuildingName());
            leg.put("floorNum", startFloor.getFloorNumber());
            leg.put("route", path);
            legs.add(leg);
        } else {
            // we're mapping room to room in a different building
            // get start leg
            Intersection startExitDoor = routeService.getLowestIntersection(startFloor.getId());
            Map<Intersection, Intersection> startMap = routeService.floorDjikstra(startIntersection);
            List<Intersection> startPath = RouteService.getPathFromGraph(startMap, startIntersection, startExitDoor);
            Map<String, Object> leg = new HashMap<>();
            leg.put("building", startBuilding.get().getBuildingName());
            leg.put("floorNum", startFloor.getFloorNumber());
            leg.put("route", startPath);
            legs.add(leg);

            // get end leg
            Intersection endExitDoor = routeService.getLowestIntersection(endFloor.getId());
            Map<Intersection, Intersection> endMap = routeService.floorDjikstra(endExitDoor);
            List<Intersection> endPath = RouteService.getPathFromGraph(endMap, endExitDoor, endIntersection);
            leg = new HashMap<>();
            leg.put("building", endBuilding.get().getBuildingName());
            leg.put("floorNum", endFloor.getFloorNumber());
            leg.put("route", endPath);
            legs.add(leg);
        }

        return legs;
    }
}
