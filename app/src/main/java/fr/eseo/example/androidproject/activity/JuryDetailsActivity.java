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
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.JuryDetailsAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.StudentsGroup;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.JuryDetailsFragment;

public class JuryDetailsActivity extends AppCompatActivity {

    public Toast errorRequestToast;
    private Toast errorResultToast;
    private ProgressDialog progressDialog;
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_JURY = "jury";
    private String token;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentDetails = getIntent();
        token = intentDetails.getStringExtra(ARG_TOKEN);
        JuryModel jury = (JuryModel) intentDetails.getSerializableExtra(ARG_JURY);
        System.out.println("NUMERO "+jury.getJuryId());
        username = intentDetails.getStringExtra(ARG_USERNAME);

        final Context ctx = getApplicationContext();
        SSLSocketFactory sslSocket = Utils.configureSSLContext(ctx).getSocketFactory();
        errorRequestToast = Toast.makeText(JuryDetailsActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(JuryDetailsActivity.this, "You don't have projects to see", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_jury_details);
        JuryDetailsAsyncTask juryDetailsAsyncTask = new JuryDetailsAsyncTask(this, sslSocket);
        this.progressDialog = ProgressDialog.show(JuryDetailsActivity.this, "Loading", "Please wait ...", true);
        juryDetailsAsyncTask.execute("https://172.24.5.16/pfe/webservice.php?q=JYINF&user=" + username + "&jury="+ jury.getJuryId()+ "&token=" + token, "GET");


    }

    public void treatmentResultMyDetailsJury(JSONObject jsonObject) {
        List<ProjectModel> projects = new ArrayList<>();
        HashMap<Integer, StudentsGroup> groups = new HashMap<>();

        try {
            if (jsonObject.get("result").equals("KO")) {
                errorResultToast.show();
            } else {
                JSONArray jsonProjects = jsonObject.getJSONArray("projects");
                for(int i = 0; i < jsonProjects.length(); i++) {
                    JSONObject jsonProject = jsonProjects.getJSONObject(i);
                    int projectId = jsonProject.getInt("projectId");
                    String title = jsonProject.getString("title");
                    String descrip = jsonProject.getString("descrip");
                    int confid = jsonProject.getInt("confid");
                    Boolean poster = jsonProject.getBoolean("poster");
                    JSONObject jsonSupervisor = jsonProject.getJSONObject("supervisor");
                    String project_supervisor = jsonSupervisor.getString("forename") + " " + jsonSupervisor.getString("surname");
                    ProjectModel project = new ProjectModel(projectId,title,descrip,poster,confid,project_supervisor);
                    projects.add(project);
                    JSONArray jsonStudents = jsonProject.getJSONArray("students");
                    List<UserModel> studentsMember = new ArrayList<>();

                    for(int j = 0; j < jsonStudents.length(); j++){
                        JSONObject jsonStudent = jsonStudents.getJSONObject(j);
                        int userId = jsonStudent.getInt("userId");
                        String user_forename = jsonStudent.getString("forename");
                        String user_surname = jsonStudent.getString("surname");
                        studentsMember.add(new UserModel(userId, user_forename, user_surname));
                    }
                    StudentsGroup studentsGroup = new StudentsGroup(projectId, studentsMember);
                    groups.put(projectId, studentsGroup);
                }

            }
            for(int i = 0; i < projects.size(); i++){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = JuryDetailsFragment.newInstance(projects.get(i),groups.get(projects.get(i).getProjectId()),this.username, this.token);
                ft.add(R.id.jury_detail_container,fragment, "Project-"+i);
                ft.commit();


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.progressDialog.dismiss();

    }
}