package fr.eseo.example.androidproject.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fr.eseo.example.androidproject.AsynchroneTasks.PosterAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;
import fr.eseo.example.androidproject.api.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PosterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PosterFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROJECT = "project";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    private PosterAsyncTask posterAsyncTask;

    // TODO: Rename and change types of parameters
    private ProjectModel project;
    private String username;
    private String token;

    private ProgressDialog progressDialog;

    private ImageView posterImage;

    public PosterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param project Parameter 1.
     * @return A new instance of fragment posterFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        posterImage = v.findViewById(R.id.poster_imageView);

        posterAsyncTask = new PosterAsyncTask(this, Utils.configureSSLContext(v.getContext()).getSocketFactory());
        String url = Utils.buildURLForPOSTR(username,token, project.getProjectId(),"FLB64");
        this.progressDialog = ProgressDialog.show(v.getContext(),"Loading", "Please wait ...", true);
        posterAsyncTask.execute(url, "GET");
        return v;
    }

    public void treatmentResult(Bitmap bitmap){
        posterImage.setImageBitmap(bitmap);
        this.progressDialog.dismiss();
    }
}