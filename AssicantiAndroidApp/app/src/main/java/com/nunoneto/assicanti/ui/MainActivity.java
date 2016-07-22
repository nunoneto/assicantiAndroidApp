package com.nunoneto.assicanti.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.Price;
import com.nunoneto.assicanti.ui.fragment.CustomerDataFragment;
import com.nunoneto.assicanti.ui.fragment.MenuFragment;
import com.nunoneto.assicanti.ui.fragment.OnOrderFragmentListener;
import com.nunoneto.assicanti.ui.fragment.OptionalsFragment;
import com.nunoneto.assicanti.ui.fragment.OrderSummaryFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnOrderFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showMenus();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

            }
        });
    }

    private void showMenus(){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,new MenuFragment(),MenuFragment.NAME)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.dayMenu) {

        } else if (id == R.id.weekMenu) {

        }else if (id == R.id.myOrders) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void showOptionals(Price price) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, OptionalsFragment.newInstance(price.getItemId(),price.getSize(),price.getTier(),price.getId()),OptionalsFragment.NAME)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showForm() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, CustomerDataFragment.newInstance(),CustomerDataFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void goToSummary() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, OrderSummaryFragment.newInstance(),OrderSummaryFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

}
