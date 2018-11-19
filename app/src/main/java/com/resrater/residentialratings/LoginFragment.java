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
public class LoginFragment extends Fragment implements View.OnClickListener{

    private View root;
    private TextInputEditText emailTextInput, passwordTextInput;
    private Button btnLogin, btnSignup;
    private String email, password;
    private loginInterface mCallBack;

    public interface loginInterface {
        void signUpClicked();
        void showMap();
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().setTitle("Login below:");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        emailTextInput = (TextInputEditText) root.findViewById(R.id.email);
        passwordTextInput = (TextInputEditText) root.findViewById(R.id.password);
        btnLogin = (Button) root.findViewById(R.id.btnLogin);
        btnSignup = (Button) root.findViewById(R.id.btnSignup);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (loginInterface) activity;

        }catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + "must use login interface");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                // TODO add firebase login
                login();
                break;
            case R.id.btnSignup:
                mCallBack.signUpClicked();
                break;
        }
    }

    private void login() {
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

        // TODO add firebase login and get home address
        mCallBack.showMap();
    }


}
