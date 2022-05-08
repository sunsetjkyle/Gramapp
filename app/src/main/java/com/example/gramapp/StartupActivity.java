package com.example.gramapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StartupActivity extends AppCompatActivity {
    private EditText fullnames, phone, address, email;
    private MaterialButton save;
    private CircleImageView profileimg;
    private FirebaseAuth sAuth;
    private FirebaseFirestore sFirestore;
    private Uri resulturi;
    String user;
    private static final int gallery_pick= 1;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);



        sAuth = FirebaseAuth.getInstance();
        sFirestore = FirebaseFirestore.getInstance();
        user = sAuth.getCurrentUser().getUid();
//        StorageReference storageReference = storage.getReference();
//        StorageReference imageRef = storageReference.child("Profile Pics");

        fullnames = findViewById(R.id.fullnames);
        phone = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        email = findViewById(R.id.username);
        profileimg = findViewById(R.id.profile_image);

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                    if (ContextCompat.checkSelfPermission(StartupActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(StartupActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 33);
//                    }
//                    else {
                        Intent intent_gallery = new Intent();
                     intent_gallery.setType("image/*");
                        intent_gallery.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent_gallery, "select picture"), gallery_pick);
                    }

//                    }
//            }
        });

        documentReference = sFirestore.collection("Users").document(user);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot.exists()) {
                    Glide. with(getApplicationContext()).load(snapshot.get("profile_img_url")).placeholder(R.drawable.ic_profile).into(profileimg);
                }
            }
        });


        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();

            }
        });
    }

    private void saveUserInfo() {

        String fullname = fullnames.getText().toString();
        String phone_number = phone.getText().toString();
        String city_address =  address.getText().toString();
        String username = email.getText().toString();

        if (TextUtils.isEmpty(fullname)||TextUtils.isEmpty(phone_number)||TextUtils.isEmpty(city_address)||TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your details to proceed", Toast.LENGTH_SHORT).show();
        } else {

            Map<String, Object> hashmap = new HashMap();
            hashmap.put("Full_names", fullname);
            hashmap.put("Phone_numbers", phone_number);
            hashmap.put("Postal Address", city_address);
            hashmap.put("User_email", username);
            hashmap.put("search", fullname.toLowerCase());

            documentReference.set(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(StartupActivity.this, "Details added successfully", Toast.LENGTH_SHORT).show();
           sendToMainActivity();


                    }

                }
            });









        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.show();


        if (resulturi != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images").child(user);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resulturi);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

            byte  [] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;

                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference()!=null) {

                            Task <Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String image_url = uri.toString();

                                    Map new_image = new HashMap();
                                    new_image.put("profile_img_url", image_url);
                                    documentReference.update(new_image);
                                    finish();
                                    return;
                                }
                            });


                        }

                    }
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded" + " " + progress+ "%" );
                }
            });
        }


            }

    private void sendToMainActivity() {
        startActivity(new Intent(StartupActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resulturi = imageUri;
            profileimg.setImageURI(resulturi);


        }

    }

}


