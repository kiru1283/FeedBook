package androidapp.feedbook.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import androidapp.feedbook.R;

/**
 * Activity to hold the navigation bar for the application to display options to user
 */
public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Intent intentLogin = getIntent();
        if(intentLogin.hasExtra(Intent.EXTRA_TEXT)){
            userid = intentLogin.getStringExtra(Intent.EXTRA_TEXT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Fragment     fragment = ViewFeedFragment.newInstance(userid);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        }

    /**
     * Method to diable back button
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            Intent moveToMain = new Intent(this.getApplicationContext(), LoginActivity.class);
            moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            moveToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(moveToMain);
            NavActivity.this.finish();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to load the fragment based on the option selected
     * @param item - the item selected from the navigation bar
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_Subscribe) {
            // Handle the subscription action
            fragment = SubscribeFragment.newInstance(userid);

        } else if (id == R.id.nav_Feed) {
            //view feeds
            fragment = ViewFeedFragment.newInstance(userid);
        } else if (id == R.id.nav_Fav) {
            //view favourites
            fragment = ViewFavFragment.newInstance(userid);

        } else if (id == R.id.nav_Manage) {
            //to handle the unsubscribe event
            fragment = ManageFragment.newInstance(userid);

        } else if (id == R.id.log_out) {
            //to close the fragment and go to login screen
            Intent moveToMain = new Intent(this.getApplicationContext(), LoginActivity.class);
            moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            moveToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(moveToMain);
            NavActivity.this.finish();

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
