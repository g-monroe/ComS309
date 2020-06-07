package com.example.isuview.Buildings;

import android.widget.ImageView;

import com.example.isuview.Entities.BuildingEntity;

import java.util.ArrayList;

public class Buildings {
    public ArrayList<BuildingEntity> buildings = new ArrayList<BuildingEntity>();
    public Buildings(ImageView img){
        Gilman gilman = new Gilman(img);
        Atanasoff atanasoff = new Atanasoff(img);
        Pearson pearson = new Pearson(img);
        buildings.add(gilman);
        buildings.add(atanasoff);
        buildings.add(pearson);
    }
    public BuildingEntity getByName(String name){
        for (BuildingEntity building : buildings) {
            if (building.getName() == name){
                return building;
            }
        }
        return null;
    }
    public BuildingEntity getByAddress(String address){
        System.out.println("A:" + address);
        for (BuildingEntity building : buildings) {
            System.out.println("B:" + building.getAddress());
            if (building.getAddress() == address){
                return building;
            }
        }
        return null;
    }
}
