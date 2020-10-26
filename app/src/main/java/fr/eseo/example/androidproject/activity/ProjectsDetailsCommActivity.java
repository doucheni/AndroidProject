package fr.eseo.example.androidproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.JuryProjectCommAsyncTask;
import fr.eseo.example.androidproject.fragments.JuryProjectDetailsComm;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.IdentityStudentsFragment;
import fr.eseo.example.androidproject.fragments.PosterFragment;
import fr.eseo.example.androidproject.fragments.ProjectDetailFragment;

public class ProjectsDetailsCommActivity extends AppCompatActivity {

    private static final String ARG_PROJECT = "project";
    private static final String ARG_STUDENTS = "students";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_JURY = "jury";

    JuryProjectCommAsyncTask juryProjectCommAsyncTask;
    ProjectsDetailsCommActivity instance;

    private SSLSocketFactory sslSocketFactory;
    private ProgressDialog progressDialog;

    private String token;
    private String username;
    private ProjectModel project;
    private StudentsGroup studentsGroup;

    private Button buttonPoster;
    private Button buttonMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_details_comm);

        Intent intent = getIntent();
        token = intent.getStringExtra(ARG_TOKEN);
        project = (ProjectModel) intent.getSerializableExtra(ARG_PROJECT);
        username = intent.getStringExtra(ARG_USERNAME);
        studentsGroup = (StudentsGroup) intent.getSerializableExtra(ARG_STUDENTS);
        sslSocketFactory = Utils.configureSSLContext(this.getApplicationContext()).getSocketFactory();
        instance = this;
        buttonPoster = findViewById(R.id.button_poster);
        buttonMore = findViewById(R.id.button_details);

        buttonPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = PosterFragment.newInstance(project, username, token);
                ft.add(R.id.poster_container, fragment, "poster");
                ft.commit();
                 */
                DialogFragment dialogPoster = PosterFragment.newInstance(project, username, token);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogPoster.show(ft, "poster");
            }
        });

        buttonMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                juryProjectCommAsyncTask = new JuryProjectCommAsyncTask(project, sslSocketFactory, instance);
                String url = Utils.buildUrlForLIJUR(username, token);
                juryProjectCommAsyncTask.execute(url, "GET");
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ProjectDetailFragment.newInstance(project);
        ft.add(R.id.project_detail_container,fragment, "project");
        ft.commit();

        for(UserModel user : studentsGroup.getMembers()){
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            Fragment fragment2 = IdentityStudentsFragment.newInstance(user);
            ft2.add(R.id.student_container, fragment2, "student");
            ft2.commit();
        }

        if(!project.getProjectPoster()){
            buttonPoster.setEnabled(false);
        }
    }

    public void presentJury(JuryModel juryModel){
        DialogFragment dialogJury = JuryProjectDetailsComm.newInstance(juryModel);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialogJury.show(ft, "jury");
    }

}

