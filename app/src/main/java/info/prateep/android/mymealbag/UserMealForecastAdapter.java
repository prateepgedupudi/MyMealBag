package info.prateep.android.mymealbag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by prateep.gedupudi on 04-07-2016.
 */
public class UserMealForecastAdapter extends BaseAdapter {

    private Map<String, String> mData = new TreeMap<String, String>();
    private String[] mKeys;


    public UserMealForecastAdapter(Map<String, String> data) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        //Creation of ematy dates and data for a week.
        mData.put(df.format(calendar.getTime()), "Add Meal");
        for (int i = 1; i < 7; i++) {
            calendar.add(calendar.DATE, 1);
            mData.put(df.format(calendar.getTime()), "Add Meal");
        }
        //Clearing the Calendar to get current date again
        calendar = Calendar.getInstance();
        //Sync the data with fire base data
        for (String currentKey : data.keySet()) {
            try {
                //Show meal data for only current and future dates up to seven days
                if (df.parse(currentKey).compareTo(calendar.getTime()) >= 0) {
                    mData.put(currentKey, data.get(currentKey));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        mKeys = mData.keySet().toArray(new String[mData.size()]);
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mData.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mData.get(mKeys[position]);
    }

    public Object getItemKey(int position) {
        return mKeys[position];
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View resultView;
        String key = mKeys[position];
        String value = getItem(position).toString();
        if (convertView == null) {
            resultView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_meal_forecast_list_item, parent, false);
        } else {
            resultView = convertView;
        }

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date MyDate = null;
        try {
            MyDate = newDateFormat.parse(key);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newDateFormat.applyPattern("EEEE");
        String myDay = newDateFormat.format(MyDate);
        ((TextView) resultView.findViewById(R.id.user_meal_forevast_list_item_day_textview)).setText(myDay);
        ((TextView) resultView.findViewById(R.id.user_meal_forevast_list_item_date_textview)).setText(key);
        ((TextView) resultView.findViewById(R.id.user_meal_forevast_list_item_meal_textview)).setText(value);


        return resultView;
    }
}
