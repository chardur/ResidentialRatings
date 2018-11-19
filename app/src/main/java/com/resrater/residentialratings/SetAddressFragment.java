package com.resrater.residentialratings;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SetAddressFragment extends Fragment {

    private View root;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private LatLng homeAddress;
    private Button btnSaveAddress;
    private SetAddressInterface mCallBack;

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
        getActivity().setTitle("Set your home address:");
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

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (SetAddressInterface) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must use SetAddress interface");
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
                Toast.makeText(getActivity(), "An error occurred, please try again",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveAddress = (Button) root.findViewById(R.id.btnSaveAddress);
        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add home address to database
                mCallBack.setMapHomeAddress(homeAddress);
                mCallBack.showMap();
            }
        });
    }
}
