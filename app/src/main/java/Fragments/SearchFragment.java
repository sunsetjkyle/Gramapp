package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.gramapp.MainActivity;
import com.example.gramapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Adapters.UserAdapter;
import Models.User;


public class SearchFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
//   private Toolbar sToolbar;
    private RecyclerView recyclerView;
    ArrayList<User> suserArraylist;
    UserAdapter userAdapter;
    SearchView searchView;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).getSupportActionBar().show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        sToolbar = view.findViewById(R.id.search_toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        searchView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.search_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        suserArraylist = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), suserArraylist);
        recyclerView.setAdapter(userAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUsers(newText);
                return true;
            }
        });

        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                        User user = documentSnapshot.toObject(User.class);
                        suserArraylist.add(user);
                        userAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    private void searchUsers(String newText) {
        firebaseFirestore.collection("Users").whereEqualTo("User_email", newText.toLowerCase()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        suserArraylist.clear();
                        for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                            User user = documentSnapshot.toObject(User.class);
                            suserArraylist.add(user);
                            userAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}