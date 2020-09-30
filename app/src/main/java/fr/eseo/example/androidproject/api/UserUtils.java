package fr.eseo.example.androidproject.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import android.content.Context;
import android.content.res.Resources;

public class UserUtils {

    private static final String API_URL = "https://172.24.5.16/pfe/webservice.php?";
    private static final String API_QUERY_LOGON = "LOGON";
    private static final String API_KEY_USER = "user";
    private static final String API_KEY_PASS = "password";

    private String username;
    private String password;
    private RequestQueue queue;
    private Context context;



    public UserUtils(String username, String password, RequestQueue queue, Context context){
        this.username = username;
        this.password = password;
        this.queue = queue;
        this.context = context;
    }

    public void logUser(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                this.buildUrl(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Response","oui oui ");
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void getUserToken(){
        try{
            URL url = new URL(this.buildUrl());
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            InputStream is = con.getInputStream();
            Log.d("response", this.readStream(is));
        }catch(MalformedURLException mue){
            mue.printStackTrace();
        }catch(ProtocolException pe){
            pe.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }


    }

    public String buildUrl(){
        return API_URL + "q=" + API_QUERY_LOGON + "&" + API_KEY_USER + "=" + this.username + "&" + API_KEY_PASS + "=" + this.password;
    }

    public void buildCertificate() throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, KeyManagementException {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
        int caIdentifier = this.context.getResources().getIdentifier("DIS_Inter_CA", "raw", context.getPackageName());
        InputStream caInput = context.getResources().openRawResource(caIdentifier);
                //new BufferedInputStream(new FileInputStream("res/DIS_Inter_CA.crt"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(this.buildUrl());
        HttpsURLConnection urlConnection =
                (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream in = urlConnection.getInputStream();
        Log.d("response", readStream(in));
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }


}
