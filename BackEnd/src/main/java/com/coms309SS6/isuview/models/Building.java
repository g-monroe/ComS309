package com.coms309SS6.isuview.models;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Building Entity
 */
@Entity
public class Building {
    @Id
    @Column(length = 32)
    private String id;
    private String buildingName;
    private float longitude;
    private float latitude;
    @ElementCollection
    private List<Integer> floors = new ArrayList<>();

    /**
     * Constructor Building
     */
    public Building() {

    }

    /**
     * Constructor Building
     *
     * @param id           String
     * @param buildingName String
     * @param longitude    float
     * @param latitude     float
     * @param floors       floors
     */
    public Building(String id, String buildingName, float longitude, float latitude, List<Integer> floors) {
        this.id = id;
        this.buildingName = buildingName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.floors = floors;
    }

    /**
     * Gets the id
     *
     * @return id string
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the Building name
     *
     * @return String
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * Sets the Building Name
     *
     * @param buildingName String
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /**
     * Gets the longitude
     *
     * @return float
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude
     *
     * @param longitude float
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the latitude
     *
     * @return float
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude
     *
     * @param latitude float
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the Floors
     *
     * @return list of floors
     */

    public List<Integer> getFloors() {
        return floors;
    }

    /**
     * Adds Floor
     *
     * @param floor Floor number to add
     */
    public void addFloor(Integer floor) {
        floors.add(floor);
        Collections.sort(floors);
    }

    /**
     * Sets the Floors
     *
     * @param floors Floors to set
     */
    public void setFloors(List<Integer> floors) {
        this.floors = floors;
    }
}