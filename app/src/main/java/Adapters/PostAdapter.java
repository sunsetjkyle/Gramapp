package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gramapp.CommentActivity;
import com.example.gramapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import Models.Posts;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context pContext;
    ArrayList<Posts> pPostsArraylist;
    FirebaseAuth mAuth;
    String  user, post_id;
    FirebaseFirestore db;


    public PostAdapter(Context pContext, ArrayList<Posts> pPostsArraylist) {
        this.pContext = pContext;
        this.pPostsArraylist = pPostsArraylist;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(pContext).inflate(R.layout.post_item_list, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser().getUid();

Posts posts = pPostsArraylist.get(position);
post_id = posts.getPost_id().toString();
holder.caption.setText(posts.getCaption());
holder.name.setText(posts.getFull_names());
        Glide.with(pContext).load(pPostsArraylist.get(position).getPost_img_url()).into(holder.post);
        Glide.with(pContext).load(pPostsArraylist.get(position).getProfile_img_url()).into(holder.profImage);
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts").document(post_id);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot  = task.getResult();
                            if (documentSnapshot.exists()) {
                                String post_imgurl = documentSnapshot.getString("post_img_url");
//                                Toast.makeText(pContext, post_imgurl, Toast.LENGTH_SHORT).show();
                                HashMap savemap = new HashMap();
                                savemap.put("post_img_url", post_imgurl);
                                //vshoes, ,men Jumia,
                                savemap.put("liked_by", user);

                                DocumentReference reference =  FirebaseFirestore.getInstance().collection("saves").document(user)
                                        .collection("my_saves").document();
                                reference.set(savemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(pContext, "Post saved", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(pContext, "Failed!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            } else {

                            }
                        }
                        else {
                            Log.d("Error", "get failed with", task.getException());
                        }
                    }
                });
            }
        });


holder.more_icon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(pContext, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.edit:
                        editPost(posts.getPost_id());
                        return  true;
                    case R.id.delete:
                        deletePost();
                        return true;
                    case R.id.report:
                        Toast.makeText(pContext, "Report sent!", Toast.LENGTH_SHORT).show();
                        return  true;

                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        if (!posts.getPublisher().equals(user)){
            popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
            popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
        }
        popupMenu.show();
    }
});


        holder.comment.setOnClickListener(new
                                                  View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                          Intent comment_intent = new Intent(pContext, CommentActivity.class);
comment_intent.putExtra("post_id", posts.getPost_id());
comment_intent.putExtra("publisher", posts.getPublisher());
pContext.startActivity(comment_intent);
                                                      }
                                                  });

    }

    private void deletePost() {
        db.collection("posts").document(post_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    pPostsArraylist.remove(post_id);notifyDataSetChanged();
                    Toast.makeText(pContext, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(pContext, "Failed to delete post" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void editPost(String post_id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(pContext);
        alertDialog.setTitle("Edit Post");
        final EditText editText = new EditText(pContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(layoutParams);
        alertDialog.setView(editText);

getText(post_id, editText);

alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("caption", editText.getText().toString());


        FirebaseFirestore.getInstance().collection("posts").document(post_id).update(hashMap);
        notifyDataSetChanged();
    }
});
alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
    }
});
alertDialog.show();

    }

    private void getText(String post_id, EditText editText) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts").document(post_id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String caption = documentSnapshot.getString("caption");
                        editText.setText(caption);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return pPostsArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView caption, name ;
        CircleImageView profImage;
        ImageView chat, like, comment, share, save, post, more_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.post_description);
            name = itemView.findViewById(R.id.post_name);
            profImage = itemView.findViewById(R.id.post_profile_image);
            like = itemView.findViewById(R.id.fav);
            save = itemView.findViewById(R.id.save);
            post = itemView.findViewById(R.id.postfinal_placeholder);
            comment= itemView.findViewById(R.id.chat);
            more_icon = itemView.findViewById(R.id.more_icon);

        }
    }
}
