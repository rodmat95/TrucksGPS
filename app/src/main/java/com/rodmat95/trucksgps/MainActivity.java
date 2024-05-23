package com.rodmat95.trucksgps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rodmat95.trucksgps.Main.DashboardFragment;
import com.rodmat95.trucksgps.Main.HomeFragment;
import com.rodmat95.trucksgps.Main.UserFragment;

public class MainActivity extends AppCompatActivity {

    DashboardFragment dashboardFragment = new DashboardFragment();
    HomeFragment homeFragment = new HomeFragment();
    UserFragment userFragment = new UserFragment();

    enum ProviderType {
        BASIC
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(dashboardFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.dashboardFragment) {
                loadFragment(dashboardFragment);
                return true;
            } else if (itemId == R.id.homeFragment) {
                loadFragment(homeFragment);
                return true;
            } else if (itemId == R.id.userFragment) {
                loadFragment(userFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}