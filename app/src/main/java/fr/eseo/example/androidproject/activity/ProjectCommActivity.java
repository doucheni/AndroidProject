package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.CommAsyncTask;
import fr.eseo.example.androidproject.AsynchroneTasks.JuryAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.room.entities.Project;

public class ProjectCommActivity extends AppCompatActivity {

    public static final String PROJECT_ID = "Project_id";
    public SSLSocketFactory sslSocketFactory;
    public Toast errorRequestToast;
    private Toast errorResultToast;
    private CommAsyncTask commAsyncTask;
    private ProgressDialog progressDialog;
    private String token;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.token = intent.getStringExtra("TOKEN");
        this.username = intent.getStringExtra("USERNAME");
        final Context ctx = getApplicationContext();
        sslSocketFactory = Utils.configureSSLContext(ctx).getSocketFactory();
        errorRequestToast = Toast.makeText(ProjectCommActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(ProjectCommActivity.this, "Projects are not available", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_com_projects);
        commAsyncTask = new CommAsyncTask(this, sslSocketFactory);
        this.progressDialog = ProgressDialog.show(ProjectCommActivity.this,"Loading", "Please wait ...", true);
        commAsyncTask.execute("https://172.24.5.16/pfe/webservice.php?q=LIPRJ&user="+username+"&token="+token, "GET");
    }

    public void treatmentResultProject(JSONObject jsonObject){
        try{
            if(jsonObject.get("result").equals("KO")){
                errorResultToast.show();
            }else{
                JSONArray jsonProject = jsonObject.getJSONArray("projects");
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
                Log.d("test", "in treatment project, run jury request");
                JuryAsyncTask juryAsyncTask = new JuryAsyncTask(this, sslSocketFactory, jsonProject);
                String requestJury = "https://172.24.5.16/pfe/webservice.php?q=LIJUR&user="+this.username+"&token="+this.token;
                juryAsyncTask.execute(requestJury, "GET");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void treatmentResultJury(JSONArray jsonProject, JSONObject jsonJury){
        Log.d("jury", jsonJury.toString());
        this.progressDialog.dismiss();
    }
}
