package com.coms309SS6.isuview.service;

import com.coms309SS6.isuview.models.BusRoute;
import com.coms309SS6.isuview.models.BusStop;
import com.coms309SS6.isuview.models.BusStoppage;
import com.coms309SS6.isuview.repository.BusRouteRepository;
import com.coms309SS6.isuview.repository.BusStopRepository;
import com.coms309SS6.isuview.repository.BusStoppageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

@Service
public class BusService {
    @Autowired
    BusRouteRepository busRouteRepository;

    @Autowired
    BusStopRepository busStopRepository;

    @Autowired
    BusStoppageRepository busStoppageRepository;

    public Iterable<BusRoute> getAllRoutes() {
        return busRouteRepository.findAll();
    }

    public Iterable<BusStop> getAllStops() {
        return busStopRepository.findAll();
    }

    public Optional<BusStop> getStopByName(String name) {
        return busStopRepository.findByName(name);
    }

    public Iterable<BusStop> getAllStopsForRoute(int busRouteId) {
        Set<Integer> stopIds = new HashSet<>();
        Iterable<BusStoppage> busStoppages = busStoppageRepository.findByBusRouteId(busRouteId);

        for (BusStoppage busStoppage : busStoppages) {
            stopIds.add(busStoppage.getBusStopId());
        }

        return busStopRepository.findAllById(stopIds);
    }

    public List<BusStoppage> getStoppagesForRouteAndStop(int busRouteId, int busStopId) {
        return busStoppageRepository.findByBusRouteIdAndBusStopIdOrderByTime(busRouteId, busStopId);
    }

    public List<Time> getTimesForRouteAndStop(int busRouteId, int busStopId) {
        Iterable<BusStoppage> busStoppages = busStoppageRepository.findByBusRouteIdAndBusStopIdOrderByTime(busRouteId, busStopId);
        List<Time> times = new ArrayList<>();
        busStoppages.forEach((busStoppage) -> times.add(busStoppage.getTime()));
        return times;
    }

    /**
     * Inserts data in the JSON format specified here:
     * {
     * "route": "9 Plum",
     * "stop": "Kildee Hall",
     * "times": [
     * "7:11am",
     * "7:31am",
     * "7:51am",
     * "8:11am",
     * "8:31am",
     * "8:51am",
     * "9:11am",
     * "9:31am",
     * "9:51am",
     * "10:11am",
     * "10:31am",
     * "10:51am",
     * "11:11am",
     * "11:31am",
     * "11:51am",
     * "12:11pm",
     * "12:31pm",
     * "12:51pm",
     * "1:11pm",
     * "1:31pm",
     * "1:51pm"
     * ]
     * }
     *
     * @param json JSON map of data in specified format
     * @return List of bus stoppages for given route and stop
     */
    public List<BusStoppage> setDataFromJson(Map<String, Object> json) {
        // find/create route
        String routeName = (String) json.get("route");
        BusRoute busRoute;
        Optional<BusRoute> busRouteOptional = busRouteRepository.findByName(routeName);
        if (busRouteOptional.isPresent()) {
            busRoute = busRouteOptional.get();
        } else {
            busRoute = new BusRoute();
            busRoute.setId(routeName.hashCode()); // if we webscrape MyCyRide these should change to match their ID
            busRoute.setName(routeName);
            busRoute.setColor("#FFFFFF"); // we don't track this on the app
            busRoute.setRouteNum(Integer.parseInt(routeName.charAt(0) + ""));
            busRouteRepository.save(busRoute);
        }

        // find/create stop
        String stopName = (String) json.get("stop");
        BusStop busStop;
        Optional<BusStop> busStopOptional = busStopRepository.findByName(stopName);
        if (busStopOptional.isPresent()) {
            busStop = busStopOptional.get();
        } else {
            busStop = new BusStop();
            busStop.setId(stopName.hashCode()); // if we webscrape MyCyRide these should change to match their ID
            busStop.setName(stopName);
            busStop.setLongitude(0); // these aren't actually important on the app right now since we don't link it on the view
            busStop.setLatitude(0);
            busStopRepository.save(busStop);
        }

        // remove existing data
        for (BusStoppage stoppage : busStoppageRepository.findByBusRouteIdAndBusStopId(busRoute.getId(), busStop.getId())) {
            busStoppageRepository.delete(stoppage);
        }

        // save new times
        Iterable<String> times = (Iterable<String>) json.get("times");
        for (String timeStr : times) {
            Time time = getTimeFromString(timeStr);
            BusStoppage busStoppage = new BusStoppage();
            busStoppage.setId((routeName + stopName + timeStr).hashCode());
            busStoppage.setBusRouteId(busRoute.getId());
            busStoppage.setBusStopId(busStop.getId());
            busStoppage.setTime(time);
            busStoppageRepository.save(busStoppage);
        }

        return getStoppagesForRouteAndStop(busRoute.getId(), busStop.getId());
    }

    /**
     * Gets the earliest arrivable time for the given bus route and bus stop by comparing it to the
     * current time found by the system. We don't have to worry about timezone issues since our server is
     * hosted on an ISU server, which should have the same timezone as ISU itself.
     *
     * @param busRouteId Bus Route ID for our earliest arrivable time
     * @param busStopId  Bus Stop ID for our earliest arrivable time
     * @return The closest time that the bus will arrive to our stop
     */
    public Time getEarliestArrivableTime(int busRouteId, int busStopId) {
        Iterable<BusStoppage> busStoppages = busStoppageRepository.findByBusRouteIdAndBusStopIdOrderByTime(busRouteId, busStopId);
        Time curTime = Time.valueOf(LocalTime.now());
        for (BusStoppage busStoppage : busStoppages) {
            if (curTime.before(busStoppage.getTime())) {
                return busStoppage.getTime();
            }
        }
        return null;
    }

    /**
     * Converts our time object into a readable string i.e. "1:11pm".
     *
     * @param time Time to convert
     * @return Converted time string
     */
    public static String getReadableTime(Time time) {
        String meridiem = "am";
        int hours = time.getHours();
        if (hours >= 12) {
            hours -= 12;
            meridiem = "pm";
        }
        if (hours == 0) {
            hours = 12;
        }
        return hours + ":" + String.format("%02d", time.getMinutes()) + meridiem;
    }

    /**
     * Converts a readable time string (i.e. "1:11pm") into a java.sql.Time format.
     *
     * @param timeStr Readable time string
     * @return Time object to store
     */
    public static Time getTimeFromString(String timeStr) {
        String[] timeParts = timeStr.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1].substring(0, 2));
        if (Character.toLowerCase(timeParts[1].charAt(2)) == 'p') { //PM
            hours += 12;
        }
        if (hours % 12 == 0) { //convert 12 / 24 to 0 / 12
            hours -= 12;
        }
        return new Time(hours, minutes, 0);
    }
}
