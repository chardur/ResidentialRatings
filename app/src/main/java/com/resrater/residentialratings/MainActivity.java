package com.resrater.residentialratings;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

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
                .replace(R.id.contentMain, loginFrag,getString(R.string.loginFrag))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showMapClickDialog(Address selectedAddress) {
        MapClickDialogFragment mapClickDialogFrag = new MapClickDialogFragment();
        mapClickDialogFrag.setCancelable(true);
        mapClickDialogFrag.show(getSupportFragmentManager(), getString(R.string.mapClickDialog));
        mapClickDialogFrag.setSelectedAddress(selectedAddress);
    }

    @Override
    public void signUpClicked() {
        registerFrag = new RegisterFragment();
        fm.beginTransaction()
                .replace(R.id.contentMain, registerFrag, getString(R.string.registerFrag))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showMap() {
        fm.beginTransaction()
                .replace(R.id.contentMain, mapsFrag,getString(R.string.mapsFrag))
                .addToBackStack(null)
                .commit();
    }

    public void showSetAddressFrag(){
        fm.beginTransaction()
                .replace(R.id.contentMain, setAddressFrag,getString(R.string.setAddressFrag))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setMapHomeAddress(LatLng address) {
        mapsFrag.setHomeAddress(address);
    }
}
