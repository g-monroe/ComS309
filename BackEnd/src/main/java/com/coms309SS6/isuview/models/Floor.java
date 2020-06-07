package com.coms309SS6.isuview.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Floor {
    @Id
    @Column(length = 32)
    private String id;

    @Column(length = 32)
    private String buildingId;

    private int floorNumber;

    /**
     * Constructs an empty Floor object.
     */
    public Floor() {

    }

    /**
     * Constructs a Floor object.
     *
     * @param id
     * @param buildingId
     * @param floorNumber
     */
    public Floor(String id, String buildingId, int floorNumber) {
        this.setId(id);
        this.setBuildingId(buildingId);
        this.setFloorNumber(floorNumber);
    }

    /**
     * Get ID.
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Set ID.
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get building ID.
     *
     * @return
     */
    public String getBuildingId() {
        return buildingId;
    }

    /**
     * Set building ID.
     *
     * @param buildingId
     */
    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    /**
     * Get floor number.
     *
     * @return
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Set floor number.
     *
     * @param floorNumber
     */
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
}