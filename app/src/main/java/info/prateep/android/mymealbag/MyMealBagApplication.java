package info.prateep.android.mymealbag;

import com.firebase.client.Firebase;

/**
 * Created by prateep.gedupudi on 2/23/2016.
 * This calss includes one-time initialization of Firebase related code
 */
public class MyMealBagApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        Firebase.setAndroidContext(this);

    }
}
