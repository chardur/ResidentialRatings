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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
                        // TODO add firebase rating
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        hideKeyboard();
                        dismiss();
                        break;
                    case R.id.btnGoBack:
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        dismiss();
                        break;
                }
            }
        };

        btnGoBack.setOnClickListener(buttonListener);
        btnAddRating.setOnClickListener(buttonListener);

        mapClickDialogAddressText = (TextView) root.findViewById(R.id.mapClickDialogAddressText);
        if (selectedAddress != null){
            if (selectedAddress.getFeatureName() != null && selectedAddress.getThoroughfare() != null) {
                mapClickDialogAddressText.setText(selectedAddress.getFeatureName() + " " + selectedAddress.getThoroughfare());
            }
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

    public void hideKeyboard() {

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}
