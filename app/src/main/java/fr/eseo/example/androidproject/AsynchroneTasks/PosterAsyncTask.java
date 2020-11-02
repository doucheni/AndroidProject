package fr.eseo.example.androidproject.AsynchroneTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import javax.net.ssl.SSLSocketFactory;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.PosterFragment;

/**
 * AsyncTask for PosterFragment
 * Send a POSTR request to ESEO's API
 * Get a String IMG of the poster and return a Bitmap
 */
public class PosterAsyncTask extends AsyncTask<String, Void, Bitmap> {

    // Instance of PosterFragment
    private PosterFragment posterFragment;

    // SSLSocket configuration (certificate)
    private SSLSocketFactory sslSocketFactory;

    /**
     * Constructor of class PosterAsyncTask
     * @param posterFragment, instance of PosterFragment
     * @param sslSocketFactory, the SSLSocketFactory configuration
     */
    public PosterAsyncTask(PosterFragment posterFragment, SSLSocketFactory sslSocketFactory){
        //this.projectsDetailsCommActivity = commActivity;
        this.posterFragment = posterFragment;
        this.sslSocketFactory = sslSocketFactory;
    }

    // Before Execution
    @Override
    protected void onPreExecute(){

    }

    /**
     * Execution, get a Bitmap from request's result
     * @param params, String url and String method (GET, POST, ...)
     * @return bitmap, our poster image in Bitmap format
     */
    @Override
    protected Bitmap doInBackground(String... params){
        return Utils.decodeBase64ToBitmap(Utils.getStringFromRequestWS(params[0], params[1], this.sslSocketFactory));
    }

    /**
     * Run after execution
     * Call treatmentResult from PosterFragment
     * @param bitmap, the request's result in Bitmap format
     */
    @Override
    protected void onPostExecute(Bitmap bitmap){
        if(this.posterFragment != null){
            this.posterFragment.treatmentResult(bitmap);
        }
    }

}
