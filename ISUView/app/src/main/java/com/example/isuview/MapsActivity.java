package com.example.isuview;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    protected Boolean isInStart = false;
    protected Toolbar toolbar;
    protected String destAddr,fromAddr;
    private LatLng destMarker;
    private ArrayList<LatLng> MarkerPoints = new ArrayList();
    private MapsViewModel model;
    EditText searchText, searchNumber, fromBuilding, fromRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new MapsViewModel(this);
        setContentView(R.layout.activity_maps);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Where to?                         From: (Optional)");
//        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        searchText = findViewById(R.id.toBuilding);
        searchNumber = findViewById(R.id.toRoom);
        fromBuilding = findViewById(R.id.fromBuilding);
        fromRoom = findViewById(R.id.fromRoom);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //TODO call gmaps api?
                // or delete
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //TODO clean up gps
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);


        // Add a marker at Iowa State and move the camera
        LatLng isu = new LatLng(42.0266, -93.6465);
//        mMap.addMarker(new MarkerOptions().position(isu).title("Iowa State"));

        // Zoom in on ISU
        float zoom = 15;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(isu, zoom));

        // Add test marker
        addMarker(new LatLng(40.0252, -93.6484), "Carver Hall");

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
            addMarkerandCallApi(point);
            }
        });
    }
    protected void routeBetweenMarkers(LatLng point){
                // Already two locations
                if(MarkerPoints.isEmpty())mMap.clear();
                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    mMap.clear();
                }
                // Adding new item to the ArrayList
                MarkerPoints.add(point);
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                // Setting the position of the marker
                options.position(point);
                /**
                 * For the start location, the color of marker is YELLOW and
                 * for the end location, the color of marker is RED.
                 */
                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);
                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);
                    // Getting URL to the Google Directions API
                    String url = model.getUrl(origin, dest);
                    Log.d("onMapClick", url);
                    model.callAPI(url);
                }
    }

    protected void addMarkerandCallApi(LatLng point){
        if(point == null) return;
        mMap.clear();
        destMarker = point;//TODO find building near marker
        MarkerOptions options = new MarkerOptions();
        options.position(point);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);
        // Checks, whether start and end locations are captured
        Location curr = mMap.getMyLocation();
        if(curr != null){
            LatLng origin =  new LatLng(curr.getLatitude(),curr.getLongitude());//new LatLng(currentPos.getLatitude(), currentPos.getLongitude());
            LatLng dest = point;
            // Getting URL to the Google Directions API
            String url = model.getUrl(origin, dest);
            Log.d("onMapClick", url);
            model.callAPI(url);
        }
    }

    private Marker addMarker(LatLng position, String markerText) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(markerText);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)));

        return mMap.addMarker(markerOptions);
    }

    public void ToNextView(View view){
        if(destAddr != null){
            Intent intent = new Intent(this, BuildingView.class);
            if(isInStart){
                intent.putExtra("address", fromAddr.split(",")[0]);
            }else{
                intent.putExtra("address", destAddr.split(",")[0]);
            }
            intent.putExtra("toRoom", searchNumber.getText().toString());
            intent.putExtra("isStart", isInStart);
            isInStart = false;
            startActivity(intent);
        }
    }

    public void ToBusView(View view){
        Intent intent = new Intent(this, BusRouteViewController.class);
        startActivity(intent);
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void SearchBuilding(View view){
        hideKeyboard();
        String textIn = searchText.getText().toString();
        String textFrom = fromBuilding.getText().toString();
        if(textIn.isEmpty()) return;
        if(textFrom.isEmpty()){
            model.SearchOneBuilding(textIn);
        }else {
            model.SearchBetweenBuildings(textIn, textFrom);
        }
    }

    protected void drawRoute(List<List<HashMap<String,String>>> result){
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();
            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.RED);
            Log.d("onPostExecute","onPostExecute lineoptions decoded");
        }
        // Drawing polyline in the Google Map for the i-th route
        if(lineOptions != null) {
            mMap.addPolyline(lineOptions);
        }
        else {
            Log.d("onPostExecute","without Polylines drawn");
        }
    }

    //GPS STuff
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        //todo change to ISU?
        LatLng redmond = new LatLng(47.6739881, -122.121512);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(50);
                    circleOptions.fillColor(Color.YELLOW);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };

}