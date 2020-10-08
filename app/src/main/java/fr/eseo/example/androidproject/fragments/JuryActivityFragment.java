package fr.eseo.example.androidproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import java.io.Serializable;
import java.util.List;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.Project;

public class JuryActivityFragment extends Fragment {

    private static final String ARG_JURY = "jury";
    private Jury jury;
    private TextView textTitle;
    private int idJury;

    public JuryActivityFragment() {
        // Required empty public constructor
    }

    public static JuryActivityFragment newInstance(Jury jury) {
        JuryActivityFragment fragment = new JuryActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JURY, jury);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jury = (Jury) getArguments().getSerializable(ARG_JURY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_jury_summary_list, container, false);

        textTitle = v.findViewById(R.id.summary_jury_id);
        textTitle.setText(String.valueOf(jury.getIdJury()));
        System.out.println(jury.getIdJury());

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        return v;
    }

}
