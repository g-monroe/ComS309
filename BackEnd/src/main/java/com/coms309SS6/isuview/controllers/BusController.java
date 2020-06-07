package com.coms309SS6.isuview.controllers;

import com.coms309SS6.isuview.models.BusRoute;
import com.coms309SS6.isuview.models.BusStop;
import com.coms309SS6.isuview.models.BusStoppage;
import com.coms309SS6.isuview.repository.BusRouteRepository;
import com.coms309SS6.isuview.repository.BusStopRepository;
import com.coms309SS6.isuview.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BusController {

    @Autowired
    BusService busService;

    @Autowired
    BusRouteRepository busRouteRepository;

    @Autowired
    BusStopRepository busStopRepository;

    @PutMapping("/api/bus/setData")
    public List<BusStoppage> handleBusData(@RequestBody Map<String, Object> busData) {
        return busService.setDataFromJson(busData);
    }

    @GetMapping("/api/bus/routes")
    public Iterable<BusRoute> getRoutes() {
        return busService.getAllRoutes();
    }

    @GetMapping("/api/bus/stops")
    public Iterable<BusStop> getStops(@RequestParam(value = "route", required = false) String routeName) {
        if (routeName == null) {
            return busService.getAllStops();
        } else {
            Optional<BusRoute> busRoute = busRouteRepository.findByName(routeName);
            if (busRoute.isPresent()) {
                return busService.getAllStopsForRoute(busRoute.get().getId());
            } else {
                return Collections.emptyList();
            }
        }
    }

    @GetMapping("/api/bus/times")
    public List<String> getTimes(@RequestParam(value = "route") String routeName,
                                 @RequestParam(value = "stop") String stopName) {
        Optional<BusRoute> busRoute = busRouteRepository.findByName(routeName);
        if (!busRoute.isPresent()) return Collections.emptyList();
        Optional<BusStop> busStop = busStopRepository.findByName(stopName);
        if (!busStop.isPresent()) return Collections.emptyList();
        return busService.getTimesForRouteAndStop(busRoute.get().getId(), busStop.get().getId()).stream().map(BusService::getReadableTime).collect(Collectors.toList());
    }
}
