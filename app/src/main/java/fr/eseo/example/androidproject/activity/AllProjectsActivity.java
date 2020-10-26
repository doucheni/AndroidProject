package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.AllProjectsAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.AllProjectsFragment;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.api.JuryModel;


public class AllProjectsActivity extends AppCompatActivity {

    public static final String PROJECT_ID = "Project_id";

    public SSLSocketFactory sslSocketFactory;
    public Toast errorRequestToast;
    private Toast errorResultToast;
    private AllProjectsAsyncTask commAsyncTask;
    private ProgressDialog progressDialog;
    private String token;
    private String username;
    private EseoDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.token = intent.getStringExtra("TOKEN");
        this.username = intent.getStringExtra("USERNAME");
        final Context ctx = getApplicationContext();
        sslSocketFactory = Utils.configureSSLContext(ctx).getSocketFactory();
        errorRequestToast = Toast.makeText(AllProjectsActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(AllProjectsActivity.this, "Projects/Jury are not available", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_all_projects);
        commAsyncTask = new AllProjectsAsyncTask(this, sslSocketFactory);
        this.progressDialog = ProgressDialog.show(AllProjectsActivity.this,"Loading", "Please wait ...", true);
        String requestProject = "https://172.24.5.16/pfe/webservice.php?q=LIPRJ&user="+this.username+"&token="+this.token;
        commAsyncTask.execute(requestProject, "GET");
    }

    public void treatmentResult(JSONObject jsonObject){
        List<ProjectModel> projects = new ArrayList<>();
        HashMap<Integer, JuryModel> membersJury = new HashMap<>();
        try{
            if(jsonObject.get("result").equals("KO")){
                errorResultToast.show();
            }else{
                JSONArray jsonProjects = jsonObject.getJSONArray("projects");
                for(int i = 0; i < jsonProjects.length(); i++){
                    JSONObject jsonProject = jsonProjects.getJSONObject(i);
                    int project_id = jsonProject.getInt("projectId");
                    String project_title = jsonProject.getString("title");
                    String project_description = jsonProject.getString("descrip");
                    int project_confid = jsonProject.getInt("confid");
                    Boolean project_poster = jsonProject.getBoolean("poster");
                    JSONObject jsonSupervisor = jsonProject.getJSONObject("supervisor");
                    String project_supervisor = jsonSupervisor.getString("forename") + " " + jsonSupervisor.getString("surname");
                    projects.add(new ProjectModel(project_id, project_title, project_description, project_poster, project_confid, project_supervisor));
                }

                for(int i = 0; i < projects.size(); i++){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = AllProjectsFragment.newInstance(projects.get(i), this.username, this.token);
                    ft.add(R.id.project_container,fragment, "project-"+i);
                    ft.commit();
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        this.progressDialog.dismiss();
    }
}