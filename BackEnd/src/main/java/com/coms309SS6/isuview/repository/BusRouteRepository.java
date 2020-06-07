package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.BusRoute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRouteRepository extends CrudRepository<BusRoute, Integer> {
    List<BusRoute> findByRouteNum(int routeNum);

    Optional<BusRoute> findByName(String name);
}