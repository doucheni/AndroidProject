package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

/**
 * Class for the main activity of a Communication Member
 * In this Activity, the user can :
 *  See all projects
 *  Create a pseudo-jury manually
 *  Create a pseudo-jury automatically
 *  Select a project to see more details (poster, description, students, jury, ...)
 */
public class ProjectCommActivity extends AppCompatActivity {

    // SSLSocketFactory configured with certificate
    public SSLSocketFactory sslSocketFactory;

    // Toasts for error
    public Toast errorRequestToast;
    private Toast errorResultToast;

    // Instance of CommAsyncTask for LIPRJ request
    private CommAsyncTask commAsyncTask;

    // Loading ProgressDialog during request
    private ProgressDialog progressDialog;

    // Intent's data
    private String token;
    private String username;

    // List of ProjectModel selected by the user
    private List<ProjectModel> projectsSelected = new ArrayList<>();

    // List of PseudoJuryModel created by the user
    private List<PseudoJuryModel> pseudoJuryModelList = new ArrayList<>();

    // Views from XML Layout
    private Button btnNewPJ;
    private Button btnAutoPJ;
    private LinearLayout pseudoJuryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_projects);

        // Initialization of each ProjectCommActivity's variables
        this.initClassVariables();

        // Run AsyncTask for the connection to the ESEO's API
        this.runCommAsynTask();

        // Add the a OnClickListener on btnNewPJ
        this.initBtnNewPJAction();

        // Add a OnClickListener on btnAutoPJ
        this.initBtnAutoPJAction();

    }

    /**
     * Function wich obtain the result of the LIPRJ request (API)
     * Create a Fragment for each project in the result
     * @param jsonObject, the result in JSONObject format
     */
    public void treatmentResultLIPRJ(JSONObject jsonObject){
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

    /**
     * Adding a OnClickListener for button of New Pseudo-Jury's creation
     * Creation of a new PseudoJury
     * Add it in the list of PseudoJury
     * Creation of a new fragment for the new PseudoJury
     */
    private void initBtnNewPJAction(){
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
    }

    /**
     * Adding a OnClickListener for button of Auto Pseudo-Jury's creation
     * Send a PORTE request to get several random project
     * Create a new PseudoJury
     * Add it in the list of PseudoJury
     * Creation of a new fragment for the PseudoJury
     */
    private void initBtnAutoPJAction(){
        btnAutoPJ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RandomProjectsPJAsyncTask randomProjectsPJAsyncTask = new RandomProjectsPJAsyncTask(ProjectCommActivity.this, sslSocketFactory);
                progressDialog = ProgressDialog.show(ProjectCommActivity.this,"Loading", "Please wait ...", true);
                randomProjectsPJAsyncTask.execute(Utils.buildUrlForPORTE(username, token), "GET");
            }
        });
    }

    /**
     * Initialization of each variables from the class ProjectCommActivity
     */
    private void initClassVariables(){
        // Get intent's data from Logon activity
        Intent intent = getIntent();
        this.token = intent.getStringExtra("TOKEN");
        this.username = intent.getStringExtra("USERNAME");

        // Configuration of the sslSocket
        sslSocketFactory = Utils.configureSSLContext(getApplicationContext()).getSocketFactory();

        // Toast's initialization
        errorRequestToast = Toast.makeText(ProjectCommActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(ProjectCommActivity.this, "Projects/Jury are not available", Toast.LENGTH_LONG);

        // Views initialization
        pseudoJuryContainer = findViewById(R.id.pseudojury_container_main);
        btnNewPJ = findViewById(R.id.btn_newPJ);
        btnAutoPJ = findViewById(R.id.btn_autoPJ);
    }

    /**
     * Run CommAsyncTask and display the ProgressDialog during the request
     */
    private void runCommAsynTask(){
        // Init CommAsyncTask
        this.commAsyncTask = new CommAsyncTask(this, this.sslSocketFactory);

        // Showing the ProgressDialog Loading
        this.progressDialog = ProgressDialog.show(ProjectCommActivity.this,"Loading", "Please wait ...", true);

        // Execution of CommAsynTask
        commAsyncTask.execute(Utils.buildUrlForLIPRJ(this.username, this.token), "GET");
    }

}
