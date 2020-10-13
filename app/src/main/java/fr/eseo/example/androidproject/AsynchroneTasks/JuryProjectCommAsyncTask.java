package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.ProjectsDetailsCommActivity;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.ProjectModel;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;

public class JuryProjectCommAsyncTask extends AsyncTask<String, Void, JuryModel> {

    ProjectsDetailsCommActivity projectsDetailsCommActivity;
    ProjectModel project;
    SSLSocketFactory sslSocketFactory;

    public JuryProjectCommAsyncTask(ProjectModel project, SSLSocketFactory sslSocketFactory, ProjectsDetailsCommActivity projectsDetailsCommActivity){
        this.project = project;
        this.sslSocketFactory = sslSocketFactory;
        this.projectsDetailsCommActivity = projectsDetailsCommActivity;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected JuryModel doInBackground(String... params){
        JuryModel jury = null;

        String urlRequest = params[0];
        String method = params[1];

        InputStream inputStream = Utils.sendRequestWS(urlRequest, method, this.sslSocketFactory);
        JSONObject resultFromStream = Utils.getJSONFromString(Utils.readStream(inputStream));

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

    @Override
    protected void onPostExecute(JuryModel juryModel){
        //this.projectsDetailsCommActivity.updateActivityViews(juryModel);
    }


}
