package fr.eseo.example.androidproject.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.io.IOUtils;

import fr.eseo.example.androidproject.R;

/**
 * Utilitary Class for Web Service requests
 */
public class Utils {

    // Timing of reading
    private static final int READ_TIMEOUT = 5000;
    // Timing of connection
    private static final int CONNECTION_TIMEOUT = 1000;

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
        }catch (CertificateException | IOException  | NoSuchAlgorithmException | KeyStoreException | KeyManagementException ce){
            ce.printStackTrace();
        }
        return sslContext;
    }

    /**
     * Send a request to the web service
     * @param url, url for the connection
     * @param requestMethod, methode's type (GET, POST, ...)
     * @param sslSocket, SSLSocketFactory configured with certificate
     * @return responseStream, the output of the request
     */
    public static InputStream sendRequestWS(String url, String requestMethod, SSLSocketFactory sslSocket){
       InputStream responseStream = null;
        try {
            // URL creation
            URL url_connection = new URL(url);
            // Configuration connection with SSLContext
            HttpsURLConnection connection = (HttpsURLConnection)url_connection.openConnection();
            connection.setSSLSocketFactory(sslSocket);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestMethod(requestMethod);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();

            if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                return connection.getInputStream();
            }

        }catch(IOException ioe){
            return null;
        }
        return null;
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

    public static String getJSONValue(JSONArray jsonArray, String name){
        String value = null;
        for(int i = 0; i < jsonArray.length(); i++){
            try{
                value = jsonArray.getJSONObject(i).getString(name);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return  value;
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
        String theString = "";
        try{
            theString = IOUtils.toString(is, StandardCharsets.UTF_8);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return theString;
    }

    /**
     * Get a JSON object from a String object
     * @param stringObject our String
     * @return a JSONobject
     */
    public static JSONObject getJSONFromString(String stringObject){
        JSONObject jsonObject = null;
        Log.d("result", stringObject);
        try{
            jsonObject = new JSONObject(stringObject);
        }catch (JSONException jsonE){
            jsonE.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray getJSONFromJSON(JSONObject jsonObject, String nameValue){
        JSONArray jsonResult = null;
        try{
            jsonResult = jsonObject.getJSONArray(nameValue);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return jsonResult;
    }

}
