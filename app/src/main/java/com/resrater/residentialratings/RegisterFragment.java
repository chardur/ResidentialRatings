package com.resrater.residentialratings;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View root;
    private TextInputEditText emailTextInput, passwordTextInput;
    private Button btnRegister;
    private String email, password;
    private registerInterface mCallBack;

    public interface registerInterface {
        void showMap();
        void showSetAddressFrag();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_register, container, false);

        getActivity().setTitle("Register below:");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        emailTextInput = (TextInputEditText) root.findViewById(R.id.emailRegister);
        passwordTextInput = (TextInputEditText) root.findViewById(R.id.passwordRegister);
        btnRegister = (Button) root.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (registerInterface) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must use register interface");
        }

    }

    @Override
    public void onClick(View v) {
        registerUser();
        // TODO add firebase register

    }

    private void registerUser() {
        password = passwordTextInput.getText().toString().trim();
        email = emailTextInput.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInput.setError("Please enter a valid email address");
            emailTextInput.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordTextInput.setError("Please enter a password with 6 or more characters");
            passwordTextInput.requestFocus();
            return;
        }

        // TODO add firebase register
        mCallBack.showSetAddressFrag();

    }

}
