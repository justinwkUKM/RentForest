package com.example.yeefang.rentforest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yee Fang on 14/09/2016.
 */
public class RetrieveUserInput extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText inputLocation;
    private List<ItemData> roomRentalList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_rental_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        inputLocation = (EditText) findViewById(R.id.et_location);

        Button goBtn = (Button) findViewById(R.id.btn_go);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveUserInput();
                refreshList();
                return;
            }
        });

        roomRentalList = new ArrayList<>();
        getData();
    }

    private void refreshList() {
        String location = inputLocation.getText().toString();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, true);
        String url = "https://api.airbnb.com/v2/search_results?client_id=3092nxybyb0otqw18e8nh5nty&locale=en-US&currency=MYR&_format=for_search_results_with_minimal_pricing&_limit=10&_offset=0&fetch_facets=true&guests=1&ib=false&ib_add_photo_flow=true&location="+ location + "MY" +"%2C%20CA%2C%20MY&min_bathrooms=0&min_bedrooms=0&min_beds=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    Log.e("Response", response.toString());
                    JSONArray resultArray = response.getJSONArray("search_results");
                    //calling method to parse json array
                    parseData(resultArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to this queue
        requestQueue.add(jsonObjectRequest);
    }

    private void getData() {
        String location = inputLocation.getText().toString();
        final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, true);
        String url = "https://api.airbnb.com/v2/search_results?client_id=3092nxybyb0otqw18e8nh5nty&locale=en-US&currency=MYR&_format=for_search_results_with_minimal_pricing&_limit=10&_offset=0&fetch_facets=true&guests=1&ib=false&ib_add_photo_flow=true&location="+ location + "MY" +"%2C%20CA%2C%20MY&min_bathrooms=0&min_bedrooms=0&min_beds=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    Log.e("Response", response.toString());
                    JSONArray resultArray = response.getJSONArray("search_results");
                    //calling method to parse json array
                    parseData(resultArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to this queue
        requestQueue.add(jsonObjectRequest);
    }

    private void parseData(JSONArray resultArray) {
        for (int i = 0; i < resultArray.length(); i++) {
            ItemData room = new ItemData();
            JSONObject json = null;
            try {
                json = resultArray.getJSONObject(i);

                JSONObject postItem = json.getJSONObject("listing");
                JSONObject postPrice = json.getJSONObject("pricing_quote").getJSONObject("rate");

                room.setImageURL(postItem.getString("picture_url"));
                room.setName(postItem.getString("name"));
                room.setPrice("RM" + postPrice.getString("amount"));
                room.setPropertyType("Type: " + postItem.getString("property_type"));
                room.setPublicAddress("Address: " + "\n" + postItem.getString("public_address"));
                Log.e("ItemData Response(" + resultArray.length() + ")", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            roomRentalList.add(room);
        }
        //Initialize adapter
        adapter = new MyAdapter(roomRentalList, this);

        //Add adapter to recycler view
        recyclerView.setAdapter(adapter);

    }
}
