package com.resrater.residentialratings;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private View root;
    private SupportMapFragment mapFragment;
    private static final float DEFAULT_ZOOM = 19;
    private GoogleMap mMap;
    private LatLng latLng, home;
    private Marker marker, homeMarker;
    Geocoder geocoder;

    public MapsActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentManager fm = getChildFragmentManager();

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }



        return root = inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment.getMapAsync(this);

        // geocoder turns a latlng into a human address
        geocoder = new Geocoder(this.getContext(), Locale.US);

        // start the map at the users home
        home = new LatLng(40.647360, -112.306890);

    }

    // Manipulates the map once available.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // move the map to the users home and add a marker
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(home , 19.0f) );
        homeMarker = mMap.addMarker(new MarkerOptions().position(home).title("Home").draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_blue)));

        // listener for user to select locations on the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //save current location
                latLng = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);
                if (address != null) {
                    Toast.makeText(MapsActivity.this.getContext(), address.getFeatureName() +" "
                            + address.getThoroughfare() +" " +address.getPostalCode(), Toast.LENGTH_SHORT).show();
                }

                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }

                //place marker where user just clicked
                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker"));

            }
        });

    }
}
