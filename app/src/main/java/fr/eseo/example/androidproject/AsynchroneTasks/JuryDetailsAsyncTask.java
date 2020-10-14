package fr.eseo.example.androidproject.AsynchroneTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.JuryDetailsActivity;
import fr.eseo.example.androidproject.api.Utils;

public class JuryDetailsAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private JuryDetailsActivity juryDetailsActivity;
    private SSLSocketFactory sslSocketFactory;

    public JuryDetailsAsyncTask(JuryDetailsActivity juryDetailsActivity, SSLSocketFactory sslSocketFactory){
        this.juryDetailsActivity = juryDetailsActivity;
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
        this.juryDetailsActivity.treatmentResultMyDetailsJury(jsonObject);
    }

}

