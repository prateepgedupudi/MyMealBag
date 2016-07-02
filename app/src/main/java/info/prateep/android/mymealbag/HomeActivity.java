package info.prateep.android.mymealbag;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import info.prateep.android.mymealbag.login.LoginActivity;
import info.prateep.android.mymealbag.model.User;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    String photoUrl;
    TextView usrEmailView;
    TextView usrNameView;
    Map<String,List<String>> myItems;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // Firebase database
    FirebaseDatabase database;
    DatabaseReference myRef;

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


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Get currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //If user is not logged in then open Login Page
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        //Get the user details
        else {
            Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
            if (mFirebaseUser.getPhotoUrl() != null) {
                photoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
            //We can not directly get headerLayout items from Android Support Library 23.1.1 onwards. Below is the way to get those.
            View headerLayout = navigationView.getHeaderView(0);
            usrEmailView = (TextView)headerLayout.findViewById(R.id.userEmail);
            usrNameView = (TextView)headerLayout.findViewById(R.id.userName);
            this.getUserInFireBase(mFirebaseUser.getUid());
            //TODO Write seperate method to Get User object from firebase after getting user authentication by Uid. Which should give user complete name, email, photo url ect...
        }

        //Below code is for dummy. Feel free to refactor
        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        List<String> data;
        if(myItems!=null){
            for (Map.Entry<String,List<String>> entry:myItems.entrySet()
                 ) {
               StringBuffer day = new StringBuffer(entry.getKey()).append(":");
                List<String> items=entry.getValue();
                for (String item:items
                     ) {
                    day.append(item).append("/n");
                }

            }
        }
        /*String[] data = {
                "MON - 15 FEB16 - Rice,Daal,Curry",
                "TUE - 16 FEB16 - Choose Meal",
                "WED - 17 FEB16 - Choose Meal",
                "THU - 18 FEB16 - Choose Meal",
                "FRI - 19 FEB16 - Choose Meal",
                "SAT - 20 FEB16 - Choose Meal",
                "SUN - 21 FEB16 - Choose Meal",
        };*/

        List<String> weekMealForecast = new ArrayList<String>(7);

        final ArrayAdapter<String> mMealForecastAdapter =
                new ArrayAdapter<String>(
                        getApplicationContext(), // The current context (this activity)
                        R.layout.grid_item_forecast, // The name of the layout ID.
                        R.id.grid_item_forecast_textview, // The ID of the textview to populate.
                        weekMealForecast);
        GridView gridView = (GridView) findViewById(R.id.gridview_forecast);
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
        switch (item.getItemId()) {
            case R.id.nav_favorite:
                //Handle fovorite
                break;
            case R.id.nav_order_history:
                break;
            case R.id.nav_faq:
                break;
            case R.id.nav_contact:
                break;
            case R.id.nav_sign_out:
                //TODO clear logged in user details in shared preferences
                //Redirect page to LoginActivity
                mFirebaseAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserInFireBase(final String uid) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef.child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        if(user!=null){
                            usrEmailView.setText(user.getEmail());
                            usrNameView.setText(user.getName()+" M:"+user.getMobile());
                            myItems=user.getItems();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(LOG_TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }
}
