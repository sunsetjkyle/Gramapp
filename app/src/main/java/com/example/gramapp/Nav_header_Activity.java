package com.example.gramapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Nav_header_Activity extends AppCompatActivity {
    private FirebaseAuth nAuth;
    FirebaseFirestore firebaseFirestore;
    String user,  fullname, img_url;
    CircleImageView nav_profile;
    TextView nev_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        nAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        nav_profile = findViewById(R.id.nav_profile);
//        nav_profile.setOnClickListener(this::In¿¿¿);
        nev_name = findViewById(R.id.nav_name);

        user = nAuth.getCurrentUser().getUid();

//        DocumentReference documentReference = firebaseFirestore.collection("Users").document(user);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (value.exists()) {
//                    Glide.with(getApplicationContext()).load(value.get("profile_img_url")).placeholder(R.drawable.profilepng).into(nav_profile);
//                    img_url = value.getString("profile_img_url");
//                    fullname = value.getString("Full_names");
//                }
//
//            }
//        });


    }
}