package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gramapp.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Adapters.PostAdapter;
import Models.Posts;


public class HomeFragment extends Fragment {

private RecyclerView postRecyclerview;
private FirebaseFirestore hFirestore;


ArrayList<Posts> postsArrayList;
PostAdapter pPostAdapter;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Posts");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

View view = inflater.inflate(R.layout.fragment_home, container, false);
        hFirestore = FirebaseFirestore.getInstance();
        postRecyclerview = view.findViewById(R.id.rv_posts);
        postRecyclerview.setHasFixedSize(true);
        postRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        postsArrayList = new ArrayList<Posts>();
        pPostAdapter = new PostAdapter(getContext(), postsArrayList);
        postRecyclerview.setAdapter(pPostAdapter);

        eventChangeListener();



        return view;


    }

    private void eventChangeListener() {
        hFirestore.collection("posts").orderBy("post_current_time", Query.Direction.DESCENDING).
                addSnapshotListener(new
                                                                                                                            EventListener<QuerySnapshot>() {
                                                                                                                                @Override
                                                                                                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
if (error != null){
    Log.e("Error!", error.getMessage());
    return;

}
for (DocumentChange documentChange: value.getDocumentChanges()){
    if (documentChange.getType() == DocumentChange.Type.ADDED){
postsArrayList.add(documentChange.getDocument().toObject(Posts.class));
    }
pPostAdapter.notifyDataSetChanged();
}
                                                                                                                                }
                                                                                                                            });

    }

}