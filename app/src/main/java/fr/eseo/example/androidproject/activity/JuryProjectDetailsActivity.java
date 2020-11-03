package fr.eseo.example.androidproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.JuryProjectsDetailsAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.IdentityStudentsFragmentVote;
import fr.eseo.example.androidproject.fragments.JuryProjectDetailFragment;
import fr.eseo.example.androidproject.fragments.JuryProjectDetailsComm;
import fr.eseo.example.androidproject.fragments.PosterFragment;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.room.entities.MarksJury;

public class JuryProjectDetailsActivity extends AppCompatActivity {

    private static final int WRITE_REQUEST_CODE = 101;

    // Intent's arguments
    private static final String ARG_PROJECT = "project";
    private static final String ARG_STUDENTS = "students";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";

    // Instance of JuryProjectsDetailsAsyncTask
    JuryProjectsDetailsAsyncTask juryProjectsDetailsAsyncTask;

    // instance of JuryProjectDetailsActivity
    JuryProjectDetailsActivity instance;


    // SSLSocketFactory configuration
    private SSLSocketFactory sslSocketFactory;

    // Intent's values
    private String token;
    private String username;
    private ProjectModel project;
    private StudentsGroup studentsGroup;

    // Views from XML layout
    private Button buttonPoster;
    private Button buttonMore;
    private Button buttonStudents;
    private LinearLayout marksResults;

    // List from database
    private List<MarksJury> projectMarks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_details_jury);

        // Initialization of variables
        this.initVariables();

        // Add onClickListener to buttonPoster
        this.initBtnPosterAction();

        // Add onClickListener to buttonMore
        this.initBtnMoreAction();


        // Create a ProjectDetailFragment
        this.createProjectDetailFragment();

        // Create Several StudentIdentityFragments
        this.createStudentsIdentityFragments();


        // Get and present marks from database
        this.updateMarks();
    }

    /**
     * Function wich obtain the result of the LIJUR request
     * create a DialogFragment with the given juryModel
     * @param juryModel, the request's result
     */
    public void presentJury(JuryModel juryModel){
        DialogFragment dialogJury = JuryProjectDetailsComm.newInstance(juryModel);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialogJury.show(ft, "jury");
    }




    /**
     * Create a file with the given title and call startActivityForResult to write in it
     * @param title, the title of the file
     */
    private void createFile(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * Called after startActivityForActivity
     * @param requestCode, the action we want
     * @param resultCode, the result of the action
     * @param data, the data of the file
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String content = "";

        if(projectMarks.size() > 0){
            content += "Note(s) : ";
        }

        for(int i = 0; i < projectMarks.size(); i++){
            if(i < projectMarks.size() - 1){
                content += projectMarks.get(i).getMark() + "; ";
            }else{
                content += projectMarks.get(i).getMark() + "\n";
            }
        }



        if(requestCode == WRITE_REQUEST_CODE){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if(data != null && data.getData() != null){
                        writeInFile(data.getData(), content);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }


    /**
     * Write in a file a given content
     * @param uri, uri of the file
     * @param text, the content of the file
     */
    private void writeInFile(@NonNull Uri uri, @NonNull String text){
        OutputStream outputStream;
        try{
            outputStream = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text);
            bw.flush();
            bw.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


    /**
     * Initialization of each variables from JuryProjectDetailsActivity class
     */
    private void initVariables(){

        Intent intent = getIntent();
        token = intent.getStringExtra(ARG_TOKEN);
        project = (ProjectModel) intent.getSerializableExtra(ARG_PROJECT);
        username = intent.getStringExtra(ARG_USERNAME);
        studentsGroup = (StudentsGroup) intent.getSerializableExtra(ARG_STUDENTS);
        sslSocketFactory = Utils.configureSSLContext(this.getApplicationContext()).getSocketFactory();
        instance = this;
        buttonPoster = findViewById(R.id.button_poster);
        buttonMore = findViewById(R.id.button_details);
        marksResults = findViewById(R.id.marksResult_container);


        if(!project.getProjectPoster()){
            buttonPoster.setEnabled(false);
        }
    }



    /**
     * Adding a OnClickListener for buttonPoster
     * Create a new DialogFragment to show the poster of the project
     */
    private void initBtnPosterAction(){
        buttonPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogPoster = PosterFragment.newInstance(project, username, token);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogPoster.show(ft, "poster");
            }
        });
    }

    /**
     * Adding a OnClickListerner for buttonMore
     * Create an instance of allProjectsDetailsAsyncTask
     * Execute a LIJUR request to get the jury
     * allProjectsDetailsAsyncTask call allProjectsDetailsActivity.presentJury(JuryModel juryModel)
     */
    private void initBtnMoreAction(){
        buttonMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                juryProjectsDetailsAsyncTask = new JuryProjectsDetailsAsyncTask(project, sslSocketFactory, instance);
                juryProjectsDetailsAsyncTask.execute(Utils.buildUrlForLIJUR(username, token), "GET");
            }
        });
    }


    /**
     * Create a ProjectDetailFragment
     * Wich present main information of a given project.
     */
    private void createProjectDetailFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = JuryProjectDetailFragment.newInstance(project);
        ft.add(R.id.project_detail_container,fragment, "project");
        ft.commit();
    }

    /**
     * Create StudentsIdentityFragment for each student present in the studentsGroup
     */
    private void createStudentsIdentityFragments(){
        for(UserModel user : studentsGroup.getMembers()){
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            Fragment fragment2 = IdentityStudentsFragmentVote.newInstance(project,user);
            ft2.add(R.id.student_container, fragment2, "student");
            ft2.commit();
        }
    }


    /**
     * Create a TextView for each MarkVisitor find in the database
     * If there is no marks, display a default message
     */
    private void presentMarksFromDB(){
        // For each marks, adding a textView
        if(projectMarks.size() > 0){
            for(MarksJury mark : projectMarks){
                final TextView markView = new TextView(getApplicationContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 20, 0, 0);
                markView.setLayoutParams(params);
                markView.setText(String.valueOf(mark.getMark()) + " / 20");
                markView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
                Typeface face = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto);
                markView.setTypeface(face);

                // Adding the textview in the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            marksResults.addView(markView);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        }else{
            // If there is no marks, adding a default message
            final TextView markView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 20, 0, 0);
            markView.setLayoutParams(params);
            markView.setText("Aucune note sur ce projet");
            markView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteText));
            Typeface face = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto);
            markView.setTypeface(face);

            // Adding the textview in the main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        marksResults.addView(markView);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /**
     * Get Jury Marks and  from database
     * Stock values in list of MarkJury
     * Call presentMarksFromDB() and presentCommentsFromDB()
     */
       private void updateMarks(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                // Get comments and marks from database
                projectMarks = EseoDatabase.getDatabase(getApplicationContext()).marksJuryDAO().getMarks(project.getProjectId());

                // Create TextView for each marks
                presentMarksFromDB();


            }
        });
    }

}
