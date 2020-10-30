package fr.eseo.example.androidproject.fragments;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JuryProjectDetailsComm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JuryProjectDetailsComm extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_JURY = "jury";

    // TODO: Rename and change types of parameters
    private JuryModel jury;

    public JuryProjectDetailsComm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param juryModel Parameter 1.
     * @return A new instance of fragment JuryProjectDetailsComm.
     */
    // TODO: Rename and change types and number of parameters
    public static JuryProjectDetailsComm newInstance(JuryModel juryModel) {
        JuryProjectDetailsComm fragment = new JuryProjectDetailsComm();
        Bundle args = new Bundle();
        args.putSerializable(ARG_JURY, juryModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jury = (JuryModel)getArguments().getSerializable(ARG_JURY);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_jury_project_details_comm, container, false);

        LinearLayout linearLayout = v.findViewById(R.id.jury_project_container);

        for(int i = 0; i < jury.getMembers().size(); i++){
            TextView textView = new TextView(getActivity());

            textView.setId(i); //Set id to remove in the future.
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,20,0,20);
            textView.setLayoutParams(params);
            textView.setText(jury.getMembers().get(i).getUserForename() + " " + jury.getMembers().get(i).getUserSurname());
            Typeface face = ResourcesCompat.getFont(getContext(), R.font.roboto);
            textView.setTypeface(face);
            try{
                linearLayout.addView(textView);
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}