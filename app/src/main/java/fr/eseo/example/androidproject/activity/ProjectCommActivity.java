package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.CommAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.ProjectsDetailsCommFragment;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.room.entities.Jury;
import fr.eseo.example.androidproject.room.entities.Project;
import fr.eseo.example.androidproject.room.entities.User;

public class ProjectCommActivity extends AppCompatActivity {

    public static final String PROJECT_ID = "Project_id";
    public SSLSocketFactory sslSocketFactory;
    public Toast errorRequestToast;
    private Toast errorResultToast;
    private CommAsyncTask commAsyncTask;
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
        errorRequestToast = Toast.makeText(ProjectCommActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(ProjectCommActivity.this, "Projects/Jury are not available", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_com_projects);
        commAsyncTask = new CommAsyncTask(this, sslSocketFactory);
        this.progressDialog = ProgressDialog.show(ProjectCommActivity.this,"Loading", "Please wait ...", true);
        String request = "https://172.24.5.16/pfe/webservice.php?q=LIJUR&user="+this.username+"&token="+this.token;
        commAsyncTask.execute(request, "GET");
    }

    public void treatmentResult(JSONObject jsonObject){
        List<User> users = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        List<Jury> juries = new ArrayList<>();
        try{
            if(jsonObject.get("result").equals("KO")){
                errorResultToast.show();
            }else{
                JSONArray jsonJuries = jsonObject.getJSONArray("juries");
                for(int i = 0; i < jsonJuries.length(); i++){
                    JSONObject jsonJury = jsonJuries.getJSONObject(i);
                    int jury_id = jsonJury.getInt("idJury");
                    String jury_date = jsonJury.getString("date");
                    JSONObject jsonInfo = jsonJury.getJSONObject("info");
                    JSONArray jsonMembers = jsonInfo.getJSONArray("members");
                    JSONArray jsonProjects = jsonInfo.getJSONArray("projects");

                    Jury jury = new Jury(jury_id, jury_date);
                    juries.add(jury);

                    // Creation of user who are members of Jury
                    for(int j = 0; j < jsonMembers.length(); j++){
                        JSONObject jsonUser = jsonMembers.getJSONObject(j);
                        int user_id = jsonUser.getInt("idUser");
                        String forename = jsonUser.getString("forename");
                        String surname = jsonUser.getString("surname");
                        users.add(new User(user_id, forename, surname));
                    }

                    // Creation of a list of project not confidential
                    for(int k = 0; k < jsonProjects.length(); k++){
                        JSONObject jsonProject = jsonProjects.getJSONObject(k);
                        int project_id = jsonProject.getInt("projectId");
                        String project_title = jsonProject.getString("title");
                        int project_confid = jsonProject.getInt("confid");
                        Boolean project_poster = jsonProject.getBoolean("poster");
                        JSONObject jsonSupervisor = jsonProject.getJSONObject("supervisor");
                        String project_supervisor = jsonSupervisor.getString("forename") + " " + jsonSupervisor.getString("surname");
                        if(project_confid == 0) {
                            projects.add(new Project(project_id, project_title, "", project_poster, project_confid, project_supervisor));
                        }
                    }
                }


                for(int i = 0; i < projects.size(); i++){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = ProjectsDetailsCommFragment.newInstance(projects.get(i));
                    ft.add(R.id.project_container,fragment, "project-"+i);
                    ft.commit();
                }




                /*
                List<Project> listProjects = new ArrayList<>();
                for(int i = 0; i < jsonProject.length(); i++){
                    JSONObject project = jsonProject.getJSONObject(i);

                    // Creation Project object

                    int project_id = project.getInt("projectId");
                    String project_title = project.getString("title");
                    String project_desc = project.getString("descrip");
                    Boolean poster = project.getBoolean("poster");
                    JSONArray jsonSupervisor = project.getJSONArray("supervisor");
                    String supervisor = jsonSupervisor.getString(0) + " " + jsonSupervisor.getString(1);

                }
                 */
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        this.progressDialog.dismiss();
    }
}
