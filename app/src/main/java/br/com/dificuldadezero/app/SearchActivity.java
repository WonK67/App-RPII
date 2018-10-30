package br.com.dificuldadezero.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

public class SearchActivity extends AppCompatActivity {

    private Spinner spinner;
    private RadioButton radioButtonDoacao;
    private RadioButton radioButtonDescarte;
    private String[] donationMaterials;
    private String[] thrashMaterials;


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

        String[] donationMaterials = {
                "roupas",
                "brinquedos",
                "móveis",
                "eletrônicos",
                "eletrodomésticos",
                "livros"
        };
        this.donationMaterials = donationMaterials;

        String[] thrashMaterials = {
                "Entulho",
                "Metal",
                "Papel",
                "Plástico",
                "Vidro",
                "Movél",
                "Óleo de cozinha",
                "Pilhas",
                "Lixo eletrônico (exceto pilhas e baterias)"
        };
        this.thrashMaterials = thrashMaterials;
    }

    public void openMapActivity(View view) {
        Intent intent = new Intent(this, MapsBaseActivity.class);
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
}
