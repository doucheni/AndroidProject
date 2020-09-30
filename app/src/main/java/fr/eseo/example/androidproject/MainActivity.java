package fr.eseo.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import fr.eseo.example.androidproject.api.UserUtils;

public class MainActivity extends AppCompatActivity {

    Button btn_request;
    RequestQueue queue;

    String user_token = "";
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_request = findViewById(R.id.btnRequete);
        queue = Volley.newRequestQueue(this);
        ctx = this.getApplicationContext();
        btn_request.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = "alberpat";
                String password = "w872o32HkYAO";
                UserUtils userUtils = new UserUtils(username, password, queue, MainActivity.this);
                new DownloadDataFromAPI().execute(userUtils.buildUrl());
            }
        });
    }

    class DownloadDataFromAPI extends AsyncTask<String, Void, String> {

        private static final String GET_METHOD = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;
        private static final String PATH_TO_KEYSTORE = "C:\\Users\\nicol\\Documents\\I3_2020-2021\\AndroidProject\\app\\keystore\\mykeystore.bks";
        private static final String PASS_KEYSTORE = "network";

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String result = "";
            try {
                URL url = new URL(urlString);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                InputStream certificateInput = ctx.getResources().openRawResource(R.raw.dis_inter_ca);

                KeyStore keysCA = KeyStore.getInstance("BKS");
                keysCA.load(new FileInputStream(PATH_TO_KEYSTORE),PASS_KEYSTORE.toCharArray());

                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                tmf.init(keysCA);

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                con.setReadTimeout(READ_TIMEOUT);
                con.setRequestMethod(GET_METHOD);
                con.setReadTimeout(READ_TIMEOUT);
                con.setSSLSocketFactory(sslSocketFactory);
                con.connect();

                result = this.readStream(con.getInputStream());

            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }catch(KeyStoreException kse){
                kse.printStackTrace();
            }catch(NoSuchAlgorithmException nsae){
                nsae.printStackTrace();
            }catch(CertificateException ce){
                ce.printStackTrace();
            }catch(KeyManagementException kme){
                kme.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result", result);
        }

        private String readStream(InputStream is) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            is.close();
            return sb.toString();
        }

    }
}