package br.com.dificuldadezero.app.Activitiy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;

import static android.support.constraint.Constraints.TAG;

import br.com.dificuldadezero.app.R;


public class SearchActivity extends AppCompatActivity implements LocationListener {

    private Spinner spinner;
    private RadioButton radioButtonDoacao;
    private RadioButton radioButtonDescarte;
    private String[] donationMaterials;
    private String[] thrashMaterials;
    private SeekBar seekBar;

    LocationManager locationManager;
    String provider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = findViewById(R.id.spinnerMateriais);
        radioButtonDescarte = findViewById(R.id.radioButtonDescarte);
        radioButtonDoacao = findViewById(R.id.radioButtonDoacao);
        seekBar = findViewById(R.id.seekBarRaioDistancia);

        String[] donationMaterials = {
                "Roupas",
                "Brinquedos",
                "Móveis",
                "Eletrônicos",
                "Eletrodomésticos",
                "Livros"
        };
        this.donationMaterials = donationMaterials;

        String[] thrashMaterials = {
                "Metal",
                "Papel",
                "Plástico",
                "Vidro",
                "Óleo de cozinha",
                "Entulho",
                "Móveis",
                "Pilhas",
                "Baterias",
                "Computadores",
                "CPUs",
                "Notebooks",
                "Impressoras",
                "Rádios",
                "Celulares",
                "Televisões",
                "Microondas",
                "Liquidificadores"
        };
        this.thrashMaterials = thrashMaterials;
    }

    @SuppressLint("MissingPermission")
    public void openMapActivity(View view) {
        Intent intent = new Intent(this, MapsBaseActivity.class);
        intent.putExtra("donation", radioButtonDoacao.isChecked());
        intent.putExtra("material", spinner.getSelectedItem().toString());

        Location location;

        /**
         * Set GPS Location fetched address
         */
        double latitude;
        double longitude;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        location = locationManager.getLastKnownLocation(provider);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i(TAG, String.format("latitude: %s", latitude));
        Log.i(TAG, String.format("longitude: %s", longitude));
        intent.putExtra("gpsLatitude", latitude);
        intent.putExtra("gpsLongitude", longitude);

        intent.putExtra("maxDistance", seekBar.getProgress());
        startActivity(intent);
    }


    public void selectDonationOrThrash(View view) {
        ArrayAdapter<String> spinnerArrayAdapter;

        if(radioButtonDoacao.isChecked()){
            spinnerArrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item,
                            donationMaterials);
        } else {
            spinnerArrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item,
                            thrashMaterials);
        }

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location permission")
                        .setMessage("Location message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SearchActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        provider = locationManager.getBestProvider(new Criteria(), false);
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onClickUseMyGPS(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }
    }
}
