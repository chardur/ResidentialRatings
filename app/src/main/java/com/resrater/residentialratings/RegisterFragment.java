package com.resrater.residentialratings;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View root;
    private TextInputEditText emailTextInput, passwordTextInput;
    private Button btnRegister;
    private String email, password;
    private registerInterface mCallBack;
    private FirebaseAuth mAuth;

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

        getActivity().setTitle(getString(R.string.titleRegisterBelow));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        emailTextInput = (TextInputEditText) root.findViewById(R.id.emailRegister);
        passwordTextInput = (TextInputEditText) root.findViewById(R.id.passwordRegister);
        btnRegister = (Button) root.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (registerInterface) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + getString(R.string.mustUseRegisterInterface));
        }

    }

    @Override
    public void onClick(View v) {
        registerUser();

    }

    private void registerUser() {
        password = passwordTextInput.getText().toString().trim();
        email = emailTextInput.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInput.setError(getString(R.string.resgisterPleaseEnterValidEmail));
            emailTextInput.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordTextInput.setError(getString(R.string.registerPleaseEnterPassword6More));
            passwordTextInput.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), R.string.successfullyRegistered,
                                    Toast.LENGTH_SHORT).show();
                            mCallBack.showSetAddressFrag();
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getActivity(), R.string.alreadyRegistered,
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), R.string.unableToRegister,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

}
