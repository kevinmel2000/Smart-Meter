package com.app.croptest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Display extends AppCompatActivity {
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/OCRActivity/";
    public static final String lang = "eng";
    public static final String digital = "lets";
    ImageView _myImage;
    ImageView _myImageOpenCV;
    TextView _myOCRView;
    String stringUri;
    Bitmap croppedBmp;
    private String _myOCRText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        _myImage = (ImageView) findViewById(R.id.cropped_image);
        _myImageOpenCV = (ImageView) findViewById(R.id.final_image);
        _myOCRView = (TextView) findViewById(R.id.result_ocr);

        String[] paths = new String[]{DATA_PATH, DATA_PATH + "tessdata/"};
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {

                    Log.v("msg", "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {

                    Log.v("msg", "Created directory " + path + " on sdcard");
                }
            }
        }

        if (!(new File(DATA_PATH + "tessdata/" + digital + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/" + digital + ".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + digital + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v("msg", "Copied " + digital + " traineddata");
            } catch (IOException e) {
                Log.e("msg", "Was unable to copy " + digital + " traineddata " + e.toString());
            }
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("KEY")) {
            stringUri = extras.getString("KEY");
        }
        Uri uri;
        uri = Uri.parse(stringUri);
        Bitmap imageOriginal = null;
        try {
            imageOriginal = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        croppedBmp = Bitmap.createBitmap(imageOriginal, 330, 150, imageOriginal.getWidth() - 670, imageOriginal.getHeight() / 4 - 180);
        Bitmap OpenCvbitmap = Bitmap.createBitmap(imageOriginal, 500, 200, imageOriginal.getWidth() - 950, imageOriginal.getHeight() / 4 - 220);
        _myImage.setImageBitmap(croppedBmp);

        Bildverarbeitung(OpenCvbitmap);
        _myImageOpenCV.setImageBitmap(OpenCvbitmap);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, digital);

        baseApi.setImage(croppedBmp);

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        Log.v("msg", "OCRED TEXT: " + recognizedText);

        if (digital.equalsIgnoreCase("lets")) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }

        recognizedText = recognizedText.trim();
        if (recognizedText.length() != 0) {
            _myOCRText = _myOCRText.length() == 0 ? recognizedText : _myOCRText + " " + recognizedText;
            _myOCRView.setText(_myOCRText);
        }
        _myOCRText = "";

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.crop_tool) {
            Intent intent = new Intent(Display.this, SmartMeterActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.camera_btn_display) {
            Intent intent = new Intent(Display.this, CameraActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Bitmap Bildverarbeitung(Bitmap image) {

        Mat tmp = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(image, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(tmp, tmp, new Size(3, 3), 0);
        Imgproc.threshold(tmp, tmp, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.dilate(tmp, tmp, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));
        Imgproc.erode(tmp, tmp, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8)));
        Utils.matToBitmap(tmp, image);
        return image;
    }
}
