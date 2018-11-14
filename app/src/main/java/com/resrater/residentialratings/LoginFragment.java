package com.resrater.residentialratings;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private View root;
    private TextInputEditText usernameTextInput, passwordTextInput;
    private Button btnLogin;
    private TextView registerTextView;
    private String username, password;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        usernameTextInput = (TextInputEditText) root.findViewById(R.id.userName);
        passwordTextInput = (TextInputEditText) root.findViewById(R.id.password);



        if (usernameTextInput.getText() != null){
            username = usernameTextInput.getText().toString();
        }

        if (passwordTextInput.getText() != null){
            password = passwordTextInput.getText().toString();
        }

    }
}
