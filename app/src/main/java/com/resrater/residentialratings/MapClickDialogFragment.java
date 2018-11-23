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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.resrater.residentialratings.models.Rating;


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
        if (selectedAddress != null){
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
                        //add new rating
                        addRating();
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

    private void addRating() {

        //create the rating
        Rating newRating = new Rating();
        newRating.setScore((int) addRatingBar.getRating());
        newRating.setAddress(selectedAddress.getAddressLine(0));
        newRating.setMapLocation(new GeoPoint(selectedAddress.getLatitude(),
                selectedAddress.getLongitude()));
        newRating.setFeedback(feedbackText.getText().toString());
        newRating.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // add rating to the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newRatingRef = db.collection("ratings").document();
        newRatingRef.set(newRating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Rating added!",
                            Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Failed, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setSelectedAddress(Address address) {
        selectedAddress = address;
    }

}
