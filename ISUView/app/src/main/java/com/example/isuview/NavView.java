package com.example.isuview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

@SuppressLint("AppCompatCustomView")
public class NavView extends ImageView {
    int i = 0;
    float[][] route;
    private NavViewModel model;
    public NavView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    private float mScaleFactor = 1.0f;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.RED);
        p.setStrokeWidth(15f);
        p.setStyle(Paint.Style.STROKE);
        drawRoute(canvas, p, route);
    }
    public void setScale(float scale){
        mScaleFactor = scale;
    }

    //Lines
    public float[][] getRoute(){
        if(i==0){
            i++;
            float[][] ans = {{0,0}, {20,20},{20,10}, {50,50}};
            return ans;
        }else{
            i = 0;
            float[][] ans = {{50,50}, {50,20},{20,0}, {100,50}};
            return ans;
        }
    }

    private void drawRoute(Canvas canvas, Paint p, float[][] route){
        float[] start, end;
        if(route == null) return;
        for(int i =1; i<route.length;i++){
            start = convert(route[i-1][0], route[i-1][1]);
            end = convert(route[i][0],route[i][1]);
            canvas.drawLine(start[0],start[1],end[0],end[1], p);
        }
    }

    //X and Y should be 0-100
    //Converts points to pixel
    private float[] convert(float X, float Y){
        float x1 = 0, y1 = 490, x2 = 1100, y2=1330;//TODO Make global Constant?
        float[] ans = new float[2];
        if(X>100) X = 100;
        if(Y>100) Y = 100;

        ans[0] = ((x2 - x1) * X/100 )+ x1;
        ans[1] = ((y2 - y1) * Y/100 )+ y1;
        return ans;
    }

    //Draws Vertical Line
    private void drawVLine(Canvas canvas, float X, float startY, float stopY, Paint p){
        canvas.drawLine(X,startY,X,stopY,p);
    }

    //Draws Horizontal Line
    private void drawHLine(Canvas canvas, float Y, float startX, float stopX, Paint p){
        canvas.drawLine(startX,Y,stopX,Y,p);
    }
    protected NavViewModel getModel(){
        return model;
    }

}
