package fr.eseo.example.androidproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.PseudoJuryModel;

public class VisitorActivity extends AppCompatActivity {

    private PseudoJuryModel pseudoJuryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);



    }

}