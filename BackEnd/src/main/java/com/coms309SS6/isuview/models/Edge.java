package com.coms309SS6.isuview.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Edge {
    @Id
    @Column(length = 32)
    private String id;

    private String intersection1Id;
    private String intersection2Id;

    @Column(length = 32)
    private String floorId;

    /**
     * Gets the ID.
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the first intersection ID.
     *
     * @return first intersection ID
     */
    public String getIntersection1Id() {
        return intersection1Id;
    }

    /**
     * Sets the first intersection ID.
     *
     * @param intersection1Id intersection ID to set
     */
    public void setIntersection1Id(String intersection1Id) {
        this.intersection1Id = intersection1Id;
    }

    /**
     * Gets the second intersection ID.
     *
     * @return second intersection ID
     */
    public String getIntersection2Id() {
        return intersection2Id;
    }

    /**
     * Sets the second intersection ID.
     *
     * @param intersection2Id intersection ID to set
     */
    public void setIntersection2Id(String intersection2Id) {
        this.intersection2Id = intersection2Id;
    }

    /**
     * Gets the floor ID.
     *
     * @return floor ID
     */
    public String getFloorId() {
        return floorId;
    }

    /**
     * Sets the floor ID.
     *
     * @param floorId floor ID to set
     */
    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
}