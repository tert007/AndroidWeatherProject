package com.example.alexander.test2.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alexander on 10.04.2016.
 */
public class ConnectionTracker {

    private Context context;

    public ConnectionTracker(Context context){
        this.context = context;
    }

    public boolean canConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return true;
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }

        return false;
    }
}
