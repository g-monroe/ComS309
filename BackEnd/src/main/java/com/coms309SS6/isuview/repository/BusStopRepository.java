package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.BusStop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusStopRepository extends CrudRepository<BusStop, Integer> {
    Optional<BusStop> findByName(String name);
}