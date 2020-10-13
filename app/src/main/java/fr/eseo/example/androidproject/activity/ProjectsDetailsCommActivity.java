package fr.eseo.example.androidproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.JuryProjectCommAsyncTask;
import fr.eseo.example.androidproject.AsynchroneTasks.PosterAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.ProjectDetailFragment;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.room.entities.Project;

public class ProjectsDetailsCommActivity extends AppCompatActivity {

    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_JURY = "jury";

    JuryProjectCommAsyncTask juryProjectCommAsyncTask;

    private SSLSocketFactory sslSocketFactory;
    private ProgressDialog progressDialog;
    private LinearLayout posterContainer;

    private String token;
    private String username;
    private ProjectModel project;
    private JuryModel juryModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_details_comm);

        Intent intent = getIntent();
        token = intent.getStringExtra(ARG_TOKEN);
        project = (ProjectModel) intent.getSerializableExtra(ARG_PROJECT);
        username = intent.getStringExtra(ARG_USERNAME);
        posterContainer = findViewById(R.id.posterContainer);
        sslSocketFactory = Utils.configureSSLContext(this.getApplicationContext()).getSocketFactory();

        juryProjectCommAsyncTask = new JuryProjectCommAsyncTask(project, sslSocketFactory, this);
        this.progressDialog = ProgressDialog.show(ProjectsDetailsCommActivity.this,"Loading", "Please wait ...", true);
        juryProjectCommAsyncTask.execute(Utils.buildUrlForLIJUR(username, token),"GET");

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

        ImageView posterProject = new ImageView(ProjectsDetailsCommActivity.this);
        posterProject.setImageBitmap(resized);
        posterContainer.addView(posterProject);
        this.progressDialog.dismiss();
    }

    public void updateActivityViews(JuryModel juryModel){
        Bitmap posterBM = null;
        Log.d("poster", project.getProjectPoster());
        if(!this.project.getProjectPoster().equals("")){
            if(this.project.getProjectPoster() != null){
                try {
                    byte[] decodedString = project.getProjectPoster().getBytes("UTF-8");
                    posterBM = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                } catch (IllegalArgumentException | IOException e) {
                    System.out.println("Error decoding:" + e.getMessage() );
                }
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int newWidth = size.x;
                int currentWidth = posterBM.getWidth();
                int currentHeight = posterBM.getHeight();
                float ratio = currentHeight / currentWidth;
                int newHeight = Math.round(ratio * newWidth);
                Bitmap resized = Bitmap.createScaledBitmap(posterBM, newWidth, newHeight, true);
                ImageView posterProject = new ImageView(ProjectsDetailsCommActivity.this);
                posterProject.setImageBitmap(resized);
                posterContainer.addView(posterProject);
            }
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ProjectDetailFragment.newInstance(project, juryModel);
        ft.add(R.id.project_detail_container,fragment, "project");
        ft.commit();
        this.progressDialog.dismiss();
    }
}

