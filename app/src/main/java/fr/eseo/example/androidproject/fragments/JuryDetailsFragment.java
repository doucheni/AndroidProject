package fr.eseo.example.androidproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.JuryProjectDetailsActivity;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;

public class JuryDetailsFragment  extends Fragment {

    private static final String ARG_JURY = "project";
    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_STUDENTS = "students";

    private TextView textTitle;
    private TextView posterIndicator;
    private TextView confidentialityIndicator;
    private ProjectModel project;
    private Intent intent;
    private String username;
    private String token;
    private StudentsGroup studentsGroup;


    public JuryDetailsFragment() {
        // Required empty public constructor
    }
    public static JuryDetailsFragment newInstance(ProjectModel project, StudentsGroup studentsGroup, String username, String token) {
        JuryDetailsFragment fragment = new JuryDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JURY, project);
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
            project = (ProjectModel) getArguments().getSerializable(ARG_JURY);
            username = getArguments().getString(ARG_USERNAME);
            token = getArguments().getString(ARG_TOKEN);
            studentsGroup = (StudentsGroup)getArguments().getSerializable(ARG_STUDENTS);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_jury_details_project, container, false);

        textTitle = v.findViewById(R.id.projectTitle);
        textTitle.setText(project.getProjectTitle());
        posterIndicator = v.findViewById(R.id.posterBoolean);

        if(project.getProjectPoster()){
            posterIndicator.setText("poster available");
        }else{
            posterIndicator.setText("No poster available");
        }

        confidentialityIndicator = v.findViewById(R.id.confidentialityIndicator);

        if(project.getConfidentiality() != 0){
            confidentialityIndicator.setText("Confidential project");
        }else{
            confidentialityIndicator.setText("Not confidential");
        }

        intent = new Intent(getActivity(), JuryProjectDetailsActivity.class);
        intent.putExtra(ARG_PROJECT, project);
        intent.putExtra(ARG_USERNAME,username);
        intent.putExtra(ARG_TOKEN, token);
        intent.putExtra(ARG_STUDENTS, studentsGroup);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                    startActivity(intent);

            }
        });
        return v;
    }

}
