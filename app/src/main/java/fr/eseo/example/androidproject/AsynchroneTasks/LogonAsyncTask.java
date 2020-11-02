package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.logon;
import fr.eseo.example.androidproject.api.Utils;

/**
 * AsyncTask for logo activity
 * Send a LOGON request to ESEO's API
 * get the token of the user if he is register in the database
 */
public class LogonAsyncTask extends AsyncTask<String, Void, JSONObject> {

    // Instance of logon activity
    private logon logon;

    // SSLSocketFactory configuration (certificate)
    private SSLSocketFactory sslSocketFactory;

    public LogonAsyncTask(logon logonActivity, SSLSocketFactory sslSocketFactory){
        this.logon = logonActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    // Run before execution
    @Override
    protected void onPreExecute() {
    }

    /**
     * Execution, send a LOGON request
     * with the given username and password, verify that the user is register
     * if he is register, give a token
     * @param strings, String url and String method (GET, POST, ...)
     * @return a JSONObject, the request's result if JSON format
     */
    @Override
    protected JSONObject doInBackground(String... strings) {
        return Utils.getJSONFromString(Utils.readStream(Utils.sendRequestWS(strings[0], strings[1], this.sslSocketFactory)));
    }

    /**
     * Run after execution
     * Call the method treatmentResultLOGON from logon activity
     * @param jsonObject, the request's result if JSON format
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        this.logon.treatmentResultLOGON(jsonObject);
    }
}
