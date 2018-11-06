package br.com.dificuldadezero.app.Activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.dificuldadezero.app.R;

public class MapsBaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private boolean donation = false;
    private String material;
    private double gpsLatitude;
    private double gpsLongitude;
    private int maxDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        donation = intent.getBooleanExtra("donation", false);
        material = intent.getStringExtra("material");
        gpsLatitude = intent.getDoubleExtra("gpsLatitude", 0);
        gpsLongitude = intent.getDoubleExtra("gpsLongitude", 0);
        maxDistance = intent.getIntExtra("maxDistance", 100);
        MapsActivity fragment = new MapsActivity();
        Bundle arguments = new Bundle();
        arguments.putBoolean("donation", donation);
        arguments.putString("material", material);
        arguments.putDouble("gpsLatitude", gpsLatitude);
        arguments.putDouble("gpsLongitude", gpsLongitude);
        arguments.putInt("maxDistance", maxDistance);
        fragment.setArguments(arguments);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, fragment, "MapsFragment");
        transaction.commitAllowingStateLoss();

    }




}
