package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.User;
import fr.eseo.example.androidproject.api.Utils;

public class logon extends AppCompatActivity{

    private Button login;
    private EditText username;
    private EditText password;
    private String name;
    private String pass;
    private Context ctx;
    private LoginRequest loginRequest;
    private SSLSocketFactory sslSocket;

    private Toast errorConnectionToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        login = findViewById(R.id.activity_logon_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login.setEnabled(false);
        ctx = this.getApplicationContext();
        username.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);

        sslSocket = Utils.configureSSLContext(ctx).getSocketFactory();

        errorConnectionToast = Toast.makeText(logon.this, "Erreur de connexion, vérifiez votre connexion à eduroam", Toast.LENGTH_SHORT);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest = new LoginRequest();
                User user = new User(username.getText().toString(), password.getText().toString());

                String urlRequestLogin = user.buildUrlForLogin();

                try {
                    InputStream resultStream = loginRequest.execute(urlRequestLogin).get();
                    if( resultStream != null){
                        JSONObject resultJSON = Utils.getJSONFromString(Utils.readStream(resultStream));
                        if(Utils.getJSONValue(resultJSON, "result").equals("KO")){
                            Toast.makeText(logon.this, "Identifiant ou mot de passe incorrect", Toast.LENGTH_LONG).show();
                        }else{
                            String token = Utils.getJSONValue(resultJSON, "token");
                            user.setToken(token);
                            String urlRequestStatus = user.buildUrlForStatus();
                            InputStream resultStatus = new RequestForAPI().execute(urlRequestStatus,"GET").get();
                            if(resultStatus != null){
                                JSONObject jsonRole = Utils.getJSONFromString(Utils.readStream(resultStatus));
                                JSONArray jsonValue = Utils.getJSONFromJSON(jsonRole,"info");
                                Log.d("role", Utils.getJSONValue(jsonValue, "descr"));
                                switch(Utils.getJSONValue(jsonValue, "descr")){
                                    case "Service Communications" :
                                        String username = Utils.getJSONValue(jsonValue,"username");
                                        Intent intent = new Intent(ctx, ProjectCommActivity.class);
                                        intent.putExtra("TOKEN", token);
                                        intent.putExtra("USERNAME", username);
                                        startActivity(intent);
                                        break;
                                }
                            }

                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Enabled the login button only if we write in username and password 
            String usernameInput = username.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();
            login.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    class LoginRequest extends AsyncTask<String, Void, InputStream> {

        private ProgressDialog progress;
        private static final String GET_METHOD = "GET";
        private LoginRequest loginRequest;

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(logon.this, "Chargement", "Juste un instant ...",true);
            super.onPreExecute();
        }

        @Override
        protected InputStream doInBackground(String... params) {
            String urlString = params[0];
            InputStream result;
            result = Utils.sendRequestWS(urlString, GET_METHOD, sslSocket);

            if(result == null){
                progress.dismiss();
                errorConnectionToast.show();
            }

            return result;
        }

        @Override
        protected void onPostExecute(InputStream result) {
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
        }

        public InputStream sendRequestWS(String url, String requestMethod, SSLSocketFactory sslSocket){
            InputStream responseStream = null;
            try {
                    // URL creation
                    URL url_connection = new URL(url);
                    // Configuration connection with SSLContext
                    HttpsURLConnection connection = (HttpsURLConnection)url_connection.openConnection();
                    connection.setSSLSocketFactory(sslSocket);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod(requestMethod);
                    connection.setConnectTimeout(1000);
                    connection.connect();
                    if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                        return connection.getInputStream();
                    }
            } catch (IOException ioe){
                return null;
            }

            return null;
        }
    }

    class RequestForAPI extends AsyncTask<String, Void, InputStream>{
        private ProgressDialog progress;
        private LoginRequest loginRequest;

        @Override
        protected void onPreExecute(){
            progress = ProgressDialog.show(logon.this, "Chargement", "Juste un instant ...",true);
            super.onPreExecute();
        }

        @Override
        protected InputStream doInBackground(String... params) {
            String urlString = params[0];
            String method = params[1];
            InputStream result;
            result = Utils.sendRequestWS(urlString, method, sslSocket);

            if(result == null){
                progress.dismiss();
                errorConnectionToast.show();
            }

            return result;
        }

        @Override
        protected void onPostExecute(InputStream result) {
            progress.dismiss();
            super.onPostExecute(result);
        }
    }

}