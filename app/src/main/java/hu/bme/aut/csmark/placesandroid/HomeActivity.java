package hu.bme.aut.csmark.placesandroid;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.fragment.AddPlaceFragment;
import hu.bme.aut.csmark.placesandroid.fragment.HomeFragment;
import hu.bme.aut.csmark.placesandroid.fragment.OwnPlacesFragment;
import hu.bme.aut.csmark.placesandroid.model.account.Account;
import hu.bme.aut.csmark.placesandroid.model.account.AccountDatabase;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static private AccountDatabase database;
    static private Account userAccount;
    static private List<Account> allAccounts;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    FragmentTransaction fragmentTransaction;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundleSearcher = new Bundle();
        bundleSearcher.putString(getString(R.string.searcher), getString(R.string.no_search));
        Fragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundleSearcher);
        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.commit();

        TextView nameNavBar = headerView.findViewById(R.id.nameNavBar);
        TextView emailNavBar =  headerView.findViewById(R.id.emailNavBar);

        database = Room.databaseBuilder(
                getApplicationContext(),
                AccountDatabase.class,
                getString(R.string.accountTable)
        ).build();

        long id;

        id = getIntent().getLongExtra(getString(R.string.userID), -1);

        Log.i(getString(R.string.ID), String.valueOf(id));

        userAccount = AccountDatabase.getAccountbyId(id, database);

        Log.i(getString(R.string.entering),userAccount.id + getString(R.string.colon) + userAccount.name);

        nameNavBar.setText(userAccount.name);
        emailNavBar.setText(userAccount.email);

        bundle = new Bundle();
        bundle.putLong(getString(R.string.userID), id);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_all) {
            Bundle bundleSearcher = new Bundle();
            bundleSearcher.putString(getString(R.string.searcher), getString(R.string.no_search));
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            homeFragment.setArguments(bundleSearcher);
            fragmentTransaction.replace(R.id.main_container, homeFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.all_places);
        } else if (id == R.id.nav_add) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment addPlaceFragment = new AddPlaceFragment();
            addPlaceFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_container, addPlaceFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.add_new_place);
        } else if (id == R.id.nav_myplaces) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment ownPlacesFragment = new OwnPlacesFragment();
            ownPlacesFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.main_container, ownPlacesFragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.own_places);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
