package com.example.isuview;

import android.app.ListActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class BusRouteViewController extends AppCompatActivity {
    BusRouteViewModel model;

    public static final String CHANNEL_ID = "com.ISUView.busalerts";

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Bus Alert")
            .setContentText("Not set up yet")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    NotificationManager notificationManager;

    protected void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new BusRouteViewModel(this);
        setContentView(R.layout.activity_bus_route_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setContentText(model.curRoute +" approaching "+model.curStop);
                createNotificationChannel();
                notificationManager.notify(1, builder.build());
                connectWebSocket();
                Snackbar.make(view, "Created Websocket", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String url = "http://10.24.226.172:8080/api/bus/stops?route=" + model.curRoute;
        final ListView times = findViewById(R.id.list_view);
        times.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                 model.curTime = adapter.getItemAtPosition(position).toString();
             }
         });
        model.callAPI(times, url);

        url = "http://10.24.226.172:8080/api/bus/stops?times=" + model.curRoute + "&stop=" + model.curStop;
        final Spinner busStop = findViewById(R.id.spinner2);
        busStop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                AdapterView routeView = view.findViewById(R.id.spinner1);
                // curRoute = routeView.getSelectedItem().toString();
                model.curStop = parent.getItemAtPosition(pos).toString();
                //TODO: Change url based on selected
                model.callAPI(times, "http://10.24.226.172:8080/api/bus/times?route=" + model.curRoute + "&stop=" + model.curStop);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        model.callAPI(busStop, url);

        Spinner busRoute = findViewById(R.id.spinner1);
        busRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                model.curRoute = parent.getItemAtPosition(pos).toString();
                model.callAPI(busStop, "http://10.24.226.172:8080/api/bus/stops?route=" + model.curRoute);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        model.callAPI(busRoute, "http://10.24.226.172:8080/api/bus/routes");
    }


    private void connectWebSocket() {
        URI uri;
        try {
            //TODO change uri
            uri = new URI("ws://coms-309-ss-6.misc.iastate.edu:8080/websocket/bus");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("Websocket", "Opened");
                //mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                mWebSocketClient.send(model.curRoute + "::" + model.curStop);
            }

            @Override
            public void onBinaryReceived(byte[] data) {

            }

            @Override
            public void onPingReceived(byte[] data) {

            }

            @Override
            public void onPongReceived(byte[] data) {

            }

            @Override
            public void onTextReceived(String s) {
                builder.setContentText(s);
                createNotificationChannel();
                notificationManager.notify(1, builder.build());
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        mWebSocketClient.connect();
    }
    protected ArrayAdapter<String> spinAdapter(String[] data){
        ArrayAdapter<String> spinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        return spinAdapt;
    }
    protected ArrayAdapter<String> listAdapter(String[] data){
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        R.layout.timeview,
                        R.id.cheese_name,
                        data
                );
        return adapter;
    }
}
