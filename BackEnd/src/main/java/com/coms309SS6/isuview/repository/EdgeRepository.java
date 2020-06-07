package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Edge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgeRepository extends CrudRepository<Edge, String> {
    List<Edge> findByIntersection1Id(String intersection1Id);

    List<Edge> findByIntersection2Id(String intersection2Id);

    List<Edge> findByFloorId(String floorId);
}