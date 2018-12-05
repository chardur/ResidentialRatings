package com.resrater.residentialratings;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.resrater.residentialratings.models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetAddressFragment extends Fragment {

    private View root;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private LatLng homeAddress;
    private Button btnSaveAddress;
    private SetAddressInterface mCallBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference newUserRef;

    interface SetAddressInterface{
       void setMapHomeAddress(LatLng address);
       void showMap();
    }

    public SetAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_set_address, container, false);

        // set title and close keyboard
        getActivity().setTitle(getString(R.string.titleSetHomeAddress));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // setup the places autocomplete fragment
        autocompleteFragment = new SupportPlaceAutocompleteFragment();
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction()
                .replace(R.id.place_autocomplete_fragment, autocompleteFragment)
                .commit();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        newUserRef = db.collection("users").document();

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (SetAddressInterface) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + getString(R.string.mustUseSetAddressInterface));
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                homeAddress = place.getLatLng();

            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), R.string.toastErrorOccured,
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveAddress = (Button) root.findViewById(R.id.btnSaveAddress);
        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (homeAddress != null && mAuth.getCurrentUser() != null) {

                    //create a new user
                    User newUser = new User();
                    newUser.setUserID(mAuth.getCurrentUser().getUid());
                    newUser.setEmail(mAuth.getCurrentUser().getEmail());
                    newUser.setHomeAddress(new GeoPoint(homeAddress.latitude, homeAddress.longitude));

                    // insert the new user in firestore
                    newUserRef.set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mCallBack.setMapHomeAddress(homeAddress);
                                mCallBack.showMap();
                            }else{
                                Toast.makeText(getActivity(), R.string.toastFailedPleaseTryAgain,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), R.string.toastYouMustSelectHome,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
