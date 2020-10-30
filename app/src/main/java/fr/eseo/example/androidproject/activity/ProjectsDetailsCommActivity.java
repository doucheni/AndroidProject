package fr.eseo.example.androidproject.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.JuryProjectCommAsyncTask;
import fr.eseo.example.androidproject.fragments.JuryProjectDetailsComm;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.IdentityStudentsFragment;
import fr.eseo.example.androidproject.fragments.PosterFragment;
import fr.eseo.example.androidproject.fragments.ProjectDetailFragment;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.room.entities.CommentsVisitor;
import fr.eseo.example.androidproject.room.entities.MarksVisitor;

public class ProjectsDetailsCommActivity extends AppCompatActivity {

    private static final String ARG_PROJECT = "project";
    private static final String ARG_STUDENTS = "students";
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_JURY = "jury";

    JuryProjectCommAsyncTask juryProjectCommAsyncTask;
    ProjectsDetailsCommActivity instance;

    private SSLSocketFactory sslSocketFactory;
    private ProgressDialog progressDialog;

    private String token;
    private String username;
    private ProjectModel project;
    private StudentsGroup studentsGroup;

    private Button buttonPoster;
    private Button buttonMore;
    private Button buttonExport;

    private LinearLayout marksContainer;
    private LinearLayout commentsContainer;

    private List<MarksVisitor> projectMarks;
    private List<CommentsVisitor> projectComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_details_comm);

        Intent intent = getIntent();
        token = intent.getStringExtra(ARG_TOKEN);
        project = (ProjectModel) intent.getSerializableExtra(ARG_PROJECT);
        username = intent.getStringExtra(ARG_USERNAME);
        studentsGroup = (StudentsGroup) intent.getSerializableExtra(ARG_STUDENTS);
        sslSocketFactory = Utils.configureSSLContext(this.getApplicationContext()).getSocketFactory();
        instance = this;
        buttonPoster = findViewById(R.id.button_poster);
        buttonMore = findViewById(R.id.button_details);
        marksContainer = findViewById(R.id.marks_container);
        commentsContainer = findViewById(R.id.comments_container);
        buttonExport = findViewById(R.id.btn_export);

        buttonPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogPoster = PosterFragment.newInstance(project, username, token);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogPoster.show(ft, "poster");
            }
        });

        buttonMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                juryProjectCommAsyncTask = new JuryProjectCommAsyncTask(project, sslSocketFactory, instance);
                String url = Utils.buildUrlForLIJUR(username, token);
                juryProjectCommAsyncTask.execute(url, "GET");
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ProjectDetailFragment.newInstance(project);
        ft.add(R.id.project_detail_container,fragment, "project");
        ft.commit();

        for(UserModel user : studentsGroup.getMembers()){
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            Fragment fragment2 = IdentityStudentsFragment.newInstance(user);
            ft2.add(R.id.student_container, fragment2, "student");
            ft2.commit();
        }

        if(!project.getProjectPoster()){
            buttonPoster.setEnabled(false);
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                // Get comments and marks from database
                projectMarks = EseoDatabase.getDatabase(getApplicationContext()).marksVisitorDAO().getMarks(project.getProjectId());
                projectComments = EseoDatabase.getDatabase(getApplicationContext()).commentsVisitorDAO().getComments(project.getProjectId());

                // For each marks, adding a textView
                if(projectMarks.size() > 0){
                    for(MarksVisitor mark : projectMarks){
                        final TextView markView = new TextView(getApplicationContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10, 20, 0, 0);
                        markView.setLayoutParams(params);
                        markView.setText(String.valueOf(mark.getMark()) + " / 20");

                        // Adding the textview in the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    marksContainer.addView(markView);
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

                    // Adding the textview in the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                marksContainer.addView(markView);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

                // For each comments, adding a text view
                if(projectComments.size() > 0){
                    for(CommentsVisitor comment : projectComments){
                        final TextView commentView = new TextView(getApplicationContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10, 20, 0, 0);
                        commentView.setLayoutParams(params);
                        commentView.setText(comment.getComment());

                        // Adding the textView in the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    commentsContainer.addView(commentView);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                    // If there is no comments : adding a default message
                }else{
                    final TextView commentView = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 20, 0, 0);
                    commentView.setLayoutParams(params);
                    commentView.setText("Aucun commentaire pour ce projet");

                    // Adding the textView in the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                commentsContainer.addView(commentView);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(projectMarks.size() > 0 | projectComments.size() > 0){
                    createFile("Project_"+project.getProjectId()+"_Review");
                }else{
                    Toast.makeText(getApplicationContext(), "Impossible d'exporter lorsqu'il n'y a pas de données", Toast.LENGTH_LONG);
                }
            }
        });

        System.out.println(ProjectsDetailsCommActivity.this.getFilesDir().getAbsolutePath());

    }

    public void presentJury(JuryModel juryModel){
        DialogFragment dialogJury = JuryProjectDetailsComm.newInstance(juryModel);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialogJury.show(ft, "jury");
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File gpxfile = new File(Environment.getStorageDirectory(), "PFE_JPO/" + sFileName);
            if(!gpxfile.exists()){
                gpxfile.mkdirs();
                gpxfile.createNewFile();
            }
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void WriteBtn(String content, String fileName, Context context) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(content);
            outputWriter.close();
            //display file saved message
            Toast.makeText(context, "File saved successfully!",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int WRITE_REQUEST_CODE = 101;

    private void createFile(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

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

        if(projectComments.size() > 0){
            content += "Commentaire(s) : ";
        }

        for(int j = 0; j < projectComments.size(); j++) {
            if (j < projectComments.size() - 1) {
                content += projectComments.get(j).getComment() + "; ";
            } else {
                content += projectComments.get(j).getComment();
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
}

