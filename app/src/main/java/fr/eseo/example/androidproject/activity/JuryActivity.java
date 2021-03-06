package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.myJuryAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.JuryModel;
import fr.eseo.example.androidproject.api.UserModel;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.JuryActivityFragment;


public class JuryActivity extends AppCompatActivity {
    public Toast errorRequestToast;
    private Toast errorResultToast;
    private ProgressDialog progressDialog;
    public static final String JURY_ID = "jury_id";
    public static final String USERNAME ="username";
    public static final String TOKEN = "token";
    public  String username;
    public  String token;
    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentJury = getIntent();
        token = intentJury.getStringExtra("TOKEN");
        username = intentJury.getStringExtra("USERNAME");
        final Context ctx = getApplicationContext();
        SSLSocketFactory sslSocket = Utils.configureSSLContext(ctx).getSocketFactory();
        errorRequestToast = Toast.makeText(JuryActivity.this, "Error during the request", Toast.LENGTH_LONG);
        errorResultToast = Toast.makeText(JuryActivity.this, "Your jury is not available", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_jury);
        myJuryAsyncTask myJuryAsyncTask = new myJuryAsyncTask(this, sslSocket);
        this.progressDialog = ProgressDialog.show(JuryActivity.this, "Loading", "Please wait ...", true);
        myJuryAsyncTask.execute("https://172.24.5.16/pfe/webservice.php?q=MYJUR&user=" + username + "&token=" + token, "GET");

    }

    public void treatmentResultMyJury(JSONObject jsonObject) {
        List<JuryModel> juries = new ArrayList<>();
        ArrayList<UserModel> listMembers = new ArrayList<>();

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
                    for(int m=0;m<jsonMembers.length();m++){
                        JSONObject jsonMember = jsonMembers.getJSONObject(m);
                        int member_id = jsonMember.getInt("idUser");
                        String member_forename = jsonMember.getString("forename");
                        String member_surname = jsonMember.getString("surname");
                        UserModel member = new UserModel(member_id,member_forename,member_surname);
                        listMembers.add(member);
                    }

                    JuryModel jury = new JuryModel(jury_id, jury_date,listMembers);
                    juries.add(jury);


                }

            }
            for(int i = 0; i < juries.size(); i++){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = JuryActivityFragment.newInstance(juries.get(i),this.username,this.token);
                ft.add(R.id.jury_container,fragment, "Jury-"+i);
                ft.commit();


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.progressDialog.dismiss();

    }

    public void seeProjects(View view) {
        Button button = view.findViewById(R.id.button_projects);
        intent = new Intent(this.getApplicationContext(), AllProjectsActivity.class);
        intent.putExtra("TOKEN", token);
        intent.putExtra("USERNAME", username);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(intent);
            }
        });

    }





}