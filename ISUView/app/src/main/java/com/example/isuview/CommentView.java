package com.example.isuview;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GestureDetectorCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import tech.gusavila92.websocketclient.WebSocketClient;
public class CommentView extends AppCompatActivity {
    NotificationManager notificationManager;
    public static final String CHANNEL_ID = "com.ISUView.commentalerts";
    String msg = "";
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("New Comment")
            .setContentText("Not set up yet")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    private void createNotificationChannel() {
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

    private String building = "gilman";

    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
                break;
            case MotionEvent.ACTION_MOVE :
                break;
            case MotionEvent.ACTION_UP :
                break;
            default :

        }
        return true;
    }
    ArrayList<String> comments = new ArrayList<String>();
    ArrayList<String> authors = new ArrayList<String>();
    ArrayList<Integer> images = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        building = getIntent().getStringExtra("building");
        System.out.println(building);
        GrabComments(building);
        final EditText editText = findViewById(R.id.commentInput);
        builder.setContentText("New Comment");
        createNotificationChannel();
        notificationManager.notify(1, builder.build());
        connectWebSocket();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == 6){
                    ListView list = (ListView) findViewById(R.id.commentList);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, comments);

                    // Here, you set the data in your ListView
                    list.setAdapter(adapter);
                    // this line adds the data of your EditText and puts in your array
                    CreateComment(editText.getText().toString());
                    // next thing you have to do is check if your adapter has changed
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                    editText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    handled = true;
                }
                return handled;
            }
        });


    }
    public void CreateComment(String commentText){

        msg = commentText;
        postAPI(commentText);
        ClearComments();
        GrabComments(building);
    }
    public void ClearComments(){
        comments.clear();
        authors.clear();
        images.clear();
    }
    public ArrayList<String> GrabComments(String buildingId){
        callAPI(buildingId);
        return new ArrayList<String>();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void postAPI(String commentText){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.24.226.172:8080/api/createcomment?userId=0&building=" + building.toLowerCase() + "&commentText=" + commentText;

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            int i =1;
            i++;
        }else{
            url.charAt(1);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userid", "0");

        // Initialize a new JsonArrayRequest instance
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject obj = response;
                            String id = obj.getString("id");
                            System.out.println("RECIEVED: " + id);
                            mWebSocketClient.send(id);
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println(error);
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
    private void callAPI(String buildingId){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.24.226.172:8080/api/grabcomments/" + buildingId.toLowerCase();

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
                        try{
                            for(int i =0; i<response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);
                                String id = obj.getString("id");
                                String comment = obj.getString("commentText");
                                comments.add(comment);
                                authors.add("User #" + id);
                                images.add(R.drawable.common_google_signin_btn_icon_dark);
                                System.out.println(id);
                            }
                        }catch (Exception e){
                            System.out.println("test");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println(error);
                    }
                }
        );
        CustomListAdapter whatever = new CustomListAdapter(this, authors, comments, images);
        ListView listView = (ListView) findViewById(R.id.commentList);
        listView.setAdapter(whatever);
        // next thing you have to do is check if your adapter has changed
        whatever.notifyDataSetChanged();
        listView.refreshDrawableState();
        listView.clearFocus();
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
    @Override
    public void onResume(){
        super.onResume();
        CustomListAdapter whatever = new CustomListAdapter(this, authors, comments, images);
        ListView listView = (ListView) findViewById(R.id.commentList);
        listView.setAdapter(whatever);
        // next thing you have to do is check if your adapter has changed
        whatever.notifyDataSetChanged();
        listView.refreshDrawableState();

    }
    private WebSocketClient mWebSocketClient;
    private void connectWebSocket() {
        URI uri;
        try {
            //TODO change uri
            uri = new URI("ws://coms-309-ss-6.misc.iastate.edu:8080/websocket/comment");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Here");
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("CONNECTED TO SOCKET");
                Log.i("Websocket", "Opened");
                mWebSocketClient.send(msg);
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
                System.out.println("Got something back " + s);
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

    public void Notify(View view) {
        builder.setContentText("New Comment");
        createNotificationChannel();
        notificationManager.notify(1, builder.build());
        connectWebSocket();
        Snackbar.make(view, "Created Websocket", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
}
