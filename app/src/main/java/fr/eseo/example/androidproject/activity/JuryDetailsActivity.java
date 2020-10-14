package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.JuryDetailsAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.JuryDetailsFragment;
import fr.eseo.example.androidproject.room.entities.Jury;

public class JuryDetailsActivity extends AppCompatActivity {

    private SSLSocketFactory sslSocket;
    public Toast errorRequestToast;
    private Toast errorResultToast;
    private ProgressDialog progressDialog;
    private JuryDetailsAsyncTask juryDetailsAsyncTask;
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_JURY = "jury";
    private String token;
    private String username;
    private String jury;
    int jury_id;
    int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentDetails = getIntent();
        token = intentDetails.getStringExtra(ARG_TOKEN);
        jury = intentDetails.getStringExtra(ARG_JURY);
        username = intentDetails.getStringExtra(ARG_USERNAME);

        final Context ctx = getApplicationContext();
        sslSocket = Utils.configureSSLContext(ctx).getSocketFactory();
        errorRequestToast = Toast.makeText(JuryDetailsActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(JuryDetailsActivity.this, "You don't have projects to see", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_jury_details);
        juryDetailsAsyncTask = new JuryDetailsAsyncTask(this, sslSocket);
        this.progressDialog = ProgressDialog.show(JuryDetailsActivity.this, "Loading", "Please wait ...", true);
        juryDetailsAsyncTask.execute("https://172.24.5.16/pfe/webservice.php?q=MYJUR&user=" + username + "&token=" + token, "GET");


    }

    public void treatmentResultMyDetailsJury(JSONObject jsonObject) {
        List<Jury> juries = new ArrayList<>();
        List<ProjectModel> projects = new ArrayList<>();

        try {
            if (jsonObject.get("result").equals("KO")) {
                errorResultToast.show();
            } else {
                JSONArray jsonJuries = jsonObject.getJSONArray("juries");
                for(int i = 0; i < jsonJuries.length(); i++) {
                    JSONObject jsonJury = jsonJuries.getJSONObject(i);
                    jury_id = jsonJury.getInt("idJury");
                    String jury_date = jsonJury.getString("date");
                    JSONObject jsonInfo = jsonJury.getJSONObject("info");
                    JSONArray jsonMembers = jsonInfo.getJSONArray("members");
                    JSONArray jsonProjects = jsonInfo.getJSONArray("projects");
                    for(int p = 0; p < jsonProjects.length(); p++) {
                        JSONObject jsonProject = jsonProjects.getJSONObject(p);
                        projectId = jsonProject.getInt("projectId");
                        String title = jsonProject.getString("title");
                        int confid = jsonProject.getInt("confid");
                        boolean poster = jsonProject.getBoolean("poster");

                        ProjectModel project = new ProjectModel(projectId,title,"",poster,confid,"");
                        projects.add(project);

                    }

                }

            }
            for(int i = 0; i < projects.size(); i++){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = JuryDetailsFragment.newInstance(projects.get(i));
                ft.add(R.id.jury_detail_container,fragment, "Project-"+i);
                ft.commit();


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.progressDialog.dismiss();

    }
}