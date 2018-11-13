package br.com.dificuldadezero.app.Activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.dificuldadezero.app.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void openSearchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void openInfographActivity(View view) {
        Intent intent = new Intent(this, InfographActivity.class);
        startActivity(intent);
    }

    public void openIndicateActivity(View view) {
        Intent intent = new Intent(this, IndicateActivity.class);
        startActivity(intent);
    }


}
