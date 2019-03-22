package com.example.cameraapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Integer cameraCount;
    ImageView imageView1;
    Button cameraButton, resetButton, saveButton, retrieveButton;
    EditText editText1;
    private static final int CAMERA_REQUEST = 2222;
    Bitmap bmap;
    DatabaseInterface database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cameraCount = 0;

        this.database = new DatabaseInterface(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        retrieveButton = (Button) findViewById(R.id.retrieveButton);
        editText1 = (EditText) findViewById(R.id.editText1);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bmap != null) {
                    database.loadImageToDatabase(database.fromBmapTo64(bmap));
                    cameraCount = cameraCount + 1;
                    Toast.makeText(getApplicationContext(), "Image Saved: "+Integer.toString(cameraCount), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Take a photo first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.setImageResource(R.drawable.after_the_storm);
            }
        });

        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photoIndex = editText1.getText().toString();
                if (!photoIndex.isEmpty()) {
                    if (Integer.parseInt(photoIndex) <= cameraCount) {
                        String encodedImg = database.retrievePhoto(photoIndex);
                        Bitmap bmp = database.from64toBmap(encodedImg);
                        imageView1.setImageBitmap(bmp);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please retrieve an EXISTING image.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the index of an image.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK) {
                bmap = (Bitmap) intent.getExtras().get("data");
                this.imageView1.setImageBitmap(bmap);
        }
    }
}
