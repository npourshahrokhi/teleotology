package com.doctor.ashanet.ashanet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppConfig {
    public static String DATABASE_NAME = "ashanet";
    public static Integer DATABASE_VERSION = 2;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;
    private static int TYPE_WIFI = 1;

    public static int getConnectivityStatus(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == 1 && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return TYPE_WIFI;
            }
            if (networkInfo.getType() == 0 && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isNetworkConnected(Context context) {
        int networkStatus = getConnectivityStatus(context);
        if (networkStatus == TYPE_WIFI || networkStatus == TYPE_MOBILE) {
            return true;
        }
        return false;
    }
}
