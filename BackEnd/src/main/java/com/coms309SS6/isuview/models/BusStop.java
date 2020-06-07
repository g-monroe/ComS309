package com.coms309SS6.isuview.models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity holding data for a bus stup location - name and location on campus
 */
@Entity
public class BusStop {
    @Id
    private int id;
    private String name;

    // we don't set either of these right now because we don't track bus stop location in the app
    @Deprecated
    private long longitude;
    @Deprecated
    private long latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}