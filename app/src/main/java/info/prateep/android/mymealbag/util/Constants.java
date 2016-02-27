package info.prateep.android.mymealbag.util;

import info.prateep.android.mymealbag.BuildConfig;

/**
 * Created by prateep.gedupudi on 2/26/2016.
 */
public final class Constants {
    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;

}
