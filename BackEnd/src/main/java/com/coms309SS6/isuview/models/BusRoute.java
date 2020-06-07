package com.coms309SS6.isuview.models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity holding the data for a bus route - name, color, and nubber
 */
@Entity
public class BusRoute {
    @Id
    private int id;
    private int routeNum;
    private String name;

    //right now we don't track this in the app
    @Deprecated
    private String color; //in hexcode format ('#FFFFFF')

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(int routeNum) {
        this.routeNum = routeNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}