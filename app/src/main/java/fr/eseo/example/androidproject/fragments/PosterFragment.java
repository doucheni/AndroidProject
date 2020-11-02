package fr.eseo.example.androidproject.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import fr.eseo.example.androidproject.AsynchroneTasks.PosterAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.Utils;

/**
 * Class of the DialogFragment PosterFragment
 * Display the project's poster in a DialogFragment
 */
public class PosterFragment extends DialogFragment {

    // Parameters names
    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    // Instance of PosterAsyncTask
    private PosterAsyncTask posterAsyncTask;

    // Parameters
    private ProjectModel project;
    private String username;
    private String token;

    // ProgressDialog to show a loading screen during poster's loading
    private ProgressDialog progressDialog;

    // View from XML layout
    private ImageView posterImage;

    public PosterFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of PosterFragment
     *
     * @param project, the project with the poster we want to show.
     * @param username, the user's username.
     * @param token, the user's token
     * @return A new instance of fragment posterFragment.
     */
    public static PosterFragment newInstance(ProjectModel project, String username, String token) {
        PosterFragment fragment = new PosterFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT, project);
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (ProjectModel)getArguments().getSerializable(ARG_PROJECT);
            username = getArguments().getString(ARG_USERNAME);
            token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_poster, container, false);

        // Initialize ImageView
        posterImage = v.findViewById(R.id.poster_imageView);

        this.runPosterAsynTask(v);

        return v;
    }

    /**
     * Treat the result from PosterAsyncTask
     * Update the bitmap of the ImageView inside DialogFragment
     * @param bitmap, the request's result
     */
    public void treatmentResult(Bitmap bitmap){
        posterImage.setImageBitmap(bitmap);
        this.progressDialog.dismiss();
    }

    /**
     * Send a POSTR request to ESEO's API to get the poster of a specific project
     * PosterAsyncTask call PosterFragment.treatmentResult(Bitmap bitmap) after
     */
    private void runPosterAsynTask(View v){
        posterAsyncTask = new PosterAsyncTask(this, Utils.configureSSLContext(v.getContext()).getSocketFactory());
        this.progressDialog = ProgressDialog.show(v.getContext(),"Loading", "Please wait ...", true);
        posterAsyncTask.execute(Utils.buildURLForPOSTR(username,token, project.getProjectId(),"FLB64"), "GET");
    }
}