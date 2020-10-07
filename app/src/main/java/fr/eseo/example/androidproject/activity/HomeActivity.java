package fr.eseo.example.androidproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fr.eseo.example.androidproject.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}