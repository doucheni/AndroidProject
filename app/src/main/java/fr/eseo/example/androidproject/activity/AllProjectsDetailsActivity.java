package fr.eseo.example.androidproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.JuryProjectCommAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.PosterFragment;
import fr.eseo.example.androidproject.fragments.ProjectDetailFragment;


public class AllProjectsDetailsActivity extends AppCompatActivity {

    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_JURY = "jury";

    JuryProjectCommAsyncTask juryProjectCommAsyncTask;

    private SSLSocketFactory sslSocketFactory;
    private ProgressDialog progressDialog;

    private String token;
    private String username;
    private ProjectModel project;

    private Button buttonPoster;
    private Button buttonDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_details_comm);

        Intent intent = getIntent();
        token = intent.getStringExtra(ARG_TOKEN);
        project = (ProjectModel) intent.getSerializableExtra(ARG_PROJECT);
        username = intent.getStringExtra(ARG_USERNAME);
        sslSocketFactory = Utils.configureSSLContext(this.getApplicationContext()).getSocketFactory();

        buttonPoster = findViewById(R.id.button_poster);
        buttonDetails = findViewById(R.id.button_details);

        buttonPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = PosterFragment.newInstance(project, username, token);
                ft.add(R.id.poster_container, fragment, "poster");
                ft.commit();
            }
        });

        buttonDetails.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("detail", project.getProjectDescription());
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ProjectDetailFragment.newInstance(project);
        ft.add(R.id.project_detail_container,fragment, "project");
        ft.commit();

    }

    public void downloadImage(Bitmap bitmap){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int newWidth = size.x;

        int currentWidth = bitmap.getWidth();
        int currentHeight = bitmap.getHeight();
        float ratio = currentHeight / currentWidth;

        int newHeight = Math.round(ratio * newWidth);

        Bitmap resized = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        ImageView posterProject = new ImageView(AllProjectsDetailsActivity.this);
        posterProject.setImageBitmap(resized);
        this.progressDialog.dismiss();
    }

}