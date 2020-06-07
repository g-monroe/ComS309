package com.coms309SS6.isuview.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {
    @Id
    @Column(length = 32)
    private String id;

    private String floorId;

    private String intersectionId;

    private String roomNumber;

    /**
     * Gets Id
     *
     * @return String id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the Id
     *
     * @param id string
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets FloorId
     *
     * @return String floorId
     */
    public String getFloorId() {
        return floorId;
    }

    /**
     * Sets the Floor Id
     *
     * @param floorId string
     */
    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    /**
     * Gets IntersectionId
     *
     * @return String intersectionId
     */
    public String getIntersectionId() {
        return intersectionId;
    }

    /**
     * Sets the Intersection Id
     *
     * @param intersectionId string
     */
    public void setIntersectionId(String intersectionId) {
        this.intersectionId = intersectionId;
    }

    /**
     * Gets RoomNumber
     *
     * @return String roomNumber
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the Room Number
     *
     * @param roomNumber string
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}