package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.BusStoppage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusStoppageRepository extends CrudRepository<BusStoppage, Integer> {
    List<BusStoppage> findByBusRouteId(int busRouteId);

    List<BusStoppage> findByBusStopId(int busStopId);

    List<BusStoppage> findByBusRouteIdAndBusStopId(int busRouteId, int busStopId);

    List<BusStoppage> findByBusRouteIdAndBusStopIdOrderByTime(int busRouteId, int busStopId);
}