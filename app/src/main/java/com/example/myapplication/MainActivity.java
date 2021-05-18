package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.fragment.AboutFragment;
import com.example.myapplication.fragment.HomeMenuFragment;
import com.example.myapplication.fragment.LogoutFragment;
import com.example.myapplication.fragment.UserDataFragment;
import com.example.myapplication.preference.SessionPrefference;
import com.example.myapplication.model.DataUser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    TextView headerTxt;
    ImageView nav_header_imageView;
    DrawerLayout drawerLayout;
    SessionPrefference sessionPrefference;
    DataUser dataUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionPrefference = new SessionPrefference(getApplicationContext());

        dataUser = new DataUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("User").child(sessionPrefference.getPhone());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("DATA CHANGEt", "onDataChange: " + dataSnapshot.getValue());
                    dataUser = dataSnapshot.getValue(DataUser.class);
//                    headerTxt.setText(dataUser.getEmail());
                    Log.d("dataUser getAddress", "get: " + dataUser.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigation = findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

//        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigation, false);
//        navigation.addHeaderView(headerView);

        View header = navigation.getHeaderView(0);
        headerTxt = (TextView) header.findViewById(R.id.navheadertextView);
        nav_header_imageView = header.findViewById(R.id.nav_header_imageView);


        headerTxt.setText("XYZ");
        nav_header_imageView.setImageResource(R.drawable.googleg_standard_color_18);

        Fragment fragment = new HomeMenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.linear, fragment);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fg;
        switch (id) {
            case R.id.nav_home:
                fg = new HomeMenuFragment();
                break;
            case R.id.nav_about:
                fg = new AboutFragment();
                break;
            case R.id.nav_user:
                fg = new UserDataFragment();
                break;
            case R.id.nav_logout:
                fg = new LogoutFragment();
                break;
            default:
                fg = null;
        }
        if (fg != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.linear,fg);
            ft.addToBackStack(null);
            ft.commit();
            drawerLayout.closeDrawer(GravityCompat.START,true);

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}