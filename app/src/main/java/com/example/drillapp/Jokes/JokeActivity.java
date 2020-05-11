package com.example.drillapp.Jokes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.drillapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JokeActivity extends AppCompatActivity {

    private String url = "http://api.icndb.com/jokes/random/15";
    ArrayList<Joke> datamodelArrayList;
    private JokeListAdapter jokeListAdapter;
    ListView lv;
    private static final String TAG = "Hippulatvinkuu";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        lv = findViewById(R.id.lv);
        retrieveJSON();


    }

    private void retrieveJSON() {
        Log.e(TAG,"NOHALOO");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override

            public void onResponse(JSONObject response) {
                Log.e(TAG,"EIHALOO");
                Log.e(TAG, response.toString());
                datamodelArrayList = new ArrayList<>();

                try {
                    JSONArray responseJokes = (JSONArray) response.getJSONArray("value");

                    for (int i = 0; i < responseJokes.length(); i++) {
                        Joke joke = new Joke();

                        JSONObject dataobj = responseJokes.getJSONObject(i);
                        Log.e(TAG, dataobj.getString("joke"));
                        joke.setJokeContent(dataobj.getString("joke"));

                        datamodelArrayList.add(joke);
                    }

                    setupView();
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.e(TAG,"HALOO");

            }
        });
        requestQueue.add(jsonObjectRequest);


    }

    public void setupView() {
        jokeListAdapter = new JokeListAdapter(this, datamodelArrayList);
        lv.setAdapter(jokeListAdapter);
    }
}