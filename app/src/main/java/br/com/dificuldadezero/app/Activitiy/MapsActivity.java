package br.com.dificuldadezero.app.Activitiy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import br.com.dificuldadezero.app.DTO.Ponto;
import br.com.dificuldadezero.app.R;

import static android.support.constraint.Constraints.TAG;

public class MapsActivity extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mClient;
    private Context context;
    private Boolean donation;
    private String material;
    private String csvFile;
    private double gpsLatitude;
    private double gpsLongitude;
    private int maxDistance;
    GeoDataClient myGeo;
    private boolean placeForCurrentParametersFound = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        context = getContext();
        Bundle arguments = getArguments();
        donation = arguments.getBoolean("donation");
        material = arguments.getString("material").toLowerCase();
        gpsLatitude = arguments.getDouble("gpsLatitude");
        gpsLongitude = arguments.getDouble("gpsLongitude");
        maxDistance = arguments.getInt("maxDistance");
        if(donation) csvFile = "donation";
        else csvFile = "discard";
        Log.e(TAG, "CSV file: " + csvFile);
        Log.e(TAG, "Donation: " + donation);
        Log.e(TAG, "Material: " + material);
        Log.i(TAG, "gpsLatitude: " + gpsLatitude);
        Log.i(TAG, "gpsLongitude: " + gpsLongitude);
        /*mClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();*/
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
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        final List<Ponto> pontos = findPoints();
        final List<String> pointsIds = new ArrayList<>();
        for(Ponto ponto: pontos){
            pointsIds.add(ponto.getId());
        }
        int height = 150;
        int width = 130;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.place);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mMap.addMarker(new MarkerOptions().position(new LatLng(gpsLatitude, gpsLongitude)).title("Origem").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        if(pontos.size() == 0){
            Toast toast = Toast.makeText(getActivity(), "Não foi encontrado nenhum ponto que atenda aos parâmetros passados", Toast.LENGTH_LONG);
            toast.show();
        } else {
            for(int i = 0; i < pointsIds.size(); i=i+20) {
                final int index = i;
                myGeo.getPlaceById(pointsIds.subList(i,pointsIds.size()).toArray(new String[0])).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                        if (task.isSuccessful()) {
                            PlaceBufferResponse places = task.getResult();
                            for(int j = 0; j < places.getCount(); j++){
                                Place myPlace = places.get(j);
                                Log.i(TAG, "Place found: " + myPlace.getName());
                                LatLng place = myPlace.getLatLng();
                                String address = String.valueOf(myPlace.getAddress());
                                String phone = String.valueOf(myPlace.getPhoneNumber());
                                String rating = String.valueOf(myPlace.getRating());
                                if (rating.equals("-1.0")) rating = "indisponível";
                                mMap.addMarker(new MarkerOptions()
                                        .position(place)
                                        .title(String.valueOf(pontos.get(index).getName()))
                                        .snippet(
                                                "Endereço: " + address + "\n" +
                                                        "Telefone: " + phone + "\n" +
                                                        "Avaliação: " + rating
                                        ));
                            }
                            places.release();
                        } else {
                            Log.e(TAG, "Place not found.");
                        }
                    }
                });
            }
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(gpsLatitude, gpsLongitude))
                .zoom(12)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        setPlaceForCurrentParametersFound(true);

    }

    public void setPlaceForCurrentParametersFound(boolean found){
        this.placeForCurrentParametersFound = found;
    }

    public boolean isPlaceForCurrentParametersFound() {
        return placeForCurrentParametersFound;
    }

    public List<Ponto> findPoints(){
        List<Ponto> csvPoints = new ArrayList<>();
        try {
            csvPoints = readCSV(csvFile);
        } catch (Exception e){
            e.printStackTrace();
        }
        List<Ponto> filteredByMaterial = filterByMaterial(csvPoints);
        Log.e(TAG, "gpsLatitude no MapsActivity: " + gpsLatitude);
        Log.e(TAG, "maxDistance no MapsActivity: " + maxDistance);
        List<Ponto> filteredPoints = filterByDistance(filteredByMaterial);
        return filteredPoints;
    }

    public List<Ponto> filterByMaterial(List<Ponto> pointList){
        List<Ponto> filteredPoints = new ArrayList<>();
        for(int i = 0; i < pointList.size(); i++){
            Ponto currentPoint = pointList.get(i);
            String materialsString = currentPoint.getMaterial().toLowerCase();
            String[] materialsOfThisLocal = materialsString.split(";");
            Log.e(TAG, materialsOfThisLocal.toString());
            boolean placeHasMaterial = false;
            for(int j = 0; j < materialsOfThisLocal.length; j++){
                if(materialsOfThisLocal[j].equals(material)) placeHasMaterial = true;
            }
            if(placeHasMaterial) filteredPoints.add(currentPoint);
        }
        return filteredPoints;
    }

    public List<Ponto> filterByDistance(List<Ponto> pointList){
        int i = 0;
        while(i < pointList.size()){

            double distance = distance(
                    gpsLatitude, gpsLongitude,
                    pointList.get(i).getLat(), pointList.get(i).getLongi(),
                    "K");
            Log.e(TAG, "distance: " + distance);
            if(distance > maxDistance) {
                Log.e(TAG, "ponto removido: " + pointList.get(i).getName());
                pointList.remove(i);
                continue;
            }
            i++;
        }
        return pointList;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public List<Ponto> readCSV(String arq) throws IOException {

        List<Ponto> pontos = new ArrayList<>();
        Context context = getContext();

        Resources res = context.getResources();
        InputStream is = getResources().openRawResource(res.getIdentifier(arq,"raw", context.getPackageName()));

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        reader.readLine();  // pula o header

        while((line = reader.readLine()) != null){

            String[] splittedLine = line.split(",");


            Ponto novoPonto = new Ponto(Double.parseDouble(splittedLine[0]),Double.parseDouble(splittedLine[1]), splittedLine[2], splittedLine[3], splittedLine[4]);

            pontos.add(novoPonto);
        }

        return pontos;
    }
}
