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
public class NavViewController extends ImageView {
    private NavViewModel model;
    private float mScaleFactor = 1.0f;
    public NavViewController(Context context, AttributeSet attrs) {
        super(context, attrs);
        model = new NavViewModel();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.RED);
        p.setStrokeWidth(15f);
        p.setStyle(Paint.Style.STROKE);
        model.drawRoute(canvas, p, model.route);
    }
    public void setScale(float scale){
        mScaleFactor = scale;
    }
    protected NavViewModel getModel(){
        return model;
    }

}
