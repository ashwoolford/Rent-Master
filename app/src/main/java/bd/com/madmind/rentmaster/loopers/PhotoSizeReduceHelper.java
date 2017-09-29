package bd.com.madmind.rentmaster.loopers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import bd.com.madmind.rentmaster.AddingProperties;

/**
 * Created by ash on 8/10/2017.
 */

public class PhotoSizeReduceHelper extends AsyncTask<Void, Void , Bitmap> {
    private ImageView imageView;
    private Context context;
    private Bitmap bitmap;
    private ByteArrayOutputStream bytearrayoutputstream;
    private byte[] BYTE;
    private ProgressDialog progressDialog;
    private String file_name;
    Bitmap bitmap1;
    AddingProperties addingProperties;

    public PhotoSizeReduceHelper(Context context, AddingProperties addingProperties, ImageView imageView, Bitmap bitmap){
        this.context = context;
        this.imageView = imageView;
        this.bitmap = bitmap;
        this.addingProperties = addingProperties;

    }
    @Override
    protected Bitmap doInBackground(Void... params) {

        bytearrayoutputstream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,bytearrayoutputstream);
        BYTE = bytearrayoutputstream.toByteArray();
        Bitmap bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

        return bitmap2;
    }

    @Override
    protected void onPreExecute() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Reducing file size...");
        progressDialog.show();

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        progressDialog.hide();
        addingProperties.getBitmap(bitmap);

        //Log.d("ashraf",saveToInternalStorage(bitmap));

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
