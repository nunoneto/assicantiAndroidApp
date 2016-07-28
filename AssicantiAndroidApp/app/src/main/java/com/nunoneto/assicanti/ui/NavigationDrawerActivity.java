package com.nunoneto.assicanti.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.entity.realm.DayMenu;
import com.nunoneto.assicanti.ui.dialog.ExistingCustomerDataDialogFragment;
import com.nunoneto.assicanti.ui.dialog.ExitAppDialogFragment;
import com.nunoneto.assicanti.ui.dialog.YesNoDialogListener;

/**
 * Created by nb20301 on 22/07/2016.
 */
public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
             if(getSupportFragmentManager().getBackStackEntryCount() == 1){
                ExitAppDialogFragment diag = ExitAppDialogFragment.newInstance(new YesNoDialogListener() {
                    @Override
                    public void yes() {
                       NavigationDrawerActivity.this.finish();
                    }

                    @Override
                    public void no() {

                    }
                });
                 diag.show(getFragmentManager(), ExitAppDialogFragment.TAG);
            }else{
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.dayMenu) {
            Intent i = new Intent(getApplicationContext(),DayMenuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.weekMenu) {

        }else if (id == R.id.myOrders) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
