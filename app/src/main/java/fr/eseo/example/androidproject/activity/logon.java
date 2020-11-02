package fr.eseo.example.androidproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.net.ssl.SSLSocketFactory;
import fr.eseo.example.androidproject.AsynchroneTasks.LogonAsyncTask;
import fr.eseo.example.androidproject.AsynchroneTasks.UserStatusAsyncTask;
import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.PseudoJuryModel;
import fr.eseo.example.androidproject.api.Utils;

/**
 * Class for home activity
 * In this activity the user can :
 *  Log in as a professor or a communication member
 *  have access to visitor activity if a communcation member has already created a pseudojury
 */
public class logon extends AppCompatActivity{

    // Views from XML layout
    private Button login;
    private EditText username;
    private EditText password;
    private TextView linkVisitor;

    /* Variables */
    private String token;
    private String usernameString;
    private String passwordString;
    private ProgressDialog progressDialog;
    private Context ctx;
    private SSLSocketFactory sslSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        // Initialization of variables
        this.initVariables();

        // Add onClickListener on login button
        this.initLoginBTNAction();

        // Add onClickListener on visitor link
        this.initLinkVisitorAction();

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

    /**
     * Initialization of variables
     */
    private void initVariables(){
        login = findViewById(R.id.activity_logon_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login.setEnabled(false);
        ctx = this.getApplicationContext();
        username.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);
        linkVisitor = findViewById(R.id.link_visitor);
        sslSocket = Utils.configureSSLContext(ctx).getSocketFactory();
    }

    /**
     * Add a OnClickListener on login button
     * Send a LOGON request to ESEO's API
     */
    private void initLoginBTNAction(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runLogonAsyncTask();
            }
        });
    }

    /**
     * Add a OnClickListener on visitor's link
     * If there is a pseudojury, redirect the user to visitor's activity
     * If there is no pseudojury, display a toast
     */
    private void initLinkVisitorAction(){
        linkVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                PseudoJuryModel pseudoJuryModel = (PseudoJuryModel)intent.getSerializableExtra("pseudojury");
                String usernameComm = intent.getStringExtra("username");
                String tokenComm = intent.getStringExtra("token");
                if(pseudoJuryModel == null){
                    Toast.makeText(logon.this, "Demandez l'intervention d'un membre du service de communication", Toast.LENGTH_LONG).show();
                }else{
                    Intent intentVisitor = new Intent(ctx, VisitorActivity.class);
                    intentVisitor.putExtra("pseudojury", pseudoJuryModel);
                    intentVisitor.putExtra("username", usernameComm);
                    intentVisitor.putExtra("token", tokenComm);
                    startActivity(intentVisitor);
                }
            }
        });
    }

    /**
     * Send a LOGON request to ESEO's API with the username and password
     */
    private void runLogonAsyncTask(){
        this.usernameString = username.getText().toString();
        this.passwordString = password.getText().toString();
        LogonAsyncTask logonAsyncTask = new LogonAsyncTask(this, this.sslSocket);
        this.progressDialog = ProgressDialog.show(logon.this, "Connexion", "Veuillez patienter, connexion en cours ...");
        logonAsyncTask.execute(Utils.buildUrlForLOGON(this.usernameString, this.passwordString), "GET");
    }

    /**
     * Treat the result from LogonAsyncTask
     * If result is OK, get the token and send a MYINF request
     * If result is KO, display a toast
     * @param jsonObject, the request's result
     */
    public void treatmentResultLOGON(JSONObject jsonObject){
        if(Utils.getJSONValue(jsonObject, "result").equals("KO")){
            Toast.makeText(logon.this, "Identifiant ou mot de passe incorrect", Toast.LENGTH_LONG).show();
            this.progressDialog.dismiss();
        }else{
            this.token = Utils.getJSONValue(jsonObject, "token");
            UserStatusAsyncTask userStatusAsyncTask = new UserStatusAsyncTask(this, this.sslSocket);
            userStatusAsyncTask.execute(Utils.buildUrlForMYINF(this.usernameString), "GET");
        }
    }

    /**
     * Treat the result from UserStatusAsyncTask
     * Get user's status (Communication, Professeur)
     * Redirect the user
     * @param jsonObject, the request's result
     */
    public void treatmenResultMYINF(JSONObject jsonObject){
        JSONArray jsonValue = Utils.getJSONFromJSON(jsonObject, "info");
        this.progressDialog.dismiss();
        switch(Utils.getJSONValue(jsonValue, "descr")){
            case "Service Communications" :
                String username = Utils.getJSONValue(jsonValue,"username");
                Intent intent = new Intent(ctx, ProjectCommActivity.class);
                intent.putExtra("TOKEN", token);
                intent.putExtra("USERNAME", username);

                startActivity(intent);
                break;

            case "Professeur" :
                String usernameJury = Utils.getJSONValue(jsonValue,"username");
                Intent intentJury = new Intent(ctx, JuryActivity.class);
                intentJury.putExtra("TOKEN",token);
                intentJury.putExtra("USERNAME",usernameJury);
                startActivity(intentJury);
                break;
        }
    }




}