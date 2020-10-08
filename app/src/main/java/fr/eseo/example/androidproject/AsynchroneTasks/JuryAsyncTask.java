package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.ProjectCommActivity;
import fr.eseo.example.androidproject.api.Utils;

public class JuryAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private ProjectCommActivity commActivity;
    private SSLSocketFactory sslSocketFactory;
    private JSONArray arrayProject;

    public JuryAsyncTask(ProjectCommActivity commActivity, SSLSocketFactory sslSocketFactory, JSONArray arrayProject){
        this.commActivity = commActivity;
        this.sslSocketFactory = sslSocketFactory;
        this.arrayProject = arrayProject;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected JSONObject doInBackground(String... params){
        JSONObject jsonObject = null;
        String urlString = params[0];
        String method = params[1];

        InputStream inputStream = Utils.sendRequestWS(urlString, method, this.sslSocketFactory);
        jsonObject = Utils.getJSONFromString(Utils.readStream(inputStream));

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject){
        this.commActivity.treatmentResultJury(this.arrayProject, jsonObject);
    }
}
