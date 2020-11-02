package fr.eseo.example.androidproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.ProjectCommActivity;
import fr.eseo.example.androidproject.activity.ProjectsDetailsCommActivity;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;

/**
 * Class of the fragment ProjectsListCommFragment
 * For a project given, create a preview of the project :
 *  Its title
 *  Its poster disponibility
 *  Its confidentiality
 */
public class ProjectsListCommFragment extends Fragment {

    private static final String MSG_POSTER_AVAILABLE = "Poster disponible.";
    private static final String MSG_POSTER_UNAVAILABLE = "Poster indisponible.";
    private static final String MSG_CONFID = "Confidentiel.";
    private static final String MSG_CONFID_OK = "Non confidentiel.";
    private static final String MSG_ERROR_ADD = "Impossible d'ajouter le projet, verifiez sa confidentialité et/ou son poster.";
    private static final String MSG_ERROR_DETAIL = "Vous ne pouvez pas voir le détail d'un projet confidentiel.";

    // Parameters name
    private static final String ARG_PROJECT = "project";
    private static final String ARG_STUDENTS = "students";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    // Parameters
    private ProjectModel project;
    private StudentsGroup studentsGroup;
    private String username;
    private String token;

    // Instance of ProjectCommActivity
    private ProjectCommActivity mainActivity;

    // Views from XML Layout
    private TextView textTitle;
    private TextView posterIndicator;
    private TextView confidentialityIndicator;
    private CheckBox checkBox;

    // Intent
    private Intent intent;

    public ProjectsListCommFragment() {
        // Required empty public constructor
    }

    /**
     * Create an instance of this fragment
     * @param project, the ProjectModel of this fragment.
     * @param studentsGroup, the Students members of the project
     * @param username, the user's username
     * @param token, the user's token
     * @return A new instance of fragment ProjectsDetailsCommFragment.
     */
    public static ProjectsListCommFragment newInstance(ProjectModel project, StudentsGroup studentsGroup, String username, String token) {
        ProjectsListCommFragment fragment = new ProjectsListCommFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
        args.putSerializable(ARG_STUDENTS, studentsGroup);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (ProjectModel) getArguments().getSerializable(ARG_PROJECT);
            username = getArguments().getString(ARG_USERNAME);
            token = getArguments().getString(ARG_TOKEN);
            studentsGroup = (StudentsGroup)getArguments().getSerializable(ARG_STUDENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_projects_list_comm, container, false);

        // Initialization of each ProjectsListCommFragment's variables
       this.initVariables(v);

       // Update View's values with project's values
       this.updateViews();

       // Add OnClickListener on the main view
       this.initViewOnClickListener(v);

       // Add OnClickListener on checkbox
        this.initCheckBoxOnClickListener();

        return v;
    }

    /**
     * Initialization of each variable for ProjectListCommFragment class
     */
    private void initVariables(View v){
        mainActivity = (ProjectCommActivity)getActivity();
        textTitle = v.findViewById(R.id.projectTitle);
        posterIndicator = v.findViewById(R.id.posterBoolean);
        confidentialityIndicator = v.findViewById(R.id.confidentialityIndicator);
        checkBox = v.findViewById(R.id.projectTitle);
        intent = new Intent(getActivity(), ProjectsDetailsCommActivity.class);
        intent.putExtra(ARG_PROJECT, project);
        intent.putExtra(ARG_STUDENTS, studentsGroup);
        intent.putExtra(ARG_USERNAME,username);
        intent.putExtra(ARG_TOKEN, token);
    }

    /**
     * Change the view's value with project's values
     */
    private void updateViews(){
        // Set the title of the proejct
        textTitle.setText(project.getProjectTitle());

        // Set the poster indicator
        if(project.getProjectPoster()){
            posterIndicator.setText(MSG_POSTER_AVAILABLE);
        }else{
            posterIndicator.setText(MSG_POSTER_UNAVAILABLE);
        }

        // Set the confidentiality indicator
        if(project.getConfidentiality() != 0){
            confidentialityIndicator.setText(MSG_CONFID);
        }else{
            confidentialityIndicator.setText(MSG_CONFID_OK);
        }
    }

    /**
     * Adding a OnClickListener for the main view.
     * Redirect the user to ProjectsDetailsCommActivity class
     * Transfer data to next activity
     * If project is confidential : show a Toast
     * @param v, the main View
     */
    private void initViewOnClickListener(View v){
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(project.getConfidentiality() != 0){
                    Toast.makeText(v.getContext(), MSG_ERROR_DETAIL, Toast.LENGTH_LONG).show();
                }else{
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Adding a OnClickListener for the checkbox
     * Update the list of project in ProjectCommActivity
     * Verify if the project can be add to a pseudo jury (confidentiality ? Poster ?)
     */
    private void initCheckBoxOnClickListener(){
        checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    boolean correctProject = project.getProjectPoster() && project.getConfidentiality() == 0;
                    if(correctProject){
                        mainActivity.addProjectSelected(project);
                    }else{
                        Toast.makeText(getContext(), MSG_ERROR_ADD, Toast.LENGTH_LONG).show();
                        checkBox.setChecked(false);
                    }

                }else{
                    mainActivity.removeProjectSelected(project);
                }
            }
        });
    }
}