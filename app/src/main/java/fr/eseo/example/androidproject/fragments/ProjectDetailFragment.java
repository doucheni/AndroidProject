package fr.eseo.example.androidproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.UserModel;
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
    private JuryModel juryModel;

    private  TextView titleProject;
    private TextView descriptionProject;
    private TextView confidProject;
    private TextView supervisorProject;
    private LinearLayout memberContainer;
    private LinearLayout juryContainer;

    public ProjectDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param project Parameter 1.
     * @param project Parameter 2.
     * @return A new instance of fragment ProjectDetailFragment.
     */
    public static ProjectDetailFragment newInstance(ProjectModel project, JuryModel juryModel) {
        ProjectDetailFragment fragment = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
        args.putSerializable(ARG_JURY, juryModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (ProjectModel) getArguments().getSerializable(ARG_PROJECT);
            juryModel = (JuryModel) getArguments().getSerializable(ARG_JURY);
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
        memberContainer = view.findViewById(R.id.memberContainer);
        juryContainer = view.findViewById(R.id.juryMemberContainer);

        titleProject.setText(project.getProjectTitle());

        descriptionProject.setText(project.getProjectDescription());

        if(project.getConfidentiality() == 0){
            confidProject.setText("Non confidentiel");
        }else{
            confidProject.setText("Confidentiel !");
        }

        supervisorProject.setText(project.getSupervisor());

        for(UserModel user : juryModel.getMembers()){
            TextView juryTextView = new TextView(view.getContext());
            juryTextView.setText(user.getUserForename() + " " + user.getUserSurname());
            juryContainer.addView(juryTextView);
        }

        return view;
    }
}