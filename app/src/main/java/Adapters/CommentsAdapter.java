package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gramapp.R;

import java.util.List;

import Models.Comments;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    Context cContext;
List <Comments> cCommentArraylist;
String post_id;

    public CommentsAdapter(Context cContext, List<Comments> cCommentArraylist, String post_id) {
        this.cContext = cContext;
        this.cCommentArraylist = cCommentArraylist;
        this.post_id = post_id;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cContext).inflate(R.layout.comment_list_item, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comments comments = cCommentArraylist.get(position);
        holder.final_comment.setText(comments.getComment());
        holder.final_name.setText(comments.getFull_names());

        Glide.with(cContext).load(cCommentArraylist.get(position).getProfile_img_url()).into(holder.comment_final_dp);



    }
//    return size of array list

    @Override
    public int getItemCount() {
        return cCommentArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

CircleImageView comment_final_dp;
TextView final_name, final_comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_final_dp = itemView.findViewById(R.id.comment_final_dp);
            final_name = itemView.findViewById(R.id.final_username);
            final_comment = itemView.findViewById(R.id.final_comment);
        }
    }
}
