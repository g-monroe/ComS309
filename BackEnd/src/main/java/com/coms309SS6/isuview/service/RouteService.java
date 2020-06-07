package com.coms309SS6.isuview.service;

import com.coms309SS6.isuview.models.Edge;
import com.coms309SS6.isuview.models.Intersection;
import com.coms309SS6.isuview.repository.EdgeRepository;
import com.coms309SS6.isuview.repository.IntersectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service to assist with making routes using the DFS and Djikstra's pathfinding algorithms.
 */
@Service
public class RouteService {
    @Autowired
    IntersectionRepository intersectionRepository;

    @Autowired
    EdgeRepository edgeRepository;

    /**
     * @param currentIntersection     The current point being mapped from
     * @param destinationIntersection The destination
     * @param previousIntersection    The previous point that was visited
     * @param depth                   The amount of iterations so far
     * @param visited                 A map of the visited points
     * @return The path of points from the destination to the starting intersection
     * @deprecated Does a depth first search across all points to find a path between two intersections.
     */
    public List<Intersection> depthFirstSearch(Intersection currentIntersection, Intersection destinationIntersection, Intersection previousIntersection, int depth, HashSet<String> visited) {
        if (depth > 50) {
            return null;
        }

        if (visited.contains(currentIntersection.getId())) {
            return null;
        }
        visited.add(currentIntersection.getId());

        if (currentIntersection.getId().equals(destinationIntersection.getId())) {
            return new ArrayList<>();
        }

        List<Intersection> connections = new ArrayList<>();

        List<Edge> edges = edgeRepository.findByIntersection1Id(currentIntersection.getId());
        for (Edge edge : edges) {
            Optional<Intersection> intersection = intersectionRepository.findById(edge.getIntersection2Id());
            if (!intersection.isPresent() || (previousIntersection != null && previousIntersection.getId().equals(intersection.get().getId()))) {
                continue;
            }
            if (!connections.contains(intersection.get())) {
                connections.add(intersection.get());
            }
        }

        edges = edgeRepository.findByIntersection2Id(currentIntersection.getId());
        for (Edge edge : edges) {
            Optional<Intersection> intersection = intersectionRepository.findById(edge.getIntersection1Id());
            if (!intersection.isPresent() || (previousIntersection != null && previousIntersection.getId().equals(intersection.get().getId()))) {
                continue;
            }
            if (!connections.contains(intersection.get())) {
                connections.add(intersection.get());
            }
        }

        if (connections.isEmpty()) {
            return null;
        }

        List<Intersection> shortestList = null;
        for (Intersection connection : connections) {
            List<Intersection> foundList = depthFirstSearch(connection, destinationIntersection, currentIntersection, depth + 1, visited);
            if (shortestList == null) {
                shortestList = foundList;
            } else if (foundList != null) {
                if (foundList.size() < shortestList.size()) {
                    shortestList = foundList;
                }
            }
        }

        if (shortestList == null) {
            return null;
        }

        shortestList.add(currentIntersection);
        return shortestList;
    }

    /**
     * Temporary function to get the southmost intersection on a floor to simulate a door.
     *
     * @param floorId ID of the floor to search
     * @return The lowest intersection on the floor
     */
    // we use this in place of having a door right now
    public Intersection getLowestIntersection(String floorId) {
        Intersection lowest = null;
        for (Intersection intersection : intersectionRepository.findByFloorId(floorId)) {
            if (lowest == null || lowest.getY() < intersection.getY()) {
                lowest = intersection;
            }
        }
        return lowest;
    }

    /**
     * Helper method to backtrack through a prev map from a Djikstra's method run to get a path between two intersections.
     *
     * @param graph The prev map from the Djikstra's run
     * @param start The beginning point for the path
     * @param end   The endpoint for the path
     * @return A list of intersections in the order of the path
     */
    public static List<Intersection> getPathFromGraph(Map<Intersection, Intersection> graph, Intersection start, Intersection end) {
        List<Intersection> path = new ArrayList<>();
        Intersection current = end;
        while (current != start) {
            path.add(current);
            current = graph.get(current);
            if (current == null) {
                // there isn't a path
                return null;
            }
        }
        path.add(current);
        return path;
    }

    /**
     * Does a Djikstra's method starting from a given intersection over an entire floor.
     *
     * @param start The start intersection for the Djikstra's run
     * @return The resultant prev map from the Djikstra's method run
     */
    // we're doing this on the fly -- in the future we can cache the entire djikstra result
    public Map<Intersection, Intersection> floorDjikstra(Intersection start) {
        Map<Intersection, Double> dist = new HashMap<>();
        Map<Intersection, Intersection> prev = new HashMap<>();
        Set<Intersection> vertices = new HashSet<>();

        Iterable<Intersection> allIntersections = intersectionRepository.findByFloorId(start.getFloorId());
        for (Intersection intersection : allIntersections) {
            dist.put(intersection, Double.MAX_VALUE / 2); // cut it in half to prevent overflow
            vertices.add(intersection);
        }
        dist.put(start, 0.0);

        while (!vertices.isEmpty()) {
            Map<Intersection, Double> distVertices = new HashMap<>();
            Double smallestDist = Double.MAX_VALUE;
            Intersection current = null;
            for (Map.Entry<Intersection, Double> entry : dist.entrySet()) {
                if (entry.getValue() < smallestDist && vertices.contains(entry.getKey())) {
                    current = entry.getKey();
                    smallestDist = entry.getValue();
                }
            }
            if (current == null) {
                break;
            }

            vertices.remove(current);

            List<Intersection> neighbors = new ArrayList<>();

            List<Edge> edges = edgeRepository.findByIntersection1Id(current.getId());
            edges.forEach(edge -> intersectionRepository.findById(edge.getIntersection2Id())
                    .ifPresent(intersection -> neighbors.add(intersection)));

            edges = edgeRepository.findByIntersection2Id(current.getId());
            edges.forEach(edge -> intersectionRepository.findById(edge.getIntersection1Id())
                    .ifPresent(intersection -> neighbors.add(intersection)));

            for (Intersection neighbor : neighbors) {
                Double newDist = dist.get(current) + calculateDist(current, neighbor);
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    prev.put(neighbor, current);
                }
            }
        }

        return prev;
    }

    /**
     * Calculates the distance between two points using the distance formula.
     *
     * @param intersection1 The first intersection
     * @param intersection2 The second intersection
     * @return The distance between the two points.
     */
    private double calculateDist(Intersection intersection1, Intersection intersection2) {
        return Math.sqrt(
                Math.pow(intersection1.getX() - intersection2.getX(), 2) +
                        Math.pow(intersection1.getY() - intersection2.getY(), 2));
    }
}
