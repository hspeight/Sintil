package uk.co.sintildate.sintil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
//public class MainActivity extends AppCompatActivity implements NewEventDialogFragment.onNewEventAdded {

    private DrawerLayout mDrawerLayout;
    public String DEBUG_TAG = "MAC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //if(getSupportActionBar() != null)
        //    getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Check that the activity is using the layout version with
            // the fragment_container FrameLayout
            if (findViewById(R.id.fragment_container) != null) {

                // Create a new Fragment to be placed in the activity layout
                HSFrag firstFragment = new HSFrag();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                //getFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        //.add(R.id.fragment_container, firstFragment).addToBackStack("fragBack").commit();
                        .add(R.id.fragment_container, firstFragment).commit();
            }

        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                displayFrag(menuItem.getItemId());
                //Toast.makeText(MainActivity.this, menuItem.getGroupId(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayFrag(int position) {

        switch (position) {
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment()).addToBackStack("fragBack")
                        .commit();
                break;
            case R.id.navigation_item_utilitiy:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Utility()).addToBackStack("fragBack")
                        .commit();
                break;
        }

    }
/*
    public void onEventAdded(boolean recAdded) {

        //if (dbHandler.getRowCount("A") > eventRecord.size()) {
        if(recAdded) {
            HSFrag hsfrag = new HSFrag();



            //Toast.makeText(this, "something was added" , Toast.LENGTH_SHORT).show();
            //setup_list();
            //} else {
            //Toast.makeText(this, "no addition" , Toast.LENGTH_SHORT).show();

        }
    }
*/
}
