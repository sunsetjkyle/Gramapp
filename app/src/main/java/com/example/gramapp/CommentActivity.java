package com.example.gramapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapters.CommentsAdapter;
import Models.Comments;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    Toolbar comments_toolbar;
    RecyclerView comments_recyclerview;
    List <Comments> cCommentlist;
    CommentsAdapter cCommentsAdapter;


    CircleImageView comments_profileImg;
    ImageView back_img;
    TextView post;
    EditText write_acomment;

    String publisher, post_id, img_url, Full_names, user;

    private FirebaseAuth cAuth;
    FirebaseFirestore cFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        comments_toolbar = findViewById(R.id.toolbar_comments);
        setSupportActionBar(comments_toolbar);
        getSupportActionBar().setTitle("Comments");


        comments_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        publisher = intent.getStringExtra("publisher");
        post_id = intent.getStringExtra("post_id");

        comments_recyclerview = findViewById(R.id.recycler_comments);
        comments_recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        comments_recyclerview.setLayoutManager(linearLayoutManager);
        cCommentlist = new ArrayList<>();
        cCommentsAdapter = new CommentsAdapter(this, cCommentlist, post_id);
        comments_recyclerview.setAdapter(cCommentsAdapter);

        cAuth =  FirebaseAuth.getInstance();
        cFirestore = FirebaseFirestore.getInstance();
        user = cAuth.getCurrentUser().getUid();

        write_acomment = findViewById(R.id.write_comment);
        comments_profileImg = findViewById(R.id.comments_profile);
        back_img = findViewById(R.id.comments_back);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommentActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        });
        post = findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });
        getProfileData();
        readComments();
    }

    private void readComments() {
        cFirestore.collection("Comments")
                .whereEqualTo("post_id", post_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Error", error.getMessage());
                    return;
                }
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if (documentChange.getType()==DocumentChange.Type.ADDED){
                        cCommentlist.add(documentChange.getDocument().toObject(Comments.class));
                    }
                    cCommentsAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void getProfileData() {
        DocumentReference documentReference = cFirestore.collection("Users").document(user);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    Glide.with(getApplicationContext()).load(value.getString("profile_img_url")).placeholder(R.drawable.profilepng).into(comments_profileImg);
                    img_url = value.getString("profile_img_url");
                    Full_names = value.getString("Full_names");

                }

            }
        });
    }

    private void comment() {
        String txt_comment = write_acomment.getText().toString();
        String id = cFirestore.collection("Comments").document().getId();

        if (TextUtils.isEmpty(txt_comment)){
            write_acomment.setError("Cannot be Empty");

        }
        else {
            HashMap commentsmap = new HashMap();
            commentsmap.put("comment", txt_comment);
            commentsmap.put("comment_id", id);
            commentsmap.put("publisher", user);
            commentsmap.put("post_id", post_id);
            commentsmap.put("profile_img_url", img_url);
            commentsmap.put("Full_names", Full_names);

            DocumentReference documentReference = cFirestore.collection("Comments").document(id);
            documentReference.set(commentsmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CommentActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                        write_acomment.setText("");
                    }
                    else {
                        Toast.makeText(CommentActivity.this, "Failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }
    }
}