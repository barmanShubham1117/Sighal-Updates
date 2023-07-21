package com.skilligence.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skilligence.myapplication.model.BlogModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // views for button
    private Button btnUpload;

//    view for edit text
    private EditText bTitle, bDescription;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

//    private String title, description, imgURL;
    private BlogModel blog;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imgPreview);
        btnUpload = findViewById(R.id.saveBtn);
        bTitle = findViewById(R.id.bTitle);
        bDescription = findViewById(R.id.bDescription);

        blog = new BlogModel();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        blog.setTitle(bTitle.getText().toString());
        blog.setDescription(bDescription.getText().toString());

        if (filePath == null) {
            Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
        } else if (blog.getTitle() == null) {
            Toast.makeText(this, "Please enter a title.", Toast.LENGTH_SHORT).show();
        } else if (blog.getDescription() == null) {
            Toast.makeText(this, "Please enter a description.", Toast.LENGTH_SHORT).show();
        } else {
            progress = new ProgressDialog(this);
            progress.setTitle("Uploading file..");
            progress.setMessage("Your file is being uploaded, please wait.");
            progress.show();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date date = new Date();
            String filename = dateFormat.format(date);
            storageReference = FirebaseStorage.getInstance().getReference("uploads/" + filename);

            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progress.dismiss();
                            Toast.makeText(MainActivity.this, "Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                            imageView.setImageURI(null);

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.e("TAG", "onSuccess: " + uri.toString());
                                    blog.setImgURL(uri.toString());

                                    //Code to upload blog data to realtime database
                                    database = FirebaseDatabase.getInstance();
                                    reference = database.getReference("blogs").push();
                                    blog.setBlogId(reference.getKey());
                                    reference.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivity.this, "Blog uploaded successfully.", Toast.LENGTH_SHORT).show();
                                            bTitle.setText("");
                                            bDescription.setText("");
                                        }
                                    });

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.dismiss();
                            Toast.makeText(MainActivity.this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            filePath = data.getData();
            imageView.setImageURI(filePath);
        }
    }
}