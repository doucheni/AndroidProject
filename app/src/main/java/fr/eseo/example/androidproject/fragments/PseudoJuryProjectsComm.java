package fr.eseo.example.androidproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.activity.logon;
import fr.eseo.example.androidproject.api.PseudoJuryModel;

/**
 * Class of the fragment PseudoJuryProjectsComm
 * For a PseudoJury given, create a TextView wich contain the title of each projects.
 */
public class PseudoJuryProjectsComm extends Fragment {

    // Parameter's names
    private static final String ARG_PJ = "pseudojury";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    // Parameters
    private PseudoJuryModel pseudoJuryModel;
    private String username;
    private String token;

    public PseudoJuryProjectsComm() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of the fragment
     * @param pseudoJuryModel pseudoJuryModel, the pseudoJury of the fragment.
     * @param username, user's username.
     * @param token, user's token.
     * @return A new instance of fragment pseudoJuryProjectsComm.
     */
    public static PseudoJuryProjectsComm newInstance(PseudoJuryModel pseudoJuryModel, String username, String token) {
        PseudoJuryProjectsComm fragment = new PseudoJuryProjectsComm();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PJ, pseudoJuryModel);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pseudoJuryModel = (PseudoJuryModel)getArguments().getSerializable(ARG_PJ);
            username = getArguments().getString(ARG_USERNAME);
            token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pseudo_jury_projects_comm, container, false);

        // Get the linearLayout container
        LinearLayout linearLayout = v.findViewById(R.id.pseudojury_container);

        // Add a TextView in the container
        try{
            linearLayout.addView(this.createTextView());
        }catch(Exception e){
            e.printStackTrace();
        }

        // Add a OnClickListner on the view
        this.initOnClickListenerAction(v);

        return v;
    }

    /**
     * Create a TextView wich contain each project's title of the PseudoJury
     * @return textView, the TextView
     */
    private TextView createTextView(){
        TextView textView = new TextView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,20,0,20);
        textView.setLayoutParams(params);

        String content = "";

        for(int i = 0; i < pseudoJuryModel.getProjects().size(); i++){
            if(i < pseudoJuryModel.getProjects().size() - 1){
                content += pseudoJuryModel.getProjects().get(i).getProjectTitle() + " / ";
            }else{
                content += pseudoJuryModel.getProjects().get(i).getProjectTitle();
            }
        }

        textView.setText(content);
        textView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.colorWhiteText));

        return textView;
    }

    /**
     * Configure the action of View's OnClickListener
     * @param v, the main View of the fragment
     */
    private void initOnClickListenerAction(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), logon.class);
                intent.putExtra(ARG_PJ, pseudoJuryModel);
                intent.putExtra(ARG_USERNAME, username);
                intent.putExtra(ARG_TOKEN, token);
                startActivity(intent);
            }
        });
    }
}