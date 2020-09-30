package fr.eseo.example.androidproject.activity;

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

import androidx.appcompat.app.AppCompatActivity;

import fr.eseo.example.androidproject.R;
import fr.eseo.example.androidproject.api.User;
import fr.eseo.example.androidproject.api.Utils;

public class logon extends AppCompatActivity {

    private Button login;
    private EditText username;
    private EditText password;
    private String name;
    private String pass;
    private Context ctx;



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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(username.getText().toString(), password.getText().toString());
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.execute(user.buildUrl());
                System.out.println(name);

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

    class LoginRequest extends AsyncTask<String, Void, String> {

        private static final String GET_METHOD = "GET";

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String result = "";
            result = Utils.readStream(Utils.sendRequestWS(urlString, GET_METHOD, ctx));
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }


}