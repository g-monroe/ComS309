package com.example.isuview;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BusRouteViewModel extends AppCompatActivity {
    String curRoute, curStop, curTime;
    BusRouteViewController busControl;

    public BusRouteViewModel(BusRouteViewController input){
        busControl = input;
    }

    protected void callAPI(final ListView list, String url){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(busControl);
        System.out.println(url);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{
                            // response = response.getJSONObject(0).getJSONArray("list");
                            // Loop through the array elements
                            String[] routes = new String[response.length()];

                            for (int i=0; i<response.length(); i++) {
                                // Get current json object
                                Object busRoute = response.get(i);
                                String routeName = busRoute.toString();
                                routes[i] = routeName;

                                // Get the current point (json object) data
                                System.out.println(busRoute.toString());
                            }
                            list.setAdapter(busControl.listAdapter(routes));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println("Error on API call: BUS ROUTE");
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    protected void callAPI(final Spinner spin, String url){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(busControl);
        System.out.println(url);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{
                            // response = response.getJSONObject(0).getJSONArray("list");
                            // Loop through the array elements
                            String[] routes = new String[response.length()];

                            for (int i=0; i<response.length(); i++) {
                                // Get current json object
                                JSONObject busRoute = response.getJSONObject(i);
                                String routeName = busRoute.getString("name");
                                routes[i] = routeName;

                                // Get the current point (json object) data
                                System.out.println(busRoute.getString("name"));
                            }
                            spin.setAdapter(busControl.spinAdapter(routes));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println("Error on API call: BUS ROUTE");
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
}
