package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.logon;
import fr.eseo.example.androidproject.api.Utils;

public class UserStatusAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private logon logon;
    private SSLSocketFactory sslSocketFactory;

    public UserStatusAsyncTask(logon logon, SSLSocketFactory sslSocketFactory){
        this.logon = logon;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        return Utils.getJSONFromString(Utils.readStream(Utils.sendRequestWS(strings[0], strings[1], this.sslSocketFactory)));
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        this.logon.treatmenResultMYINF(jsonObject);
    }
}
