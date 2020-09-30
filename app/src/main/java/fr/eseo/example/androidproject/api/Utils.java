package fr.eseo.example.androidproject.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import fr.eseo.example.androidproject.R;

/**
 * Utilitary Class for Web Service requests
 */
public class Utils {

    // Timing of reading
    private static final int READ_TIMEOUT = 15000;
    // Timing of connection
    private static final int CONNECTION_TIMEOUT = 15000;

    /**
     * Configuration of SSLContext
     * @param context, activity's context
     * @return SSLContext configured
     */
    public static SSLContext configureSSLContext(Context context){
        // Get Certificate given by ESEO (DIS_Inter_CA.crt)
        InputStream certificateInput = context.getResources().openRawResource(R.raw.dis_inter_ca);
        // Initialization Certificate and SSLContext
        Certificate cert;
        SSLContext sslContext = null;
        try {
            // Instance of CertificateFactory
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            cert = cf.generateCertificate(certificateInput);

            // Configuration KeyStore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", cert);

            // Configuration TrustManagerFactory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Final Configuration of SSLContext
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        }catch (CertificateException ce){
            ce.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }catch(KeyStoreException kse){
            kse.printStackTrace();
        }catch(KeyManagementException kme){
            kme.printStackTrace();
        }
        return sslContext;
    }

    /**
     * Send a request to the web service
     * @param url, url for the connection
     * @param requestMethod, methode's type (GET, POST, ...)
     * @param ctx, activity's context
     * @return responseStream, the output of the request
     */
    public static InputStream sendRequestWS(String url, String requestMethod, Context ctx){
       InputStream responseStream = null;
        try {
            // URL creation
            URL url_connection = new URL(url);
            // Configuration connection with SSLContext
            HttpsURLConnection connection = (HttpsURLConnection)url_connection.openConnection();
            connection.setSSLSocketFactory(Utils.configureSSLContext(ctx).getSocketFactory());
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod(requestMethod);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            // Send request
            responseStream = connection.getInputStream();

            Log.d("reponse",readStream(responseStream));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStream;
    }

    /**
     * Get a specific value from a JSONObject with a specific key
     * @param jsonObject, the JSONObject
     * @param name, the name of the data we search
     * @return value, the value with the key given
     */
    public static String getJSONValue(JSONObject jsonObject, String name){
        String value = "";
        try{
            // Get value with 'name' key
            value = jsonObject.get(name).toString();
        }catch (JSONException json){
            json.printStackTrace();
        }
        return value;
    }

    /**
     * Get all the data from a JSONObject
     * @param jsonObject, our JSONObject
     * @return mapValues, the HashMap of the values <String key, String value>
     */
    public static HashMap<String, String> getJSONValues(JSONObject jsonObject){
        HashMap<String, String> mapValues = new HashMap<>();
        Iterator<String> iter = jsonObject.keys();
        // Loop for each differents keys in the JSONObject
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                // Affectation in HashMap
                mapValues.put(key, jsonObject.get(key).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mapValues;
    }

    /**
     * Get a String object from a InputStream object
     * @param is, our InputStream
     * @return sb, the equivalent String
     */
    public static String readStream(InputStream is){
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        try{
            // Loop for each line in the InputStream
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                // Affectation in StringBuilder
                sb.append(line);
            }
            is.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return sb.toString();
    }
}
