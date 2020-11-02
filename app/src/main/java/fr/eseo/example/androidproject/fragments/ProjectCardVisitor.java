package fr.eseo.example.androidproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.room.entities.CommentsVisitor;
import fr.eseo.example.androidproject.room.entities.MarksVisitor;

/**
 * Class of the fragment ProjectCardVisitor
 * For a project given, create a project card with the main information
 *  Title
 *  Description
 *  Poster
 * The visitor has the possibility to write a mark and a comment to the project
 * These marks and comments are store in a local database
 */
public class ProjectCardVisitor extends Fragment {

    // Argument's names
    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    // Arguments
    private ProjectModel projectModel;
    private String username;
    private String token;

    // Views from XML layout
    private TextView projectTitleTV;
    private TextView projectDescriptionTV;
    private EditText projectCommentET;
    private EditText projectMarkET;
    private Button validateBTN;
    private Button projectPosterBTN;
    private View fragment_view;

    public ProjectCardVisitor() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment
     *
     * @param projectModel the project of the fragment
     * @param username, the communication member's username
     * @param token, the communcation member's token
     * @return A new instance of fragment ProjectCardVisitor.
     */
    public static ProjectCardVisitor newInstance(ProjectModel projectModel, String username, String token) {
        ProjectCardVisitor fragment = new ProjectCardVisitor();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, projectModel);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectModel = (ProjectModel)getArguments().getSerializable(ARG_PROJECT);
            username = getArguments().getString(ARG_USERNAME);
            token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_project_card_visitor, container, false);

        // Initialization View's Component and update TextViews
        this.initVariables();

        // Add a OnClickListener on projectPosterBTN
        this.initButtonPosterAction();

        // Add a OnClickListener on validateBTN
        this.initButtonValidateAction();

        return fragment_view;
    }

    /**
     * Initialization of the views
     * Update the text in title's TextView
     * Update the text in description's TextView
     */
    private void initVariables(){
        this.projectTitleTV = fragment_view.findViewById(R.id.project_title);
        this.projectDescriptionTV = fragment_view.findViewById(R.id.project_description);
        this.projectCommentET = fragment_view.findViewById(R.id.project_comment);
        this.projectMarkET = fragment_view.findViewById(R.id.project_mark);
        this.validateBTN = fragment_view.findViewById(R.id.btn_send_data);
        this.projectPosterBTN = fragment_view.findViewById(R.id.button_poster);

        // Add project's title to textview
        this.projectTitleTV.setText(this.projectModel.getProjectTitle());

        // Add project's description to textview
        this.projectDescriptionTV.setText(this.projectModel.getProjectDescription());
    }

    /**
     * Add a OnClickListener on projectPosterBTN
     * Create a DialogFragment to show the poster of the given project
     */
    private void initButtonPosterAction(){
        projectPosterBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment dialogPoster = PosterFragment.newInstance(projectModel, username, token);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                dialogPoster.show(ft, "poster");
            }
        });
    }

    /**
     * Add a OnClickListener on validateBTN
     * Insert comment and/or mark in the database
     * If there is no data, display a toast
     */
    private void initButtonValidateAction(){
        validateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Get values from EditText
                final String comment = projectCommentET.getText().toString();
                final String markString = projectMarkET.getText().toString();

                // If there is no data
                if(comment.equals("") && markString.equals("")){
                    Toast.makeText(v.getContext(), "Vous avez rempli aucun champs !", Toast.LENGTH_LONG).show();
                }else{
                    // If there is a comment
                    if(!comment.equals("")){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                CommentsVisitor commentsVisitor = new CommentsVisitor(projectModel.getProjectId(), comment);
                                EseoDatabase.getDatabase(fragment_view.getContext()).commentsVisitorDAO().insertAll(commentsVisitor);
                            }
                        });
                    }
                    // If there is a mark
                    if(!markString.equals("")){
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                int mark = Integer.parseInt(markString);
                                MarksVisitor marksVisitor = new MarksVisitor(projectModel.getProjectId(), mark);
                                EseoDatabase.getDatabase(fragment_view.getContext()).marksVisitorDAO().insertAll(marksVisitor);
                            }
                        });
                    }
                }
            }
        });
    }






}