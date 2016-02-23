package info.prateep.android.mymealbag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);

        //Below code is dummy. Feel free to refactor
        String[] data = {
                "South Indian By Prateep",
                "North Indian By Bakshi",
                "Punjabi By Prateep Singh",
                "Gujarati By Patel Das",
                "Sweets By Prateep Gedupudi",
                "Andhra Special By Maha",
                "Chettinadu Style By Prateep"
        };
        List<String> weekCheffForecast = new ArrayList<String>(Arrays.asList(data));
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        final ArrayAdapter<String> mForecastAdapter =
                new ArrayAdapter<String>(
                        getApplicationContext(), // The current context (this activity)
                        R.layout.grid_item_forecast, // The name of the layout ID.
                        R.id.grid_item_forecast_textview, // The ID of the textview to populate.
                        weekCheffForecast);


        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) findViewById(R.id.listview_cheff_forecast);
        gridView.setAdapter(mForecastAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), MealActivity.class);
                startActivity(intent);
            }
        });
    }
}
