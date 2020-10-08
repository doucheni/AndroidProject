package fr.eseo.example.androidproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.room.entities.Project;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsDetailsCommFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsDetailsCommFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROJECT = "project";

    private Project project;
    private TextView textTitle;

    public ProjectsDetailsCommFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param project Parameter 1.
     * @return A new instance of fragment ProjectsDetailsCommFragment.
     */
    public static ProjectsDetailsCommFragment newInstance(Project project) {
        ProjectsDetailsCommFragment fragment = new ProjectsDetailsCommFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (Project)getArguments().getSerializable(ARG_PROJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_projects_details_comm, container, false);

        textTitle = v.findViewById(R.id.projectTitle);
        textTitle.setText(project.getTitle());
        System.out.println(project.getTitle());

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                
            }
        });

        return v;
    }
}