package com.alokbharti.donationbox;

import android.content.Intent;
import android.os.Bundle;

import com.alokbharti.donationbox.ui.auth.PhoneAuthActivity;

import android.view.MenuItem;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private boolean isFirsTimeInternetCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_donate, R.id.nav_user_product)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        MenuItem contactUsItem = navigationView.getMenu().findItem(R.id.nav_contact);
        contactUsItem.setOnMenuItemClickListener(item -> {
            onBackPressed();
            String email = "bhartialok.257@gmail.com";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding my problem");
            startActivity(Intent.createChooser(intent, "Contact us..."));
            return true;
        });

        observeInternetStatusChanges();
        View headerView = navigationView.getHeaderView(0);
        TextView userPhoneNumber = headerView.findViewById(R.id.user_phone_number);
        userPhoneNumber.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
    }

    private void observeInternetStatusChanges() {
        new InternetConnectionLiveData(this).observe(this, isInternetConnected ->{
            if (!isInternetConnected){
                UtilFunctions.displaySnackBar(this, drawer, "You're offline, check your internet!!", 250);
            } else {
                if (!isFirsTimeInternetCheck) UtilFunctions.displaySnackBar(this, drawer, "We're back again", 250);
                else isFirsTimeInternetCheck = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirsTimeInternetCheck = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out){
            FirebaseAuth.getInstance().signOut();
            GlobalSettingsRepository.setUserType(this,"");
            startActivity(new Intent(this, PhoneAuthActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
