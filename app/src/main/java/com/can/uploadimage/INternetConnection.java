package com.can.uploadimage;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by LENOVO on 26/10/2017.
 */

public class INternetConnection {

    public static boolean checkConnection(Context context){

        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
