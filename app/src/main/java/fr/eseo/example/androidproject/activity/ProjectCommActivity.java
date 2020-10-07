package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.AsynchroneTasks.CommAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.Utils;

public class ProjectCommActivity extends AppCompatActivity {

    public static final String PROJECT_ID = "Project_id";
    public SSLSocketFactory sslSocketFactory;
    public Toast errorRequestToast;
    private CommAsyncTask commAsyncTask;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String token = intent.getStringExtra("TOKEN");
        final String username = intent.getStringExtra("USERNAME");
        final Context ctx = getApplicationContext();
        sslSocketFactory = Utils.configureSSLContext(ctx).getSocketFactory();
        errorRequestToast = Toast.makeText(ProjectCommActivity.this, "Error during the request", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_com_projects);
        commAsyncTask = new CommAsyncTask(this, sslSocketFactory);
        this.progressDialog = ProgressDialog.show(ProjectCommActivity.this,"Loading", "Please wait ...", true);
        commAsyncTask.execute("https://172.24.5.16/pfe/webservice.php?q=LIPRJ&user="+username+"&token="+token, "GET");
    }

    public void treatmentResult(JSONObject jsonObject){
        this.progressDialog.dismiss();
        Log.d("result", jsonObject.toString());
    }
}
