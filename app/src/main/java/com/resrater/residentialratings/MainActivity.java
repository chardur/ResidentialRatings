package com.resrater.residentialratings;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements MapsFragment.mapsInterface,
LoginFragment.loginInterface, RegisterFragment.registerInterface, SetAddressFragment.SetAddressInterface{

    private FragmentManager fm;
    private LoginFragment loginFrag;
    private RegisterFragment registerFrag;
    private MapsFragment mapsFrag;
    private SetAddressFragment setAddressFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginFrag = new LoginFragment();
        mapsFrag = new MapsFragment();
        setAddressFrag = new SetAddressFragment();

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contentMain, loginFrag,"loginFrag")
                .commit();
    }

    @Override
    public void showMapClickDialog(Address selectedAddress) {
        MapClickDialogFragment mapClickDialogFrag = new MapClickDialogFragment();
        mapClickDialogFrag.setCancelable(true);
        mapClickDialogFrag.show(getSupportFragmentManager(), "mapClickDialog");
        mapClickDialogFrag.setSelectedAddress(selectedAddress);
        //mapClickDialogFrag.setFeedbackList();
    }

    @Override
    public void signUpClicked() {
        registerFrag = new RegisterFragment();
        fm.beginTransaction()
                .replace(R.id.contentMain, registerFrag, "registerFrag")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showMap() {
        fm.beginTransaction()
                .replace(R.id.contentMain, mapsFrag,"mapsFrag")
                .addToBackStack(null)
                .commit();
    }

    public void showSetAddressFrag(){
        fm.beginTransaction()
                .replace(R.id.contentMain, setAddressFrag,"setAddressFrag")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setMapHomeAddress(LatLng address) {
        mapsFrag.setHomeAddress(address);
    }
}
