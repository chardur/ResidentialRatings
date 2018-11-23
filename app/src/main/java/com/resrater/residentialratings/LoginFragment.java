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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.resrater.residentialratings.models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private View root;
    private TextInputEditText emailTextInput, passwordTextInput;
    private Button btnLogin, btnSignup;
    private String email, password;
    private loginInterface mCallBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LatLng homeAddress;

    public interface loginInterface {
        void signUpClicked();
        void showMap();
        void setMapHomeAddress(LatLng address);
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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Successfully logged in!",
                            Toast.LENGTH_SHORT).show();

                    loadMap();
                }else{
                    Toast.makeText(getActivity(), task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadMap(){
        CollectionReference usersRef = db.collection("users");
        Query addressQuery = usersRef.whereEqualTo("userID", mAuth.getCurrentUser().getUid());
        addressQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    User user = new User();
                    for (QueryDocumentSnapshot u: task.getResult()){
                        user = u.toObject(User.class);
                    }
                    homeAddress = new LatLng(user.getHomeAddress().getLatitude(), user.getHomeAddress().getLongitude());
                    mCallBack.setMapHomeAddress(homeAddress);
                    mCallBack.showMap();
                }else{
                    Toast.makeText(getActivity(), "Failed to get home address",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("#################################");
                    System.out.println("Address failed");
                }
            }
        });
    }


}
