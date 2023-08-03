package com.example.plantdiseasedetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.plantdiseasedetection.ml.PlantDiseaseModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LinearLayout camera;
    TextView search;
    SwitchCompat marathi;
    MaterialToolbar toolbar;
    ExtendedFloatingActionButton fabCallSupport;
    int imagesize = 224;
    private static final int REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabCallSupport = findViewById(R.id.fab_main);
        fabCallSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        setupToolbar();
        camera = findViewById(R.id.button);
//        gallery = findViewById(R.id.button2);
        marathi = findViewById(R.id.marathi);
        search = findViewById(R.id.searchmain);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ActivitySearch.class));
            }
        });
        marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale;
                if(!marathi.isChecked()){
                    locale = new Locale("en");

                }else{
                    locale = new Locale("mr");
                }
                Locale.setDefault(locale);

                Configuration configuration = new Configuration();
                configuration.locale= locale;
                getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
                recreate();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),3);
                }else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                startActivityForResult(new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
//
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==3){
                Bitmap image = (Bitmap) data.getExtras().get("data");

                int dimension = Math.min(image.getWidth(),image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);

                image = Bitmap.createScaledBitmap(image,imagesize,imagesize,false);
                classifyImage(image);
            }else{
                Uri uri = data.getData();
                Bitmap image = null;
                try{
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                }catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                image = Bitmap.createScaledBitmap(image,imagesize,imagesize,false);
                classifyImage(image);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap image) {

        try{

            PlantDiseaseModel model = PlantDiseaseModel.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imagesize*imagesize*3);
            byteBuffer.order(ByteOrder.nativeOrder());


            int[] intValues = new int[imagesize*imagesize];
            image.getPixels(intValues,0, image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixels = 0;
            for (int i = 0; i < imagesize; i++) {
                for (int j = 0; j < imagesize; j++) {
                    int val = intValues[pixels++];
//                    byteBuffer.putFloat(((val>>16) & 0xFF) * (1.f/1));
//                    byteBuffer.putFloat(((val>>8) & 0xFF) * (1.f/1));
//                    byteBuffer.putFloat((val & 0xFF) * (1.f/1));
                    byteBuffer.putFloat(((val>>16) & 0xFF) /255.0f);
                    byteBuffer.putFloat(((val>>8) & 0xFF) /255.0f);
                    byteBuffer.putFloat((val & 0xFF) /255.0f);
                }
            }


            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            PlantDiseaseModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();


            float[] confidence = outputFeature0.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidence.length; i++) {
                if(confidence[i]>maxConfidence){
                    maxConfidence = confidence[i];
                    maxPos = i;
                }
            }



            // Releases model resources if no longer used.
            model.close();

            Intent intent =  new Intent(MainActivity.this,ActivityResult.class);
            intent.putExtra("image",image);
            intent.putExtra("res",maxPos);
            intent.putExtra("perc",maxConfidence);
            startActivity(intent);


        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void makePhoneCall() {
        String phoneNumber = "Replace with customer care number"; // Replace with your customer support phone number

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setupToolbar(){
        if (getSupportActionBar()==null){
            toolbar = findViewById(R.id.toolbar_main);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Plant Disease Detection", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}