package fr.eseo.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class logon extends AppCompatActivity {

    private Button login;
    private EditText username;
    private EditText password;
    private String name;
    private String pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        login = findViewById(R.id.activity_logon_btn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login.setEnabled(false);

        username.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = (username.getText().toString());
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


}