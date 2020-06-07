package com.coms309SS6.isuview.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Intersection {
    @Id
    @Column(length = 32)
    private String id;
    private float x;
    private float y;
    private String floorId;

    /**
     * Constructs an empty Intersection object.
     */
    public Intersection() {

    }

    /**
     * Constructs an Intersection object.
     *
     * @param id
     * @param x
     * @param y
     * @param floorId
     */
    public Intersection(String id, float x, float y, String floorId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.setFloorId(floorId);
    }

//    @JoinTable(name = "Edge", joinColumns = @JoinColumn(name = "id"))
//    private List<Edge> edges;

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
     * Get X.
     *
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Set X.
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get Y.
     *
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Set Y.
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Get floor ID.
     *
     * @return
     */
    public String getFloorId() {
        return floorId;
    }

    /**
     * Set floor ID.
     *
     * @param floorId
     */
    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
}