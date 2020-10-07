package fr.eseo.example.androidproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.eseo.example.androidproject.R;

public class ProjectCommActivity extends AppCompatActivity {

    public static final String PROJECT_ID = "Project_id";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String token = intent.getStringExtra("TOKEN");
        Log.d("token", token);
        setContentView(R.layout.activity_com_projects);
    }
    
}
