package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;
import org.json.JSONObject;
import javax.net.ssl.SSLSocketFactory;
import fr.eseo.example.androidproject.activity.ProjectCommActivity;
import fr.eseo.example.androidproject.api.Utils;

/**
 * AsyncTask class for ProjectCommActivity
 * Send a request PORTE to ESEO's API
 */
public class RandomProjectsPJAsyncTask extends AsyncTask<String, Void, JSONObject> {

    // Instance of ProjectCommActivity
    private ProjectCommActivity projectCommActivity;

    // SSLSocket configuration (certificate)
    private SSLSocketFactory sslSocketFactory;

    /**
     * Constructor of class RandomProjectsPJAsyncTask
     * @param projectCommActivity, instance of ProjectCommActivity
     * @param sslSocketFactory, the SSLSocketFactory configuration
     */
    public RandomProjectsPJAsyncTask(ProjectCommActivity projectCommActivity, SSLSocketFactory sslSocketFactory){
        this.projectCommActivity = projectCommActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    // Run before execution
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Execution, get a JSONObject from request's result
     * @param strings, String URL and String method (GET, POST, ...)
     * @return jsonObject, the request's result in JSONObject format
     */
    @Override
    protected JSONObject doInBackground(String... strings) {
        return Utils.getJSONFromString(Utils.readStream(Utils.sendRequestWS(strings[0], strings[1], this.sslSocketFactory)));
    }

    /**
     * Run after execution
     * Call treatmentResultPORTE from ProjectCommActivity
     * @param jsonObject, the request's result in JSONObject format
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        projectCommActivity.treatmentResultPORTE(jsonObject);
    }
}
