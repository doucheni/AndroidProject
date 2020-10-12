package fr.eseo.example.androidproject.AsynchroneTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.ProjectCommActivity;
import fr.eseo.example.androidproject.api.Utils;

public class CommAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private ProjectCommActivity commActivity;
    private SSLSocketFactory sslSocketFactory;

    public CommAsyncTask(ProjectCommActivity commActivity, SSLSocketFactory sslSocketFactory){
        this.commActivity = commActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected JSONObject doInBackground(String... params){
        JSONObject jsonObject = null;
        String urlProjects = params[0];
        String method = params[1];

        InputStream inputStream = Utils.sendRequestWS(urlProjects, method, this.sslSocketFactory);
        jsonObject = Utils.getJSONFromString(Utils.readStream(inputStream));

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject){
        this.commActivity.treatmentResult(jsonObject);
    }
}
