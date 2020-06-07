package com.coms309SS6.isuview.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;

/**
 * Entity holding the data for a bus stoppage, when a bus route stops at a bus stop - the route id, stop id, and time.
 */
@Entity
public class BusStoppage {
    @Id
    private int id;
    private int busRouteId;
    private int busStopId;
    private Time time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(int busRouteId) {
        this.busRouteId = busRouteId;
    }

    public int getBusStopId() {
        return busStopId;
    }

    public void setBusStopId(int busStopId) {
        this.busStopId = busStopId;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}