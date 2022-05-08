package com.example.gramapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {
     ImageButton close;
    ImageView post_placeholder;
    EditText description;
    TextView post;
    Toolbar posttoolbar;

    private FirebaseAuth pAuth;

    String profile_img_url, downloadUrl, PostCurrentDate, PostCurrentTime, user,  Full_names;
    private FirebaseFirestore pStorage;
    StorageTask uploadTask;
    private Uri final_uri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    private static  final int gallery_pick = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        posttoolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(posttoolbar);
        getSupportActionBar().setTitle("Post");

        posttoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();
            }
        });

        close = findViewById(R.id.close);
        post_placeholder = findViewById(R.id.post_placeholder);
        description = findViewById(R.id.decribe);
        post = findViewById(R.id.post);

        progressDialog = new ProgressDialog(this);

        pAuth = FirebaseAuth.getInstance();
        user =pAuth.getCurrentUser().getUid();
      pStorage = FirebaseFirestore.getInstance();

      storageReference = FirebaseStorage.getInstance().getReference("Post_images");

      close.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(PostActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
              finish();
          }
      });

      post .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              uploadImage();
          }
      });


      post_placeholder.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
//              pick the image
              Intent post_intent = new Intent();
              post_intent.setType("image/*");
              post_intent.setAction(Intent.ACTION_PICK);
              startActivityForResult(post_intent, gallery_pick);

          }
      });
//setting the location to be uploaded to
        DocumentReference documentReference = pStorage.collection("posts").document();
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (snapshot.exists()) {
                    Glide.with(getApplicationContext()).load(snapshot.get("img_url")).placeholder(R.drawable.ic_photo_black_48dp).into(post_placeholder);

                }

            }
        });
        getProfileData();

    }

    private void getProfileData() {
        DocumentReference documentReference = pStorage.collection("Users").document(user);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    profile_img_url = value.getString("profile_img_url");
                    Full_names = value.getString("Full_names");

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallery_pick &&  resultCode == RESULT_OK && data !=null &&data.getData() !=null) {
            final_uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), final_uri);
                post_placeholder.setImageBitmap(bitmap);


            } catch (IOException e){
                e.printStackTrace();

            }

        }

        if (final_uri != null) {

            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference reference = storageReference.child("Post Images/" + UUID.randomUUID().toString());
           reference.putFile(final_uri)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           progressDialog.dismiss();
                           Toast.makeText(PostActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                           reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   downloadUrl = uri.toString();

                               }
                           });


                       }
                   }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   progressDialog.dismiss();
                   Toast.makeText(PostActivity.this, "Failed to upload Post", Toast.LENGTH_SHORT).show();
               }
           })
           .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                   double Progress  = (100.0*snapshot.getBytesTransferred()/snapshot.getBytesTransferred());
                   progressDialog.setMessage("Uploaded"+" "+ (int) (Progress) + "%");

               }
           });

        }
    }

    private void uploadImage() {
        String caption = description.getText().toString();
        String id = pStorage.collection("posts").document().getId();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat Current_Date = new SimpleDateFormat("dd-MMMM-yyyy");
        PostCurrentDate = Current_Date.format(calendar.getTime());

        Calendar time = Calendar.getInstance();
        SimpleDateFormat Current_time = new SimpleDateFormat("HH:mm");
        PostCurrentTime = Current_time.format(time.getTime());
        
        if (TextUtils.isEmpty(caption)){
            description.setError("Write your description!");
        }
        if (final_uri==null){
            Toast.makeText(this, "Select an Image", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setTitle("Posting....");
            progressDialog.setMessage("Wait as we upload your post");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            HashMap postsmap = new HashMap();
            postsmap.put("caption", caption);
            postsmap.put("post_current_date", PostCurrentDate);
            postsmap.put("post_current_time", PostCurrentTime);
            postsmap.put("publisher", user);
            postsmap.put("Full_names", Full_names);
            postsmap.put("post_img_url", downloadUrl);
            postsmap.put("post_id", id);
            postsmap.put("profile_img_url", profile_img_url);

            DocumentReference documentReference = pStorage.collection("posts").document(id);
            documentReference.set(postsmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(PostActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        toMainActivity();
                    } else {
                        Toast.makeText(PostActivity.this, "Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }

    private void toMainActivity() {
        startActivity(new Intent(PostActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}