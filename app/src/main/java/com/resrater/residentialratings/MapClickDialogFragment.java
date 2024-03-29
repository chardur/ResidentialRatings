package com.resrater.residentialratings;


import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.resrater.residentialratings.models.Rating;
import com.resrater.residentialratings.models.Residence;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapClickDialogFragment extends DialogFragment {

    private Button btnGoBack, btnAddRating;
    private View root;
    private android.location.Address selectedAddress;
    private TextView mapClickDialogAddressText, currentRatingText;
    private RatingBar addRatingBar, mapSelectionRatingBar;
    private TextInputEditText feedbackText;
    private List<String> feedbackList;
    private ListView feedbackListView;
    private ArrayAdapter<String> adapter;
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
        feedbackListView = (ListView) root.findViewById(R.id.feedbackList);
        currentRatingText = (TextView) root.findViewById(R.id.currentRatingText);

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

        db = FirebaseFirestore.getInstance();
        feedbackList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, feedbackList);
        feedbackListView.setAdapter(adapter);
        setFeedbackList();
        setRatingBarValue();

    }

    private Task<Void> addRating(final Residence residence) {

        //create the rating
        final Rating newRating = new Rating();
        newRating.setScore((int) addRatingBar.getRating());
        newRating.setFeedback(feedbackText.getText().toString());
        newRating.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final DocumentReference newRatingRef = db.collection("residence")
                .document(residence.getAddress()).collection("ratings").document();

        // In a transaction, add the new rating and update the aggregate totals
        return db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                // Compute new number of ratings
                int newNumRatings = residence.getNumRatings() + 1;

                // Compute new average rating
                double oldRatingTotal = residence.getAvgRating() * residence.getNumRatings();
                double newAvgRating = (oldRatingTotal + newRating.getScore()) / newNumRatings;

                // Set new residence info
                residence.setNumRatings(newNumRatings);
                residence.setAvgRating(newAvgRating);

                // Update residence
                transaction.update(db.collection("residence")
                        .document(residence.getAddress()), "avgRating", residence.getAvgRating());

                transaction.update(db.collection("residence")
                        .document(residence.getAddress()), "numRatings", residence.getNumRatings());

                // add the rating
                transaction.set(newRatingRef, newRating);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), R.string.toastRatingAdded,
                        Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
                Toast.makeText(getActivity(), R.string.toastFailedToAddRating,
                        Toast.LENGTH_SHORT).show();
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

                    DocumentReference newResDocRef = db.collection("residence").document(newResidence.getAddress());
                    newResDocRef.set(newResidence).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                addRating(newResidence);
                            } else {
                                Toast.makeText(getActivity(), R.string.toastFailedToAddResidence,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.toastFailedToDetermine,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setSelectedAddress(Address address) {
        selectedAddress = address;
    }

    public void setRatingBarValue(){
        if (selectedAddress != null){
            DocumentReference ratingBarRef = db.collection("residence")
                    .document(selectedAddress.getAddressLine(0));

            ratingBarRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        Residence residence = document.toObject(Residence.class);
                        if (residence != null) {
                            mapSelectionRatingBar.setRating(Float.parseFloat(String.valueOf(residence.getAvgRating())));
                            currentRatingText.setText(getString(R.string.currentRating) + residence.getAvgRating());
                        }
                    }else{
                        Toast.makeText(getActivity(), R.string.toastFailedToGetRatingBar,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public void setFeedbackList(){
        if (selectedAddress != null){

            CollectionReference ratingRef = db.collection("residence")
                    .document(selectedAddress.getAddressLine(0))
                    .collection("ratings");

            ratingRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Rating rating = document.toObject(Rating.class);
                            if (!rating.getFeedback().isEmpty() && rating.getFeedback() != null) {
                                feedbackList.add(rating.getFeedback());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }else{
                        Toast.makeText(getActivity(), R.string.toastFailedToGetFeedback,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}
