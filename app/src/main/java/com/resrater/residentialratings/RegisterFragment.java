package com.resrater.residentialratings;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private View root;
    private TextInputEditText usernameTextInput, passwordTextInput, homeAddressInput;
    private Button btnRegister;
    private String username, password, homeAddress;
    private registerInterface mCallBack;

    public interface registerInterface {
        void showMap();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Register below:");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (registerInterface) activity;

        }catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + "must use register interface");
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        saveTextChanges();
        listenForClicks();

    }

    private void listenForClicks() {
        btnRegister = (Button) root.findViewById(R.id.btnRegister);


        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnRegister:
                        // TODO add firebase register
                        mCallBack.showMap();
                        break;
                }
            }
        };

        btnRegister.setOnClickListener(buttonListener);

    }

    private void saveTextChanges() {
        usernameTextInput = (TextInputEditText) root.findViewById(R.id.userNameRegister);
        passwordTextInput = (TextInputEditText) root.findViewById(R.id.passwordRegister);
        homeAddressInput = (TextInputEditText) root.findViewById(R.id.homeAddress);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && count > 0){
                    switch (root.getId()) {
                        case R.id.passwordRegister:
                            password = passwordTextInput.getText().toString();
                            break;
                        case R.id.userNameRegister:
                            username = usernameTextInput.getText().toString();
                            break;
                        case R.id.homeAddress:
                            homeAddress = homeAddressInput.getText().toString();
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        usernameTextInput.addTextChangedListener(textWatcher);
        passwordTextInput.addTextChangedListener(textWatcher);
        homeAddressInput.addTextChangedListener(textWatcher);
        System.out.println(username + password +homeAddress);
    }



    }
