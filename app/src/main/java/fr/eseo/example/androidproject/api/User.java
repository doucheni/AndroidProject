package fr.eseo.example.androidproject.api;

public class User {

    private static final String API_URL = "https://172.24.5.16/pfe/webservice.php?";
    private static final String API_QUERY_LOGON = "LOGON";
    private static final String API_KEY_USER = "user";
    private static final String API_KEY_PASS = "pass";

    private String username;
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String buildUrl(){
        return API_URL + "q=" + API_QUERY_LOGON + "&" + API_KEY_USER + "=" + this.username + "&" + API_KEY_PASS + "=" + this.password;
    }

}
