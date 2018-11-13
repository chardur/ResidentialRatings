package com.resrater.residentialratings;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private View root;
    private mapsInterface mCallBack;
    private SupportMapFragment mapFragment;
    private static final float DEFAULT_ZOOM = 19;
    private GoogleMap mMap;
    private LatLng latLng, home;
    private Marker marker, homeMarker;
    Geocoder geocoder;
    private android.location.Address selectedAddress;

    public MapsFragment() {

    }
    public interface mapsInterface {
        public void showMapClickDialog(Address selectedAddress);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentManager fm = getChildFragmentManager();

        // this was added to convert mapActivity to mapFragment
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }

        return root = inflater.inflate(R.layout.fragment_maps, container, false);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (mapsInterface) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() +
                    "must use maps interface");
        }

    }

    // Manipulates the map once available.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // move the map to the users home and add a marker
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(home , DEFAULT_ZOOM) );
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

                selectedAddress = addresses.get(0);
                if (selectedAddress != null) {
                    // display rating dialog
                    mCallBack.showMapClickDialog(selectedAddress);
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

    public void setSupportFragment( SupportMapFragment fragment){
        mapFragment = fragment;
    }
}
