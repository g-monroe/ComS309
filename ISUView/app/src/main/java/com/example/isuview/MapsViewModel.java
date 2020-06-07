package com.example.isuview;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsViewModel extends AppCompatActivity {
    //TODO transfer the data here
    MapsActivity map;
    public MapsViewModel(MapsActivity input){
        map = input;
    }

    protected String getUrl(LatLng origin, LatLng dest){
        return "https://maps.googleapis.com/maps/api/directions/json?"+
                "origin="+ origin.latitude+","+origin.longitude +
                "&destination=" + dest.latitude+","+ dest.longitude+
                "&mode=walking"+
                "&key=" + map.getString(R.string.google_maps_key);
    }

    //extract and draw route from here
    //update and invalidate from here
    public void callAPI(String url){//TODO rename to drawroute api or something
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(map);

//        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DataParser dataParser = new DataParser();
                        // Invokes the thread for parsing the JSON data
                        map.drawRoute(dataParser.parse(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Call API ERROR");
                        //TODO be more descriptive when no network connected
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void SearchOneBuilding(String textIn){
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?"+
                "input="+ textIn +
                "&inputtype=textquery"+
                //South west| North South
                "&locationbias=rectangle"+"-93.65572600000002,42.0360138|-122.0840042,37.4219657"+
                "&fields=name,formatted_address,geometry"+
                "&key=" + map.getString(R.string.google_maps_key);
        System.out.println(url);
        RequestQueue queue = Volley.newRequestQueue(map);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DataParser dataParser = new DataParser();
                        // Invokes the thread for parsing the JSON data
                        map.addMarkerandCallApi(dataParser.parseSearchName(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Call API ERROR");
                        //TODO be more descriptive when no network connected
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    protected void SearchBetweenBuildings(String textIn, String textFrom){
        RequestQueue queue = Volley.newRequestQueue(map);

        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?"+
                "input="+ textFrom +
                "&inputtype=textquery"+
                //South west| North South
                "&locationbias=rectangle"+"-93.65572600000002,42.0360138|-122.0840042,37.4219657"+
                "&fields=name,formatted_address,geometry"+
                "&key=" + map.getString(R.string.google_maps_key);
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DataParser dataParser = new DataParser();
                        // Invokes the thread for parsing the JSON data
                        map.routeBetweenMarkers(dataParser.parseSearchName(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Call API ERROR");
                        //TODO be more descriptive when no network connected
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?"+
                "input="+ textIn +
                "&inputtype=textquery"+
                //South west| North South
                "&locationbias=rectangle"+"-93.65572600000002,42.0360138|-122.0840042,37.4219657"+
                "&fields=name,formatted_address,geometry"+
                "&key=" + map.getString(R.string.google_maps_key);
        System.out.println(url);

        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DataParser dataParser = new DataParser();
                        // Invokes the thread for parsing the JSON data
                        map.routeBetweenMarkers(dataParser.parseSearchName(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Call API ERROR");
                        //TODO be more descriptive when no network connected
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        map.isInStart = true;
    }


    class DataParser {
        LatLng parseSearchName(JSONObject jObject){
            double lat,lng;

            JSONArray jRoutes;
            JSONObject geometry;
            JSONObject coord;
            try {
                jRoutes = jObject.getJSONArray("candidates");
                if(jRoutes.length()>0){
                    geometry = ((JSONObject)jRoutes.get(0)).getJSONObject("geometry");
                    coord = geometry.getJSONObject("location");
                    lat = coord.getDouble("lat");
                    lng = coord.getDouble("lng");
                    //TODO Create own variables
                    map.fromAddr = map.destAddr;
                    map.destAddr = ((JSONObject)jRoutes.get(0)).getString("formatted_address");
                    return new LatLng(lat,lng);
                }
                throw new IllegalArgumentException("The building search didnt work");
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }
            return null;
        }

        List<List<HashMap<String,String>>> parse(JSONObject jObject){
            List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;
            try {
                jRoutes = jObject.getJSONArray("routes");
                // Traversing all routes
                for(int i=0;i<jRoutes.length();i++){
                    jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();
                    // Traversing all legs
                    for(int j=0;j<jLegs.length();j++){
                        String time = ( (JSONObject)jLegs.get(j)).getJSONObject("duration").getString("text");
                        map.destAddr = ( (JSONObject)jLegs.get(j)).getString("end_address");
                        //TODO fix errors when hours
                        String locName = map.destAddr.split(",")[0];
                        map.toolbar.setTitle(time.split(" ")[0] +" minute walk to "+locName);
                        //Snackbar.make(findViewById(R.id.map), "Caution walking is in Beta", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
                        // Traversing all steps
                        for(int k=0;k<jSteps.length();k++){
                            String polyline = "";
                            polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);
                            // Traversing all points
                            for(int l=0;l<list.size();l++){
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude) );
                                hm.put("lng", Double.toString((list.get(l)).longitude) );
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
            }
            return routes;
        }
        /**
         * Method to decode polyline points
         * */
        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;
            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;
                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;
                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }
}
