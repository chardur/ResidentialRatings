package com.resrater.residentialratings;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.resrater.residentialratings.models.Residence;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mResidenceRef = db.collection("residence");


    public MapsFragment() {

    }

    public interface mapsInterface {
        public void showMapClickDialog(Address selectedAddress);
    }

    @Override
    public void onStart() {
        super.onStart();
        // listen for changes to the residence collection so that new ratings appear on map
        mResidenceRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                //get changes and update
                for (DocumentChange document: queryDocumentSnapshots.getDocumentChanges()){
                    Residence residence = document.getDocument().toObject(Residence.class);
                    // TODO change icon
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(residence.getMapLocation()
                                    .getLatitude(), residence.getMapLocation().getLongitude()))
                            .title(residence.getAddress().substring(0, 10) + "..., Rating: "+ residence.getAvgRating())
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_blue)));
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_maps, container, false);
        getActivity().setTitle("Add / View ratings below:");

        FragmentManager fm = getChildFragmentManager();

        // this was added to convert mapActivity to mapFragment
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment.getMapAsync(this);

        // geocoder turns a latlng into a human address
        geocoder = new Geocoder(this.getContext(), Locale.US);

        // get ratings from database and add them to the map
        addMapRatings();

    }

    private void addMapRatings() {
        mResidenceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document: task.getResult()){
                        Residence residence = document.toObject(Residence.class);
                        // TODO change icon
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(residence.getMapLocation()
                                        .getLatitude(), residence.getMapLocation().getLongitude()))
                                .title(residence.getAddress().substring(0, 10) + "..., Rating: "+ residence.getAvgRating())
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_blue)));
                    }

                }else{
                    Toast.makeText(getActivity(), "Failed to update ratings map",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


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

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // move the map to the users home and add a marker
        if (home != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, DEFAULT_ZOOM));
            homeMarker = mMap.addMarker(new MarkerOptions().position(home).title("Home").draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_blue)));
        }

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
            }
        });

        // listener if user wants to click on marker on map
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng point = marker.getPosition();

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

                return false;
            }
        });

    }

    public void setHomeAddress(LatLng address) {
        home = address;
    }

}
