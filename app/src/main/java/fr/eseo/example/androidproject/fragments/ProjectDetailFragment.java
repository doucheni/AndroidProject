package fr.eseo.example.androidproject.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;

/**
 * Class of the Fragment ProjectDetailFragment
 * Display project's main information
 *  Title
 *  Description
 *  Confidentiality
 *  Supervisor
 */
public class  ProjectDetailFragment extends Fragment {

    private static final String MSG_CONFID = "Confidentiel.";
    private static final String MSG_CONFIG_OK = "Non confidentiel.";

    // Argument's name
    private static final String ARG_PROJECT = "project";

    // Argument
    private ProjectModel project;

    // Views from XML layout
    private  TextView titleProject;
    private TextView descriptionProject;
    private TextView confidProject;
    private TextView supervisorProject;

    public ProjectDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Create a instance of ProjectDetailFragment
     *
     * @param project, our ProjectModel.
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

        // Initialization of variables
        this.initVariables(view);

        // Update the TextViews
        this.updateViews(this.project);

        return view;
    }

    /**
     * Initialization of the views
     * @param view, our main view
     */
    private void initVariables(View view){
        titleProject = view.findViewById(R.id.titleProject);
        descriptionProject = view.findViewById(R.id.descriptionProject);
        confidProject = view.findViewById(R.id.confidentialityProject);
        supervisorProject = view.findViewById(R.id.supervisorProject);
    }

    /**
     * Update TextView of the fragment
     * @param project, our ProjectModel
     */
    private void updateViews(ProjectModel project){
        titleProject.setText(project.getProjectTitle());

        descriptionProject.setText(project.getProjectDescription());

        if(project.getConfidentiality() == 0){
            confidProject.setText(MSG_CONFID);
        }else{
            confidProject.setText(MSG_CONFIG_OK);
        }

        supervisorProject.setText(project.getSupervisor());
    }



}