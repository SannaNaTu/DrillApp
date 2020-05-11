package com.example.drillapp.ContactInformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.drillapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactInformationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecycleAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;

    List<Contact> contactsList;
    String url = "https://digitradecontacs2019.azurewebsites.net/api/contacts";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is test a button", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }


    private void getData() {
        requestQueue = AzureSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        contactsList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                Contact contacts = new Contact();
                                JSONObject contact = response.getJSONObject(i);

                                contacts.setFirstName(contact.getString("firstName"));
                                contacts.setLastName(contact.getString("lastName"));
                                contacts.setEmailAddress(contact.getString("emailAddress"));
                                contacts.setCity(contact.getString("city"));
                                contacts.setStreetAddress(contact.getString("streetAddress"));
                                contacts.setId(contact.getInt("id"));
                                contacts.setPhoneNumber(contact.getInt("phoneNumber"));
                                contacts.setPostalCode(contact.getInt("postalCode"));

                                contactsList.add(contacts);
                                Log.i("Contacts",contacts.toString() );
                            }
                        } catch (Exception e) {
                            //error
                        }
                        SetupRecycler();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Contacts", "Not ok");

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void SetupRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.contactRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new LinearLayoutManager(this);
        adapter = new RecycleAdapter(this, contactsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gridLayout:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                return true;
            case R.id.listLayout:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
