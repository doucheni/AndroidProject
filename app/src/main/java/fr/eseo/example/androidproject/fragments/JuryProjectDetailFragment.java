package fr.eseo.example.androidproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.room.entities.MarksJury;

public class JuryProjectDetailFragment extends Fragment {

    private static final String ARG_PROJECT = "project";


    private ProjectModel project;
    private View view;
    private EditText projectMarkET;
    private Button validateBTN;



    public JuryProjectDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param project Parameter 1.
     * @return A new instance of fragment JuryProjectDetailFragment.
     */
    public static JuryProjectDetailFragment newInstance(ProjectModel project) {
        JuryProjectDetailFragment fragment = new JuryProjectDetailFragment();
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
         view  = inflater.inflate(R.layout.fragment_jury_project_detail, container, false);

        TextView titleProject = view.findViewById(R.id.titleProject);
        TextView descriptionProject = view.findViewById(R.id.descriptionProject);
        TextView confidProject = view.findViewById(R.id.confidentialityProject);
        TextView supervisorProject = view.findViewById(R.id.supervisorProject);
        this.projectMarkET = view.findViewById(R.id.project_mark);
        this.validateBTN = view.findViewById(R.id.btn_send_data);

        titleProject.setText(project.getProjectTitle());

        descriptionProject.setText(project.getProjectDescription());

        if(project.getConfidentiality() == 0){
            confidProject.setText("Non confidentiel");
        }else{
            confidProject.setText("Confidentiel !");
        }

        supervisorProject.setText(project.getSupervisor());

        //Button to send the grade in the database
        this.initButtonValidateAction();

        return view;
    }

    /**
     * Add a OnClickListener on validateBTN
     * Insert mark in the database
     * If there is no data, display a toast
     */
    private void initButtonValidateAction(){
        validateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Get values from EditText
                final String markString = projectMarkET.getText().toString();

                    // If there is a mark
                    if(!markString.equals("")){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                int mark = Integer.parseInt(markString);
                                MarksJury marksJury = new MarksJury(project.getProjectId(), mark);
                                EseoDatabase.getDatabase(view.getContext()).marksJuryDAO().insertAll(marksJury);
                            }
                        });
                    }
                    Toast.makeText(v.getContext(), "Votre note a bien été enregistrée", Toast.LENGTH_LONG).show();
                    projectMarkET.setText("");

            }
        });
    }

}
