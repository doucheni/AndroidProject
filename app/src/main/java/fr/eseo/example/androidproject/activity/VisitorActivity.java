package fr.eseo.example.androidproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import fr.eseo.example.androidproject.fragments.ProjectCardVisitor;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.PseudoJuryModel;

public class VisitorActivity extends AppCompatActivity {

    private static final String ARG_PJ = "pseudojury";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    private PseudoJuryModel pseudoJuryModel;
    private String username;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);

        // Get the pseudo-jury from logon activity
        Intent intent = getIntent();
        pseudoJuryModel = (PseudoJuryModel)intent.getSerializableExtra(ARG_PJ);
        username =  intent.getStringExtra(ARG_USERNAME);
        token = intent.getStringExtra(ARG_TOKEN);

        // Create fragment for each project in pseudo jury
        for(int i = 0; i < pseudoJuryModel.getProjects().size(); i++){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = ProjectCardVisitor.newInstance(pseudoJuryModel.getProjects().get(i), username, token);
            ft.add(R.id.visitor_project_container, fragment, "project-card-" + i);
            ft.commit();
        }

    }

}