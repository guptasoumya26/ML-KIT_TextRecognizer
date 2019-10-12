package com.example.mlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int TEXT_RECO_REQ_CODE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void textRecognition(View view){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,TEXT_RECO_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==TEXT_RECO_REQ_CODE){
            if (resultCode==RESULT_OK){
                Bitmap photo=(Bitmap)data.getExtras().get("data");
                textRecognition(photo);
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this,"Operation cancelled by user",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Failed to capture the image",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void textRecognition(Bitmap photo) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(this::processTextRecognitionResult).addOnFailureListener(Throwable::printStackTrace);

    }

    private void processTextRecognitionResult(FirebaseVisionText firebaseVisionText) {

        if (firebaseVisionText.getTextBlocks().size() == 0) {
            Toast.makeText(MainActivity.this,"Unable to find text",Toast.LENGTH_SHORT).show();
            return;
        }
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {

            Toast.makeText(MainActivity.this,"Recognized text is:"+block.getText(),Toast.LENGTH_SHORT).show();


            //In case you want to extract each line
			/*
			for (FirebaseVisionText.Line line: block.getLines()) {
				for (FirebaseVisionText.Element element: line.getElements()) {
					mTextView.append(element.getText() + " ");
				}
			}
			*/
        }
    }
}
