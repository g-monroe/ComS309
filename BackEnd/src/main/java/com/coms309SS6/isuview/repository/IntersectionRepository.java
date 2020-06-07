package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Intersection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntersectionRepository extends CrudRepository<Intersection, String> {
    List<Intersection> findByFloorId(String floorId);
}