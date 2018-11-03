package br.com.dificuldadezero.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mClient;


    private List<Ponto> pontos_oleo_cozinha = readCSV("ecopontos");  // lista com os pontos do arquivo csv que o aplicativo mostrará no mapa

    public MainActivity() throws IOException {


    }
    /*private List<Ponto> pontos_pilhas = readCSV();
    private List<Ponto> ecopontos = readCSV();
    private List<Ponto> lixo_eletronico = readCSV();
    private List<Ponto> pev = readCSV();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openMainMenu (View view){

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("connection","API Client Connection Successful!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("connection","API Client Connection suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("connection","API Client Connection failed!");
    }


    public List<Ponto> readCSV(String arq) throws IOException {

        List<Ponto> pontos = new ArrayList<>();
        Context context = getApplicationContext();

        Resources res = context.getResources();
        InputStream is = getResources().openRawResource(res.getIdentifier(arq,"raw", context.getPackageName()));

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("ASCII")));

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
