package com.example.yeefang.rentforest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class RoomRentalList extends AppCompatActivity {

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

        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        String[] items = new String[] { "Putrajaya", "Malacca", "Penang","KualaLumpur", "Ipoh", "Terengganu" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Log.v("item", (String) parent.getItemAtPosition(position));

                roomRentalList = new ArrayList<>();
                String location = (String) parent.getItemAtPosition(position);
                new RetrieveUserInput();
                refreshList(location);
                return;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });





        Button goBtn = (Button) findViewById(R.id.btn_go);
        roomRentalList = new ArrayList<>();
        getData();

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomRentalList = new ArrayList<>();
                String location = inputLocation.getText().toString();
                new RetrieveUserInput();
                refreshList(location);
                return;
            }
        });

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /*ItemData[] itemData = {new ItemData(0, "kitty 0", "RM10", "Flat", "123, Jalan Maju, Kuala Lumpur", R.drawable.kitty),
                new ItemData(1, "kitty 2", "RM10", "Flat", "123, Jalan Maju, Kuala Lumpur", R.drawable.kitty2),
                new ItemData(2, "kitty 3", "RM10", "Flat", "123, Jalan Maju, Kuala Lumpur", R.drawable.kitty3),
                new ItemData(3, "kitty 4", "RM10", "Flat", "123, Jalan Maju, Kuala Lumpur", R.drawable.kitty4),
                new ItemData(4, "kitty 5", "RM10", "Flat", "123, Jalan Maju, Kuala Lumpur", R.drawable.kitty5)

        };*/


    }

    private void refreshList(String location) {

            final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, true);
            String url = "https://api.airbnb.com/v2/search_results?client_id=3092nxybyb0otqw18e8nh5nty&locale=en-US&currency=MYR&_format=for_search_results_with_minimal_pricing&_limit=10&_offset=0&fetch_facets=true&guests=1&ib=false&ib_add_photo_flow=true&location="+ location  +"%20MY&min_bathrooms=0&min_bedrooms=0&min_beds=1";

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
        final ProgressDialog loading = ProgressDialog.show(this, "Loading Data", "Please wait...", false, true);
        String url = "https://api.airbnb.com/v2/search_results?client_id=3092nxybyb0otqw18e8nh5nty&locale=en-US&currency=MYR&_format=for_search_results_with_minimal_pricing&_limit=10&_offset=0&fetch_facets=true&guests=1&ib=false&ib_add_photo_flow=true&location="+ "Ampang" +"%20MY&min_bathrooms=0&min_bedrooms=0&min_beds=1";

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
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favourite) {
            Intent i = new Intent(RoomRentalList.this, FavouriteList.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
