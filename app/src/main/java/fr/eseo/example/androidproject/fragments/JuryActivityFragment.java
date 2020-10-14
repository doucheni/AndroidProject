package fr.eseo.example.androidproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.JuryDetailsActivity;
import fr.eseo.example.androidproject.room.entities.Jury;


public class JuryActivityFragment extends Fragment {

    private static final String ARG_JURY = "jury";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    private Jury jury;
    private TextView textTitle;

    private String token;
    private Intent intent;
    private String username;


    public JuryActivityFragment() {
        // Required empty public constructor
    }

    public static JuryActivityFragment newInstance(Jury jury, String username, String token) {
        JuryActivityFragment fragment = new JuryActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JURY, jury);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jury = (Jury) getArguments().getSerializable(ARG_JURY);
            username = getArguments().getString(ARG_USERNAME);
            token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_jury_summary_list, container, false);

        textTitle = v.findViewById(R.id.summary_jury_id);
        textTitle.setText(String.valueOf(jury.getIdJury()));
        //members = v.findViewById(R.id.summary_jury_names);


        intent = new Intent(getActivity(), JuryDetailsActivity.class);
        intent.putExtra("ARG_JURY", jury.getIdJury());
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
