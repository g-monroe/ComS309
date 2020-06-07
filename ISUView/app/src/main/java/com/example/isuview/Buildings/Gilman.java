package com.example.isuview.Buildings;
import android.widget.ImageView;

import com.example.isuview.Entities.BuildingEntity;
import com.example.isuview.R;
import java.util.HashMap;
import java.util.Map;


public class Gilman extends BuildingEntity {
    public Gilman(ImageView img){
        Map<Integer, Integer> floors = new HashMap<Integer, Integer>();
        floors.put(-1, R.drawable.gilman_1);
        floors.put(0, R.drawable.gilman1);
        floors.put(1, R.drawable.gilman2);
        floors.put(2, R.drawable.gilman3);
        setName("Gilman");
        setAddress("Gilman Hall");
        setFloors(floors);
        setImageView(img);
        setFloor(0);
        setMaxFloor(2);
        setMinFloor(-1);

    }
    @Override
    public Integer returnFloor() {
        int currFloor = getFloor();
        Map<Integer, Integer> floors = getFloors();
        for (Map.Entry<Integer, Integer> entry : floors.entrySet()) {
            Integer key = entry.getKey();
            Integer val = entry.getValue();
            if (key == currFloor){
                return val;
            }
        }
        return floors.get(0);
    }
}
