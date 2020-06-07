package com.example.isuview.Entities;

import android.widget.ImageView;
import java.util.Map;
/**
 * Object that takes use of the Building
 * Allows for setting the Max floor and Min Floor.
 */
public abstract class BuildingEntity {
    private int _id;
    private String _name;
    private String _address;
    private Map<Integer, Integer> _floors;
    private int _currFloor;
    private int _maxFloor;
    private int _minFloor;
    private ImageView _imgView;
    /**
     * Sets the Imageview
     *
     * @param img Sets the Image
     */
    public void setImageView(ImageView img) {
        _imgView = img;
    }
    /**
     * Gets the Image
     *
     * @return ImageView
     */
    public ImageView getImageView() {
        return _imgView;
    }
    /**
     * Sets the Id
     *
     * @param id sets _id
     */
    public void setId(int id) {
        _id = id;
    }
    /**
     * Gets the Id
     *
     * @return int
     */
    public int getId() {
        return _id;
    }
    /**
     * Sets the name
     *
     * @param name sets _name
     */
    public void setName(String name) {
        _name = name;
    }
    /**
     * Gets the Name
     *
     * @return String
     */
    public String getName() {
        return _name;
    }
    /**
     * Sets the address
     *
     * @param address sets _address
     */
    public void setAddress(String address) {
        _address = address;
    }
    /**
     * Gets the Address
     *
     * @return String
     */
    public String getAddress() {
        return _address;
    }
    /**
     * Sets the minFloor
     *
     * @param floor sets minfloor
     */
    public void setMinFloor(int floor) {
        _minFloor = floor;
    }
    /**
     * Gets the min Floor
     *
     * @return int
     */
    public int getMinFloor() {
        return _minFloor;
    }
    /**
     * Sets the current floor
     *
     * @param floor set floor value.
     */
    public void setFloor(int floor) {
        _currFloor = floor;
    }
    /**
     * Gets the Floor
     *
     * @return int
     */
    public int getFloor() {
        return _currFloor;
    }
    /**
     * Sets the max Floor
     *
     * @param floor passing value to set.
     */
    public void setMaxFloor(int floor) {
        _maxFloor = floor;
    }
    /**
     * Gets the max Floor
     *
     * @return int
     */
    public int getMaxFloor() {
        return _maxFloor;
    }
    /**
     * Sets the floors
     *
     * @param floors passing value to set.
     */
    public void setFloors(Map<Integer, Integer> floors) {
        _floors = floors;
    }
    /**
     * Return Floors
     *
     * @return Map<Integer, Integer>
     */
    public Map<Integer, Integer> getFloors() {
        return _floors;
    }
    /**
     * Changes the currFloor by up one.
     *
     * @return boolean
     */
    public boolean goUpFloor() {
        int floor = _currFloor + 1;
        if (floor >= _minFloor && floor <= _maxFloor) {
            setFloor(floor);
            return true;
        }
        return false;
    }
    /**
     * Changes the currFloor by down one.
     *
     * @return boolean
     */
    public boolean goDownFloor() {
        int floor = _currFloor - 1;
        if (floor >= _minFloor && floor <= _maxFloor) {
            _currFloor = floor;
            return true;
        }
        return false;
    }
    /**
     * Returns the Floor in a custom way depending on the building.
     *
     * @return Integer
     */
    public abstract Integer returnFloor();
    /**
     * Constructs the Building Object
     *
     * @param id        Building of Id
     * @param floors    floors linking to the resource
     * @param name      name of the building
     * @param currFloor current floor
     * @param maxFloor  Max floor
     * @param minFloor  min floor
     */
    public BuildingEntity(int id, Map<Integer, Integer> floors, String name, int currFloor, int maxFloor, int minFloor) {
        setId(id);
        setFloors(floors);
        setName(name);
        setFloor(currFloor);
        setMaxFloor(maxFloor);
        setMinFloor(minFloor);
    }
    /**
     * Constructs the Building Object
     */
    public BuildingEntity() {
    }
}