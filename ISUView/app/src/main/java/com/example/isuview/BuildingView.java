package com.example.isuview;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.isuview.Buildings.Buildings;
import com.example.isuview.Entities.BuildingEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;

import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuildingView extends AppCompatActivity {
    EditText startRoom, endRoom;
    Boolean isStart = false;
    String building;
    NavViewController navView;
    private ScaleGestureDetector mScaleGestureDetector;
    private int minFloor = -1;
    private int currFloor = 0;
    private String buildingId = "Gilman";
    private int maxFloor = 0;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;
    Buildings buildings;
    BuildingEntity currentBuilding;
    private float xCoOrdinate, yCoOrdinate;
    // This is the gesture detector compat instance.
    private GestureDetectorCompat gestureDetectorCompat = null;
    // Create a common gesture listener object.
    GestureListener gestureListener = new GestureListener(){

        @Override
        public void singleTap() {

        }

        @Override
        public void doubleTap() {

        }

        @Override
        public void swipeUp() {
            if (mScaleFactor < 1.2){
                currentBuilding.goUpFloor();
                navView.setImageResource(currentBuilding.returnFloor());
            }
        }

        @Override
        public void swipeDown() {
            if (mScaleFactor < 1.2) {
                currentBuilding.goDownFloor();
                navView.setImageResource(currentBuilding.returnFloor());
            }
        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }
    };
    ScaleListener scaleListener = new ScaleListener(){
        @Override
        public boolean onSpread(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            if (mScaleFactor > 1) {
                navView.setScaleX(mScaleFactor);
                navView.setScaleY(mScaleFactor);
                navView.setScale(mScaleFactor);
                navView.invalidate();
                return true;
            }
            return false;
        }
    };
    boolean scaling = false;
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();

        mScaleGestureDetector.onTouchEvent(motionEvent);

        if (pointerCount > 1){
            scaling = true;
            return true;
        }
        // Pass activity on touch event to the gesture detector.
        gestureDetectorCompat.onTouchEvent(motionEvent);
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
                xCoOrdinate = navView.getX() - motionEvent.getRawX();
                yCoOrdinate = navView.getY() - motionEvent.getRawY();

                break;
            case MotionEvent.ACTION_MOVE :
                if (scaling || mScaleFactor < 1.3){
                    navView.animate().x(0).y(0).setDuration(0).start();
                    return true;
                }
                navView.animate().x(motionEvent.getRawX() + xCoOrdinate).y(motionEvent.getRawY() + yCoOrdinate).setDuration(0).start();
                break;
            case MotionEvent.ACTION_UP :
                scaling = false;
                break;
            default :

        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String address = getIntent().getStringExtra("address");
        isStart = getIntent().getBooleanExtra("isStart", false);

        startRoom = findViewById(R.id.startRoom);
        endRoom = findViewById(R.id.endRoom);
        startRoom.setText(BuildingDecoder.getStartRoom(address));

        String roomNum = getIntent().getStringExtra("toRoom");
        if(roomNum.isEmpty()){
            endRoom.setText(BuildingDecoder.getEndRoom(address));
        }else{
            endRoom.setText(roomNum);
        }

        //navView = findViewById(R.id.navcanvas);
        navView= findViewById(R.id.navcanvas);
        navView.setImageResource(BuildingDecoder.decode(address));
        building = BuildingDecoder.getName(address);
        buildings = new Buildings(navView);
        currentBuilding = buildings.getByName(building);
        System.out.println(buildings.getByAddress(address));
        callAPI();

        // Create the gesture detector with the gesture listener.
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);

        mScaleGestureDetector = new ScaleGestureDetector(this, scaleListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Fetching route from " + startRoom.getText().toString() +" to "+ endRoom.getText().toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                callAPI();
                if(isStart) finish();
            }
        });

    }
    public void setFloor(int floor){
        if (floor >= minFloor && floor <= maxFloor){
            currFloor = floor;
            renderFloor();
        }
    }
    public void goUpFloor(){
        int floor = currFloor + 1;
        if (floor >= minFloor && floor <= maxFloor){
            currFloor = floor;
            renderFloor();
        }
    }
    public void goDownFloor(){
        int floor = currFloor - 1;
        if (floor >= minFloor && floor <= maxFloor){
            currFloor = floor;
            renderFloor();
        }
    }
    private void renderFloor(){
        if (buildingId == "Gilman"){
            switch (currFloor){
                case -1:
                    mImageView.setImageResource(R.drawable.gilman_1);
                    break;
                case 0:
                    mImageView.setImageResource(R.drawable.gilman1);
                    break;
                case 1:
                    mImageView.setImageResource(R.drawable.gilman2);
                    break;
                case 2:
                    mImageView.setImageResource(R.drawable.gilman3);
                    break;
                default:
                    mImageView.setImageResource(R.drawable.gilman1);
            }
        }
    }
    public void ToNextView(View view){
        Intent intent = new Intent(this, CommentView.class);
        intent.putExtra("building", building);
        startActivity(intent);
    }

    //make a route from here
    //update and invalidate from here
    private void callAPI(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.24.226.172:8080/api/routingv3?start="+ building + "," + startRoom.getText().toString() + "&end=" +building +","+ endRoom.getText().toString();
        System.out.println(url);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            int i =1;
            i++;
        }else{
            url.charAt(1);
        }

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        JSONArray jArr;
                        try{

                            response = response.getJSONObject(0).getJSONArray("route");
                            // Loop through the array elements
                            float[][] ans = new float[response.length()][2];

                            for (int i=0; i<response.length(); i++) {
                                // Get current json object
                                JSONObject point = response.getJSONObject(i);

                                // Get the current point (json object) data
                                ans[i][0] = (float) point.getDouble("x");
                                ans[i][1] = (float) point.getDouble("y");
                            }
                            navView.getModel().route = ans;
                            navView.invalidate();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        float[][] ans = navView.getModel().getRoute();
                        navView.getModel().route = ans;
                        navView.invalidate();
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
}
