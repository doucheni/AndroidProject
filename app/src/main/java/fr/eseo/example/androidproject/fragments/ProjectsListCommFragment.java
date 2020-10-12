package fr.eseo.example.androidproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.ProjectsDetailsCommActivity;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.room.entities.Project;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsListCommFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsListCommFragment extends Fragment {

    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    private ProjectModel project;
    private TextView textTitle;
    private TextView posterIndicator;
    private String username;
    private String token;
    private Intent intent;

    public ProjectsListCommFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param project Parameter 1.
     * @return A new instance of fragment ProjectsDetailsCommFragment.
     */
    public static ProjectsListCommFragment newInstance(ProjectModel project, String username, String token) {
        ProjectsListCommFragment fragment = new ProjectsListCommFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_projects_list_comm, container, false);

        textTitle = v.findViewById(R.id.projectTitle);
        textTitle.setText(project.getProjectTitle());

        posterIndicator = v.findViewById(R.id.posterBoolean);
        if(!project.getProjectPoster().equals("") || project.getProjectPoster() != null){
            posterIndicator.setText("poster available");
        }else{
            posterIndicator.setText("No poster available");
        }

        intent = new Intent(getActivity(), ProjectsDetailsCommActivity.class);
        intent.putExtra(ARG_PROJECT, project);
        intent.putExtra(ARG_USERNAME,username);
        intent.putExtra(ARG_TOKEN, token);
        v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    startActivity(intent);
                }
            });
        return v;
    }
}