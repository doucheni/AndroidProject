package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;
import org.json.JSONObject;
import javax.net.ssl.SSLSocketFactory;
import fr.eseo.example.androidproject.activity.ProjectCommActivity;
import fr.eseo.example.androidproject.api.Utils;

/**
 * AsynTask class for ProjectCommActivity
 * Send a request LIPRJ to ESEO's API
 */
public class CommAsyncTask extends AsyncTask<String, Void, JSONObject> {

    // Instance of ProjectCommActivity
    private ProjectCommActivity commActivity;

    // SSLSocket configuration (certificate)
    private SSLSocketFactory sslSocketFactory;


    /**
     * Constructor of Class CommAsyncTask
     * @param commActivity, instance of ProjectCommActivity
     * @param sslSocketFactory, the SSLSocketFactory configuration
     */
    public CommAsyncTask(ProjectCommActivity commActivity, SSLSocketFactory sslSocketFactory){
        this.commActivity = commActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    // Run before execution
    @Override
    protected void onPreExecute(){
    }

    /**
     * Execution, get a JSONObject from the request's result
     * @param params, String URL and String method (GET, POST, ...)
     * @return jsonObject, the request's result in JSONObject format
     */
    @Override
    protected JSONObject doInBackground(String... params){
        return Utils.getJSONFromString(Utils.readStream(Utils.sendRequestWS(params[0], params[1], this.sslSocketFactory)));
    }

    /**
     * Run after execution
     * Call treatmentResultLIPRJ from ProjectCommActivity
     * @param jsonObject, the request's result in JSONObject format
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject){
        this.commActivity.treatmentResultLIPRJ(jsonObject);
    }
}
