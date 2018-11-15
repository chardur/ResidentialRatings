package com.resrater.residentialratings;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private View root;
    private TextInputEditText usernameTextInput, passwordTextInput;
    private Button btnLogin, btnSignup;
    private String username, password;
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

        getActivity().setTitle("Login below:");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_login, container, false);
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
    public void onResume() {
        super.onResume();

        saveTextChanges();
        listenForClicks();

    }

    private void listenForClicks() {
        btnLogin = (Button) root.findViewById(R.id.btnLogin);
        btnSignup = (Button) root.findViewById(R.id.btnSignup);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnLogin:
                        // TODO add firebase login
                        mCallBack.showMap();
                        break;
                    case R.id.btnSignup:
                        mCallBack.signUpClicked();
                        break;
                }
            }
        };

        btnLogin.setOnClickListener(buttonListener);
        btnSignup.setOnClickListener(buttonListener);

    }

    private void saveTextChanges() {
        usernameTextInput = (TextInputEditText) root.findViewById(R.id.userName);
        passwordTextInput = (TextInputEditText) root.findViewById(R.id.password);

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
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        usernameTextInput.addTextChangedListener(textWatcher);
        passwordTextInput.addTextChangedListener(textWatcher);
        System.out.println(username + password);
    }
}
