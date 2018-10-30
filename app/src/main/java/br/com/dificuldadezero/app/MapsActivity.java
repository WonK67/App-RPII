package br.com.dificuldadezero.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class MapsActivity extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mClient;
    GeoDataClient myGeo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        mClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        myGeo = Places.getGeoDataClient(getContext());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*mMap = googleMap;
        //if(doacao)
        //Ponto[] pontos = readCSV(doacao)
        pontos = filterByMaterial(pontos)
        for(int i = 0; i < pontos.length; i++) {
            myGeo.getPlaceById("ChIJV_YJS0JazpQRjDLbjJOp47c").addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                    if (task.isSuccessful()) {
                        PlaceBufferResponse places = task.getResult();
                        Place myPlace = places.get(0);
                        Log.i(TAG, "Place found: " + myPlace.getName());
                        LatLng place = myPlace.getLatLng();
                        mMap.addMarker(new MarkerOptions()
                                .position(place)
                                .title(String.valueOf(myPlace.getName()))
                                .snippet(String.valueOf(myPlace.getRating())));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(place)
                                .zoom(12)
                                .bearing(0)
                                .tilt(0)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        places.release();
                    } else {
                        Log.e(TAG, "Place not found.");
                    }
                }
            });
        }*/

    }

    /*public List<Ponto> filterByMaterial(List<Ponto> pontos, String material){
        List<Ponto> filteredPoints = new ArrayList<>()
        for(int = 0; i < pontos.length; i++){
            Ponto currentPoint = pontos.get(i);
            String[] materialsOfThisLocal = currentPoint.getMaterial().split(",");
            boolean placeHasMaterial = false;
            for(int j = 0; j < materialsOfThisLocal.length; j++){
                if(materialsOfThisLocal[j].equals(material)) placeHasMaterial = true;
            }
            if(placeHasMaterial) filteredPoints.add(currentPoint);
        }
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
