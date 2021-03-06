package fr.eseo.example.androidproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.AllProjectsDetailsActivity;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllProjectsFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllProjectsFragment extends Fragment {

    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_STUDENTS = "students";


    private ProjectModel project;
    private StudentsGroup studentsGroup;

    private String username;
    private String token;
    private Intent intent;

    public AllProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param project Parameter 1.
     *@param studentsGroup and the username and token of the user wich is connected
     * @return A new instance of fragment AllProjectsFragment.
     */
    public static AllProjectsFragment newInstance(ProjectModel project,StudentsGroup studentsGroup, String username, String token) {
        AllProjectsFragment fragment = new AllProjectsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        args.putSerializable(ARG_STUDENTS, studentsGroup);

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
        View v = inflater.inflate(R.layout.fragment_all_projects, container, false);

        TextView textTitle = v.findViewById(R.id.projectTitle);
        textTitle.setText(project.getProjectTitle());

        TextView posterIndicator = v.findViewById(R.id.posterBoolean);

        if(project.getProjectPoster()){
            posterIndicator.setText("poster available");
        }else{
            posterIndicator.setText("No poster available");
        }

        TextView confidentialityIndicator = v.findViewById(R.id.confidentialityIndicator);

        if(project.getConfidentiality() != 0){
            confidentialityIndicator.setText("Confidential project");
        }else{
            confidentialityIndicator.setText("Not confidential");
        }

        intent = new Intent(getActivity(), AllProjectsDetailsActivity.class);
        intent.putExtra(ARG_PROJECT, project);
        intent.putExtra(ARG_USERNAME,username);
        intent.putExtra(ARG_TOKEN, token);
        intent.putExtra(ARG_STUDENTS, studentsGroup);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(project.getConfidentiality() != 0){
                    Toast.makeText(v.getContext(), "You can't see the detail of a confidential project", Toast.LENGTH_LONG).show();
                }else{
                    startActivity(intent);
                }
            }
        });
        return v;
    }
}