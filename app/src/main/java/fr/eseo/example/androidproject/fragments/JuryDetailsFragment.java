package fr.eseo.example.androidproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.ProjectsDetailsCommActivity;
import fr.eseo.example.androidproject.api.ProjectModel;

public class JuryDetailsFragment  extends Fragment {

    private static final String ARG_JURY = "project";
    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private TextView textTitle;
    private TextView posterIndicator;
    private TextView confidentialityIndicator;
    private ProjectModel project;
    private Intent intent;
    private String username;
    private String token;
    Context ctx;

    private int idJury;

    public JuryDetailsFragment() {
        // Required empty public constructor
    }
    public static JuryDetailsFragment newInstance(ProjectModel project,String username, String token) {
        JuryDetailsFragment fragment = new JuryDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JURY, project);
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

        intent = new Intent(getActivity(), ProjectsDetailsCommActivity.class);
        intent.putExtra(ARG_JURY, project);
        intent.putExtra(ARG_USERNAME,username);
        intent.putExtra(ARG_TOKEN, token);
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        return v;
    }

}
