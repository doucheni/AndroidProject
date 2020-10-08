package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.JuryActivity;
import fr.eseo.example.androidproject.api.Utils;

public class myJuryAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private JuryActivity juryActivity;
    private SSLSocketFactory sslSocketFactory;

    public myJuryAsyncTask(JuryActivity juryActivity, SSLSocketFactory sslSocketFactory){
        this.juryActivity = juryActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    protected void onPreExecute(){

    }
    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject jsonObject = null;
        String urlString = params[0];
        String method = params[1];

        InputStream inputStream = Utils.sendRequestWS(urlString, method, this.sslSocketFactory);
        jsonObject = Utils.getJSONFromString(Utils.readStream(inputStream));

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject){
        this.juryActivity.treatmentResultMyJury(jsonObject);
    }
}
