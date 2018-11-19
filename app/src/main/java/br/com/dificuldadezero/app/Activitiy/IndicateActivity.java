package br.com.dificuldadezero.app.Activitiy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import br.com.dificuldadezero.app.R;

public class IndicateActivity extends AppCompatActivity {

    EditText nome;
    EditText local;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nome = findViewById(R.id.editTextName);
        local = findViewById(R.id.editTextPlaceName);
        message = findViewById(R.id.editTextMessage);

    }

    public void sendMail (View view){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","ecobusca.app@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Indicação de novo local] " + nome.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "local: " + local.getText().toString() + "\n" + message.getText().toString());
        startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

}
