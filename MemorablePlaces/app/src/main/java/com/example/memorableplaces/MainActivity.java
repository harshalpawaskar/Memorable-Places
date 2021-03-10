package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<LatLng> locations;
    static ArrayList<String> places;
    static ArrayAdapter arrayAdapter;
    static SharedPreferences sharedPreferences;

    public void AddNewPlace(View view)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);

        ListView listView = (ListView) findViewById(R.id.listView);
        places = new ArrayList<String>();
        locations = new ArrayList<LatLng>();
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Longs",ObjectSerializer.serialize(new ArrayList<String>())));
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(places.size()>0 && longitudes.size()>0 && latitudes.size()>0) {
            if (places.size() == latitudes.size() && latitudes.size() == longitudes.size()) {
                for(int i=0;i<latitudes.size();i++)
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));
            }
        }
        else {
            places.add("Add a new place...");
            locations.add(new LatLng(0,0));
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,places);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putStringArrayListExtra("Places",places);
                intent.putExtra("placeNumber",i);
                startActivity(intent);
            }
        });
    }
}