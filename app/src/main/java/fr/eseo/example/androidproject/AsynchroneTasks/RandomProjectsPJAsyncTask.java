package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.ProjectCommActivity;
import fr.eseo.example.androidproject.api.Utils;

public class RandomProjectsPJAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private ProjectCommActivity projectCommActivity;
    private SSLSocketFactory sslSocketFactory;

    public RandomProjectsPJAsyncTask(ProjectCommActivity projectCommActivity, SSLSocketFactory sslSocketFactory){
        this.projectCommActivity = projectCommActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        JSONObject jsonObject = null;

        String urlRequest = strings[0];
        String method = strings[1];

        InputStream inputStream = Utils.sendRequestWS(urlRequest, method, this.sslSocketFactory);
        jsonObject = Utils.getJSONFromString(Utils.readStream(inputStream));

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        projectCommActivity.treatmentResultPORTE(jsonObject);
    }
}
