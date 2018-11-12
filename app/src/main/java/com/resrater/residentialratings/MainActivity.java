package com.resrater.residentialratings;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements MapsFragment.mapsInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.fragment_maps);
                MapsFragment mapsFragment = new MapsFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.map, mapsFragment,"mapsFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void showMapClickDialog() {
        MapClickDialogFragment mapClickDialogFrag = new MapClickDialogFragment();
        mapClickDialogFrag.setCancelable(true);
        mapClickDialogFrag.show(getSupportFragmentManager(), "mapClickDialog");
    }
}
