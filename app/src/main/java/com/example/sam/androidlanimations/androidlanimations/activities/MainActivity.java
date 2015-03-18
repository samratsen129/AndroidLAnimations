package com.example.sam.androidlanimations.androidlanimations.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sam.androidlanimations.androidlanimations.fragments.AdvRecyclerFragment;
import com.example.sam.androidlanimations.androidlanimations.fragments.BasicFragment;
import com.example.sam.androidlanimations.androidlanimations.fragments.DataProviderFragment;
import com.example.sam.androidlanimations.androidlanimations.fragments.NavigationDrawerFragment;
import com.example.sam.androidlanimations.androidlanimations.adapters.ListView1Adapter;

import java.util.HashMap;

import com.example.sam.androidlanimations.R;
import com.example.sam.androidlanimations.androidlanimations.model.AbstractDataProvider;
import com.example.sam.androidlanimations.androidlanimations.utils.BackgroundContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private RecyclerView mListView1;
    private ListView1Adapter mAdapterListView1;
    private Context context;
    private String mTitle="";

    BackgroundContainer mBackgroundContainer;
    boolean mSwiping = false;
    boolean mItemPressed = false;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new DataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new BasicFragment();
                break;
            case 1:
                fragment = new AdvRecyclerFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_basic);
                break;
            case 2:
                mTitle = getString(R.string.title_adv_recycler);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            //startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void removeItem(int position){
        //mAdapterListView1.loadData(SampleData1.getInstance().removeData(position));
        SnackbarManager.show(
                Snackbar.with(MainActivity.this)
                        .text("Item Deleted")
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                        .actionLabel("Undo")
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Toast.makeText(context, "TBD - Not Implemented", Toast.LENGTH_LONG);
                            }
                        }));
    }

    public AbstractDataProvider getDraggableDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((DataProviderFragment) fragment).getDataProvider();
    }
}
