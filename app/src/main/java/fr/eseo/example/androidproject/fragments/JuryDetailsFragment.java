package fr.eseo.example.androidproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.JuryActivity;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.Project;

public class JuryDetailsFragment  extends Fragment {

    private static final String ARG_JURY = "project";
    private Jury jury;
    private TextView textTitle;
    private ProjectModel project;
    Context ctx;

    private int idJury;

    public JuryDetailsFragment() {
        // Required empty public constructor
    }
    public static JuryDetailsFragment newInstance(ProjectModel project) {
        JuryDetailsFragment fragment = new JuryDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JURY, project);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (ProjectModel) getArguments().getSerializable(ARG_JURY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_jury_details_project, container, false);

        textTitle = v.findViewById(R.id.projectTitle);
        textTitle.setText(project.getProjectTitle());
        System.out.println("TITLEEE"+project.getProjectTitle());

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        return v;
    }

}
