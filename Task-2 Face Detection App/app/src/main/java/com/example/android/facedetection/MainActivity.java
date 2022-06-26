package com.example.android.facedetection;

import static com.example.android.facedetection.R.color.purple_700;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton;

    private final static int REQUEST_IMAGE_CAPTURE = 124;
    private InputImage image;
    private FaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.purple_700)));
        
        FirebaseApp.initializeApp(this);
        cameraButton = findViewById(R.id.camera_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){


            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            detectFace(bitmap);
        }
    }

    private void detectFace(Bitmap bitmap) {

        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build();

        try {

            image = InputImage.fromBitmap(bitmap,0);

            //Get an instance of FaceDetector
            detector = com.google.mlkit.vision.face.FaceDetection.getClient(highAccuracyOpts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        detector.process(image).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {
                String resultText = "";

                int i = 1;


                for (Face face:faces) {


                    i++;
                }
                resultText = resultText.concat("\n"+i+" Faces Detected");

                if (faces.size() == 0)
                    Toast.makeText(MainActivity.this, "NO FACES", Toast.LENGTH_SHORT).show();
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString(FaceDetection.RESULT_TEXT,resultText);
                    DialogFragment resultDialog = new ResultDialog();
                    resultDialog.setArguments(bundle);
                    resultDialog.setCancelable(false);
                    resultDialog.show(getSupportFragmentManager(), FaceDetection.RESULT_DIALOG);
                }

            }
        });

    }
}
