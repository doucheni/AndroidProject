package fr.eseo.example.androidproject.fragments;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;

/**
 * Class of the DialogFragment JuryProjectDetailsComm
 * Display the project's jury
 */
public class JuryProjectDetailsComm extends DialogFragment {

    // Argument's name
    private static final String ARG_JURY = "jury";

    // Argument
    private JuryModel jury;

    public JuryProjectDetailsComm() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of JuryProjectDetailsComm
     *
     * @param juryModel the JuryModel of the project.
     * @return A new instance of fragment JuryProjectDetailsComm.
     */
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

        // Initialization of container
        LinearLayout linearLayout = v.findViewById(R.id.jury_project_container);

        for(int i = 0; i < jury.getMembers().size(); i++){
            try{
                linearLayout.addView(
                        configureTextView(
                                jury.getMembers().get(i).getUserForename() + " " + jury.getMembers().get(i).getUserSurname(), i
                        )
                );
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        return v;
    }

    /**
     * Run when displaying the DialogFragment
     * The window is going to take device's with (MATCH_PARENT)
     */
    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }

    /**
     * Configure a TextView with a specific content
     * @param content, the text content of our TextView
     * @param index, the id of the TextView (int)
     * @return textView, our TextView configure
     */
    private TextView configureTextView(String content, int index){
        TextView textView = new TextView(getActivity());

        textView.setId(index); //Set id to remove in the future.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,20,0,20);
        textView.setLayoutParams(params);
        textView.setText(content);
        Typeface face = ResourcesCompat.getFont(getContext(), R.font.roboto);
        textView.setTypeface(face);
        return textView;
    }


}