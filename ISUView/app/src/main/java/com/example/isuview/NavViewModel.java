package com.example.isuview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class NavViewModel {
    private int i = 0;
    protected float[][] route;

    public NavViewModel(){

    }

    //Lines
    protected float[][] getRoute() {
        if (i == 0) {
            i++;
            float[][] ans = {{0, 0}, {20, 20}, {20, 10}, {50, 50}};
            return ans;
        } else {
            i = 0;
            float[][] ans = {{50, 50}, {50, 20}, {20, 0}, {100, 50}};
            return ans;
        }
    }

    protected void drawRoute(Canvas canvas, Paint p, float[][] route) {
        float[] start, end;
        if (route == null) return;
        for (int i = 1; i < route.length; i++) {
            start = convert(route[i - 1][0], route[i - 1][1]);
            end = convert(route[i][0], route[i][1]);
            canvas.drawLine(start[0], start[1], end[0], end[1], p);
        }
    }

    //X and Y should be 0-100
    //Converts points to pixel
    protected float[] convert(float X, float Y) {
        //TODO base conversion on screen dimensions
        float x1 = 0, y1 = 490, x2 = 1100, y2 = 1330;
        float[] ans = new float[2];
        if (X > 100) X = 100;
        if (Y > 100) Y = 100;

        ans[0] = ((x2 - x1) * X / 100) + x1;
        ans[1] = ((y2 - y1) * Y / 100) + y1;
        return ans;
    }
}
