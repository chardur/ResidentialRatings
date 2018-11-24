package com.resrater.residentialratings;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.resrater.residentialratings.models.Rating;
import com.resrater.residentialratings.models.Residence;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapClickDialogFragment extends DialogFragment {

    private Button btnGoBack, btnAddRating;
    private View root;
    private android.location.Address selectedAddress;
    private TextView mapClickDialogAddressText;
    private RatingBar addRatingBar, mapSelectionRatingBar;
    private TextInputEditText feedbackText;
    FirebaseFirestore db;

    public MapClickDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // set window transparent for effect
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        root = inflater.inflate(R.layout.fragment_map_click_dialog, container, false);

        btnAddRating = (Button) root.findViewById(R.id.btnAddRating);
        btnGoBack = (Button) root.findViewById(R.id.btnGoBack);
        addRatingBar = (RatingBar) root.findViewById(R.id.addRatingBar);
        mapSelectionRatingBar = (RatingBar) root.findViewById(R.id.mapSelectionRatingBar);
        feedbackText = (TextInputEditText) root.findViewById(R.id.feedbackText);

        db = FirebaseFirestore.getInstance();

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        mapClickDialogAddressText = (TextView) root.findViewById(R.id.mapClickDialogAddressText);
        if (selectedAddress != null) {
            if (selectedAddress.getFeatureName() != null && selectedAddress.getThoroughfare() != null) {
                mapClickDialogAddressText.setText(selectedAddress.getFeatureName() + " " + selectedAddress.getThoroughfare());
                System.out.println(selectedAddress.getAddressLine(0));
            }
        }

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAddRating:
                        // check if residence exists, then add rating
                        addResidence();
                        break;
                    case R.id.btnGoBack:
                        dismiss();
                        break;
                }
            }
        };

        btnGoBack.setOnClickListener(buttonListener);
        btnAddRating.setOnClickListener(buttonListener);

    }

    private void addRating(Residence residence) {

        //create the rating
        Rating newRating = new Rating();
        newRating.setScore((int) addRatingBar.getRating());
        //newRating.setAddress(selectedAddress.getAddressLine(0));
        //newRating.setMapLocation(new GeoPoint(selectedAddress.getLatitude(), selectedAddress.getLongitude()));
        newRating.setFeedback(feedbackText.getText().toString());
        newRating.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // add rating to the database
        DocumentReference newRatingRef = db.collection("residence")
                .document(residence.getAddress()).collection("ratings").document();
        newRatingRef.set(newRating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Rating added!",
                            Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Failed, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addResidence() {
        DocumentReference residenceDocRef = db.collection("residence")
                .document(selectedAddress.getAddressLine(0));
        residenceDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    // residence exists so add the rating
                    Residence residence = task.getResult().toObject(Residence.class);
                    addRating(residence);
                } else if (task.getResult().exists() == false) {
                    // add the residence to the db, then add the rating
                    final Residence newResidence = new Residence();
                    newResidence.setAddress(selectedAddress.getAddressLine(0));
                    newResidence.setMapLocation(new GeoPoint(selectedAddress.getLatitude(), selectedAddress.getLongitude()));
                    newResidence.setAvgRating(0);
                    newResidence.setNumRatings(0);

                    DocumentReference newResDocRef = db.collection("residence").document();
                    newResDocRef.set(newResidence).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                addRating(newResidence);
                            } else {
                                Toast.makeText(getActivity(), "Failed to add residence, please try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to determine if residence exists, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setSelectedAddress(Address address) {
        selectedAddress = address;
    }

}
