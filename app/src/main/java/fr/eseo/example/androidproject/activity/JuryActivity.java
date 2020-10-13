package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.myJuryAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.JuryActivityFragment;
import fr.eseo.example.androidproject.room.entities.Jury;

public class JuryActivity extends AppCompatActivity {
    private SSLSocketFactory sslSocket;
    public Toast errorRequestToast;
    private Toast errorResultToast;
    private ProgressDialog progressDialog;
    private myJuryAsyncTask myJuryAsyncTask;
    public static final String JURY_ID = "juryId";
    private String token;
    private String username;
    private JuryActivityFragment juryActivityFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentJury = getIntent();
        final String token = intentJury.getStringExtra("TOKEN");
        final String username = intentJury.getStringExtra("USERNAME");
        final Context ctx = getApplicationContext();
        sslSocket = Utils.configureSSLContext(ctx).getSocketFactory();
        errorRequestToast = Toast.makeText(JuryActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(JuryActivity.this, "Your jury is not available", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_jury);
        myJuryAsyncTask = new myJuryAsyncTask(this, sslSocket);
        this.progressDialog = ProgressDialog.show(JuryActivity.this, "Loading", "Please wait ...", true);
        myJuryAsyncTask.execute("https://172.24.5.16/pfe/webservice.php?q=MYJUR&user=" + username + "&token=" + token, "GET");

    }

    public void treatmentResultMyJury(JSONObject jsonObject) {
        List<Jury> juries = new ArrayList<>();

        try {
            if (jsonObject.get("result").equals("KO")) {
                errorResultToast.show();
            } else {
                JSONArray jsonJuries = jsonObject.getJSONArray("juries");
                for(int i = 0; i < jsonJuries.length(); i++) {
                    JSONObject jsonJury = jsonJuries.getJSONObject(i);
                    int jury_id = jsonJury.getInt("idJury");
                    String jury_date = jsonJury.getString("date");
                    JSONObject jsonInfo = jsonJury.getJSONObject("info");
                    JSONArray jsonMembers = jsonInfo.getJSONArray("members");
                    JSONArray jsonProjects = jsonInfo.getJSONArray("projects");

                    Jury jury = new Jury(jury_id, jury_date);
                    juries.add(jury);
                    Log.d(String.valueOf(jury), "treatmentResultMyJury: jury ");
                }

            }
            for(int i = 0; i < juries.size(); i++){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = JuryActivityFragment.newInstance(juries.get(i));
                ft.add(R.id.jury_container,fragment, "Jury-"+i);
                ft.commit();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.progressDialog.dismiss();

    }



}