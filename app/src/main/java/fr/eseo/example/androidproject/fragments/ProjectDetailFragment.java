package fr.eseo.example.androidproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.room.entities.CommentsVisitor;
import fr.eseo.example.androidproject.room.entities.MarksVisitor;
import fr.eseo.example.androidproject.room.entities.Project;
import fr.eseo.example.androidproject.room.entities.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailFragment extends Fragment {

    private static final String ARG_PROJECT = "project";
    private static final String ARG_JURY = "jury";

    private ProjectModel project;

    private  TextView titleProject;
    private TextView descriptionProject;
    private TextView confidProject;
    private TextView supervisorProject;

    public ProjectDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param project Parameter 1.
     * @return A new instance of fragment ProjectDetailFragment.
     */
    public static ProjectDetailFragment newInstance(ProjectModel project) {
        ProjectDetailFragment fragment = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (ProjectModel) getArguments().getSerializable(ARG_PROJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_project_detail, container, false);

        titleProject = view.findViewById(R.id.titleProject);
        descriptionProject = view.findViewById(R.id.descriptionProject);
        confidProject = view.findViewById(R.id.confidentialityProject);
        supervisorProject = view.findViewById(R.id.supervisorProject);

        titleProject.setText(project.getProjectTitle());

        descriptionProject.setText(project.getProjectDescription());

        if(project.getConfidentiality() == 0){
            confidProject.setText("Non confidentiel");
        }else{
            confidProject.setText("Confidentiel !");
        }

        supervisorProject.setText(project.getSupervisor());

        return view;
    }

}