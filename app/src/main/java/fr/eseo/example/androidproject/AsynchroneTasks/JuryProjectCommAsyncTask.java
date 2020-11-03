package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.ProjectsDetailsCommActivity;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;

/**
 * AsyncTask for ProjectsDetailsCommActivity
 * Send a LIJUR request to ESEO's API
 * Get the Jury of a specific project
 */
public class JuryProjectCommAsyncTask extends AsyncTask<String, Void, JuryModel> {

    // Instance of ProjectsDetailsCommActivity
    ProjectsDetailsCommActivity projectsDetailsCommActivity;

    // The project we want to find jury
    ProjectModel project;

    // SSLSocket configuration (certificate)
    SSLSocketFactory sslSocketFactory;

    /**
     * Constructor of the class JuryProjectCommAsyncTask
     * @param project, ProjectModel we want the jury
     * @param sslSocketFactory, SSLSocketFactory configuration
     * @param projectsDetailsCommActivity, instance of ProjectDetailsCommActivity
     */
    public JuryProjectCommAsyncTask(ProjectModel project, SSLSocketFactory sslSocketFactory, ProjectsDetailsCommActivity projectsDetailsCommActivity){
        this.project = project;
        this.sslSocketFactory = sslSocketFactory;
        this.projectsDetailsCommActivity = projectsDetailsCommActivity;
    }

    // Run before execution
    @Override
    protected void onPreExecute(){

    }

    /**
     * Execution, send a LIJUR request to ESEO's API
     * Get an InputStream result
     * Convert it into String format
     * Convert it into JSONObject
     * Build a JuryModel object with this JSONObject
     * Return this JuryModel
     * @param params, String url and String method (GET, POST, ...)
     * @return jury, the project's jury
     */
    @Override
    protected JuryModel doInBackground(String... params){
        JuryModel jury = null;

        JSONObject resultFromStream = Utils.getJSONFromString(
                Utils.readStream(
                        Utils.sendRequestWS(params[0], params[1], this.sslSocketFactory)
                )
        );
        return this.getJuryModelFromJSON(resultFromStream);
    }

    /**
     * Run after Execution
     * Call ProjectsDetailsCommActivity.presentJury(JuryModel juryModel)
     * To Display the JuryModel in a DialogFragment
     * @param juryModel, JuryModel obtain from request
     */
    @Override
    protected void onPostExecute(JuryModel juryModel){
        this.projectsDetailsCommActivity.presentJury(juryModel);
    }

    /**
     * Build a JuryModel object from the JSONObject request's result
     * @param resultFromStream the JSONObject from result's InputStream
     * @return jury, the JuryModel of the project
     */
    private JuryModel getJuryModelFromJSON(JSONObject resultFromStream){
        JuryModel jury = null;
        try {
            JSONArray jsonJuries = resultFromStream.getJSONArray("juries");
            for(int i = 0; i< jsonJuries.length(); i++){
                List<UserModel> membersJury = new ArrayList<>();

                JSONObject jsonJury = jsonJuries.getJSONObject(i);
                int juryId = jsonJury.getInt("idJury");
                String date = jsonJury.getString("date");
                JSONObject jsonInfo = jsonJury.getJSONObject("info");
                JSONArray jsonProjects = jsonInfo.getJSONArray("projects");


                for(int j = 0; j < jsonProjects.length(); j++){
                    JSONObject jsonProject = jsonProjects.getJSONObject(j);
                    if(this.project.getProjectId() == jsonProject.getInt("projectId")){
                        JSONArray jsonMembers = jsonInfo.getJSONArray("members");
                        for(int k = 0; k< jsonMembers.length(); k++){
                            JSONObject jsonUser = jsonMembers.getJSONObject(k);
                            membersJury.add(new UserModel(jsonUser.getInt("idUser"), jsonUser.getString("forename"), jsonUser.getString("surname")));
                        }
                        jury = new JuryModel(juryId, date, membersJury);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jury;
    }


}
