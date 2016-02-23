package info.prateep.android.mymealbag;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Below code is for dummy. Feel free to refactor
        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "MON - 15 FEB16 - Rice,Daal,Curry",
                "TUE - 16 FEB16 - Choose Meal",
                "WED - 17 FEB16 - Choose Meal",
                "THU - 18 FEB16 - Choose Meal",
                "FRI - 19 FEB16 - Choose Meal",
                "SAT - 20 FEB16 - Choose Meal",
                "SUN - 21 FEB16 - Choose Meal",
        };
        List<String> weekMealForecast = new ArrayList<String>(Arrays.asList(data));
        final ArrayAdapter<String> mMealForecastAdapter =
                new ArrayAdapter<String>(
                        getApplicationContext(), // The current context (this activity)
                        R.layout.grid_item_forecast, // The name of the layout ID.
                        R.id.grid_item_forecast_textview, // The ID of the textview to populate.
                        weekMealForecast);
        GridView gridView= (GridView)findViewById(R.id.gridview_forecast);
        gridView.setAdapter(mMealForecastAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = mMealForecastAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), ChefActivity.class);
                startActivity(intent);
            }
        });





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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {
            // Handle the camera action
        } else if (id == R.id.nav_order_history) {

        } else if (id == R.id.nav_payment_history) {

        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_contact) {

        }else if (id == R.id.nav_sign_out) {
            //TODO clear logged in user details in shared preferences

            //Redirect page to LoginActivity
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
