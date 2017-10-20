package com.android.tutor.utilities;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Gufran on 10/20/2017.
 */

public class CommonMethods {
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        boolean isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        if (!isConnected) {
            CustomDialogs.getInternetAlertDialog(context, "Please check your internet connection or try again later.", "");
        }

        return isConnected;
    }

}
