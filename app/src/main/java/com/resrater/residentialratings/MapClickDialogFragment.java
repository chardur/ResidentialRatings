package com.resrater.residentialratings;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapClickDialogFragment extends DialogFragment {

    private Button btnGoBack, btnAddRating;
    private View root;
    private android.location.Address selectedAddress;
    private TextView mapClickDialogAddressText;

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

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAddRating:
                        dismiss();
                        break;
                    case R.id.btnGoBack:
                        dismiss();
                        break;
                }
            }
        };

        btnGoBack.setOnClickListener(buttonListener);
        btnAddRating.setOnClickListener(buttonListener);

        mapClickDialogAddressText = (TextView) root.findViewById(R.id.mapClickDialogAddressText);
        if (selectedAddress != null){
            mapClickDialogAddressText.setText(selectedAddress.getFeatureName() +" "+ selectedAddress.getThoroughfare());
        }

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    public void setSelectedAddress(Address address) {
        selectedAddress = address;
    }

/*    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Map click dialog fragment")
                .setPositiveButton("Add Your Rating", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // add logic
                    }
                })
                .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        return builder.create();
    }*/



}
