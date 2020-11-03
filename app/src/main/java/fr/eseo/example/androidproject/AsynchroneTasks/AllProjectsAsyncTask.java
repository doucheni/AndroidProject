package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.AllProjectsActivity;
import fr.eseo.example.androidproject.api.Utils;

public class AllProjectsAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private final AllProjectsActivity projectActivity;
    private final SSLSocketFactory sslSocketFactory;

    public AllProjectsAsyncTask(AllProjectsActivity projectActivity, SSLSocketFactory sslSocketFactory){
        this.projectActivity = projectActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected JSONObject doInBackground(String... params){
        JSONObject jsonObject;
        String urlProjects = params[0];
        String method = params[1];

        InputStream inputStream = Utils.sendRequestWS(urlProjects, method, this.sslSocketFactory);
        jsonObject = Utils.getJSONFromString(Utils.readStream(inputStream));

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject){
        this.projectActivity.treatmentResult(jsonObject);
    }
}