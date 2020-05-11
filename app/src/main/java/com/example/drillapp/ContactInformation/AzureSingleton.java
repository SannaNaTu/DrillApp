package com.example.drillapp.ContactInformation;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AzureSingleton {
    private static AzureSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;


    private AzureSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

    }

    public static synchronized AzureSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new AzureSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {

            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
