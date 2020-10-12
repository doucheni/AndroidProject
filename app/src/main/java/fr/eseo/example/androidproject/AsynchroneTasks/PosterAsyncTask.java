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

public class PosterAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ProjectsDetailsCommActivity projectsDetailsCommActivity;
    private SSLSocketFactory sslSocketFactory;

    public PosterAsyncTask(ProjectsDetailsCommActivity commActivity, SSLSocketFactory sslSocketFactory){
        this.projectsDetailsCommActivity = commActivity;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    protected void onPreExecute(){

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected Bitmap doInBackground(String... params){
        Bitmap bitmap = null;

        InputStream inputStream = Utils.sendRequestWS(params[0], params[1], this.sslSocketFactory);
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

        bitmap = Utils.decodeBase64ToBitmap(inputStream);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        this.projectsDetailsCommActivity.downloadImage(bitmap);
    }




}
