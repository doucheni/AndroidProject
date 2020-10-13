package fr.eseo.example.androidproject.AsynchroneTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.net.ssl.SSLSocketFactory;

import fr.eseo.example.androidproject.activity.ProjectsDetailsCommActivity;
import fr.eseo.example.androidproject.api.Utils;
import fr.eseo.example.androidproject.fragments.PosterFragment;

public class PosterAsyncTask extends AsyncTask<String, Void, Bitmap> {

    //private ProjectsDetailsCommActivity projectsDetailsCommActivity;
    private PosterFragment posterFragment;
    private SSLSocketFactory sslSocketFactory;

    public PosterAsyncTask(PosterFragment posterFragment, SSLSocketFactory sslSocketFactory){
        //this.projectsDetailsCommActivity = commActivity;
        this.posterFragment = posterFragment;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Bitmap doInBackground(String... params){
        Bitmap bitmap = null;

        System.out.println(params[0]);

        String result = Utils.getStringFromRequestWS(params[0], params[1], this.sslSocketFactory);

        /*
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = Utils.calculateInSampleSize(options,50, 75);
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        */
        /*
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            ImageDecoder.Source source = ImageDecoder.createSource(byteBuffer);
            bitmap = ImageDecoder.decodeBitmap(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        bitmap = Utils.decodeBase64ToBitmap(result);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        //this.projectsDetailsCommActivity.downloadImage(bitmap);
        this.posterFragment.treatmentResult(bitmap);
    }




}
