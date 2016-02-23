package info.prateep.android.mymealbag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        //Below code is dummy. Feel free to refactor
        String[] data = {
                "3 Pulka, Palak Dal : 50",
                "3 Pulka, Potato Curry : 60",
                "Rice, Palak Dal : 50",
                "Curd Rice, Pickle : 30",
                "Veg Rice, Dal : 50",
                "Rice, Sambar : 50",
                "Pulka, Dal : 40"
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
        ListView listView = (ListView) findViewById(R.id.listview_meal_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
