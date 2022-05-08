package com.example.gramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Fragments.AccountFragment;
import Fragments.HomeFragment;
import Fragments.ReelsFragment;
import Fragments.SearchFragment;
import Fragments.SettingsFragment;
import Fragments.ShopFragment;

public class MainActivity extends  AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottom_nav;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar, navtoolbar;
    Fragment selectorFragment = null;
    private  MenuItem logout;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        getSupportActionBar().setIcon(R.drawable.ic_instagram_text_logo);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id==R.id.addpost){
                    Toast.makeText(MainActivity.this, "Add a post", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, PostActivity.class));
                    finish();
                }
                if (id==R.id.like)
                {
                    Toast.makeText(MainActivity.this, "Favourites", Toast.LENGTH_SHORT).show();
                }
                if (id==R.id.messenger){
                    Toast.makeText(MainActivity.this, "Open Messages", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
//        bottom_nav.setSelectedItemId(R.id.home_icon);
        getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment , new HomeFragment()).commit();


        navtoolbar = findViewById(R.id.nav_toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header);
        navigationView.setNavigationItemSelectedListener(this);

        // logout = findViewById(R.id.logout);
        //logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
        //  @Override
        //public boolean onMenuItemClick(MenuItem menuItem) {
        // logOutUser();

        //return false;
        // }
        //});


    }



    private void sendToLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater tInflator = getMenuInflater();
        tInflator.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_icon:
                selectorFragment = new HomeFragment();
                break;
//             getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, new HomeFragment()).commit();
//                Toast.makeText(this, "This is the Homepage", Toast.LENGTH_SHORT).show(); break;

            case R.id.search_icon:
                selectorFragment = new SearchFragment();
                break;
//                getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, new SearchFragment()).commit();
//                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            case R.id.reels_icon:
                selectorFragment = new ReelsFragment();
                break;
//                getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, new ReelsFragment()).commit();
//                Toast.makeText(this, "Continue watching reels", Toast.LENGTH_SHORT).show(); break;
            case R.id.shop_icon:
                selectorFragment = new ShopFragment();
                break;
//                getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, new ShopFragment()).commit();
//                Toast.makeText(this, "Instagram Online store", Toast.LENGTH_SHORT).show(); break;
            case R.id.account_icon:
                selectorFragment = new AccountFragment();
                break;
            case R.id.saved:
                Toast.makeText(this, "Open saved posts", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(this, "Share profile", Toast.LENGTH_SHORT).show(); break;
            case R.id.subs:
                Toast.makeText(this, "View your subscriptions", Toast.LENGTH_SHORT).show();break;
            case R.id.rate:
                Toast.makeText(this, "Rate Us on Playstore", Toast.LENGTH_SHORT).show(); break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, new SettingsFragment()).commit();
                Toast.makeText(this, "Open Settings", Toast.LENGTH_SHORT).show(); break;
            case R.id.sytem_update:
                Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show(); break;
            case R.id.logout:
                logOutUser();


        }
        if (selectorFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.host_fragment, selectorFragment).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOutUser() {
        mAuth.signOut();
        sendToLogin();
    }

}