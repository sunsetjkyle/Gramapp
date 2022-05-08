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

import java.util.ArrayList;

import Models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context sContext;
    ArrayList<User> sArraylist;

    public UserAdapter(Context sContext, ArrayList<User> sArraylist) {
        this.sContext = sContext;
        this.sArraylist = sArraylist;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(sContext).inflate(R.layout.search_item_list, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = sArraylist.get(position);
        holder.fullname.setText(user.getFull_names());
        holder.email.setText(user.getUser_email());
        Glide.with(sContext).load(sArraylist.get(position).getProfile_img_url()).into(holder.proflimg);


    }

    @Override
    public int getItemCount() {
        return sArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView proflimg;
        TextView fullname, email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            proflimg = itemView.findViewById(R.id.search_profile_img);
            fullname= itemView.findViewById(R.id.UserFullnames);
            email= itemView.findViewById(R.id.Userfull_email);
        }
    }
}
