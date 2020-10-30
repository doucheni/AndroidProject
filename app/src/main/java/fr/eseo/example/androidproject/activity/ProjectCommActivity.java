package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.CommAsyncTask;
import fr.eseo.example.androidproject.AsynchroneTasks.RandomProjectsPJAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.PseudoJuryModel;
import fr.eseo.example.androidproject.api.StudentsGroup;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.ProjectsListCommFragment;
import fr.eseo.example.androidproject.fragments.PseudoJuryProjectsComm;
import fr.eseo.example.androidproject.room.EseoDatabase;
import fr.eseo.example.androidproject.api.JuryModel;
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

    private List<ProjectModel> projectsSelected = new ArrayList<>();
    private List<PseudoJuryModel> pseudoJuryModelList = new ArrayList<>();

    private Button btnNewPJ;
    private Button btnAutoPJ;
    private LinearLayout pseudoJuryContainer;

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
        String requestProject = "https://172.24.5.16/pfe/webservice.php?q=LIPRJ&user="+this.username+"&token="+this.token;
        commAsyncTask.execute(requestProject, "GET");

        pseudoJuryContainer = findViewById(R.id.pseudojury_container_main);
        btnNewPJ = findViewById(R.id.btn_newPJ);
        btnAutoPJ = findViewById(R.id.btn_autoPJ);

        btnNewPJ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(projectsSelected.size() > 0){
                    PseudoJuryModel pseudoJuryModel = new PseudoJuryModel(projectsSelected);

                    if(pseudoJuryModelList.size() == 0){
                        pseudoJuryContainer.removeViewAt(1);
                    }

                    pseudoJuryModelList.add(pseudoJuryModel);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = PseudoJuryProjectsComm.newInstance(pseudoJuryModel, username, token);
                    ft.add(R.id.pseudojury_container_main, fragment, "pseudojury-" + (pseudoJuryModelList.size() - 1));
                    ft.commit();
                }else{
                    Toast.makeText(ProjectCommActivity.this, "Vous devez selectionner au moins un projet", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAutoPJ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RandomProjectsPJAsyncTask randomProjectsPJAsyncTask = new RandomProjectsPJAsyncTask(ProjectCommActivity.this, sslSocketFactory);
                String urlRequest = "https://172.24.5.16/pfe/webservice.php?q=PORTE&user=" + username + "&token=" + token;
                progressDialog = ProgressDialog.show(ProjectCommActivity.this,"Loading", "Please wait ...", true);
                randomProjectsPJAsyncTask.execute(urlRequest, "GET");
            }
        });

    }

    public void treatmentResult(JSONObject jsonObject){
        List<ProjectModel> projects = new ArrayList<>();
        HashMap<Integer, StudentsGroup> groups = new HashMap<>();
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

                    JSONArray jsonStudents = jsonProject.getJSONArray("students");
                    List<UserModel> studentsMember = new ArrayList<>();
                    for(int j = 0; j < jsonStudents.length(); j++){
                        JSONObject jsonStudent = jsonStudents.getJSONObject(j);
                        int userId = jsonStudent.getInt("userId");
                        String user_forename = jsonStudent.getString("forename");
                        String user_surname = jsonStudent.getString("surname");
                        studentsMember.add(new UserModel(userId, user_forename, user_surname));
                    }
                    StudentsGroup studentsGroup = new StudentsGroup(project_id, studentsMember);
                    groups.put(project_id, studentsGroup);
                }

                for(int i = 0; i < projects.size(); i++){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = ProjectsListCommFragment.newInstance(projects.get(i), groups.get(projects.get(i).getProjectId()), this.username, this.token);
                    ft.add(R.id.project_container,fragment, "project-"+i);
                    ft.commit();
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        this.progressDialog.dismiss();
    }

    /**
     * Add a ProjectModel to the list of ProjectModel that the user has selected
     * @param project the project to add
     */
    public void addProjectSelected(ProjectModel project){
        if(this.projectsSelected.size() < 5){
            this.projectsSelected.add(project);
        }else{
            Toast.makeText(ProjectCommActivity.this, "You can't select more than 5 projects", Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * Remove a ProjectModel from the list of ProjectModel that the user has selected
     * @param project the project to remove
     */
    public void removeProjectSelected(ProjectModel project){
        if(this.projectsSelected.contains(project)){
            this.projectsSelected.remove(project);
        }
    }

    /**
     * Function wich obtain the result of the PORTE request (API)
     * Create a new pseudojury model with random project
     * @param jsonObject the result of the request
     */
    public void treatmentResultPORTE(JSONObject jsonObject){
        List<ProjectModel> projectsRandom = new ArrayList<>();
        try{
            if(jsonObject.getString("result").equals("KO")){
                Toast.makeText(ProjectCommActivity.this, "Problème d'identification", Toast.LENGTH_LONG).show();
            }else{
                JSONArray jsonProjects = jsonObject.getJSONArray("projects");
                for(int i = 0; i < jsonProjects.length(); i++){
                    JSONObject jsonProject = jsonProjects.getJSONObject(i);
                    int project_id = jsonProject.getInt("idProject");
                    String project_title = jsonProject.getString("title");
                    String project_description = jsonProject.getString("description");
                    String poster = jsonProject.getString("poster");
                    ProjectModel projectModel = new ProjectModel();
                    projectModel.setProjectId(project_id);
                    projectModel.setProjectTitle(project_title);
                    projectModel.setProjectDescription(project_description);
                    projectModel.setProjectPoster(true);
                    projectModel.setProjectPosterString(poster);
                    projectsRandom.add(projectModel);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        if(pseudoJuryModelList.size() == 0){
            pseudoJuryContainer.removeViewAt(1);
        }

        PseudoJuryModel pseudoJuryModel = new PseudoJuryModel(projectsRandom);
        pseudoJuryModelList.add(pseudoJuryModel);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = PseudoJuryProjectsComm.newInstance(pseudoJuryModel, username, token);
        ft.add(R.id.pseudojury_container_main, fragment, "pseudojury-" + (pseudoJuryModelList.size() - 1));
        ft.commit();

        this.progressDialog.dismiss();
    }

}
