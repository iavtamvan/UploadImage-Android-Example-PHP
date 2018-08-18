package com.can.uploadimage.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by LENOVO on 26/10/2017.
 */

public class PermissinCheker {
    private final Context context;

    public PermissinCheker(Context context) {
        this.context = context;
    }

    public boolean lacksPermissions(String... permissions){
        for (String permission : permissions){
            if (lackPermission(permission)){
                return true;
            }
        }
        return false;
    }
    private boolean lackPermission(String per){
        return ContextCompat.checkSelfPermission(context, per) == PackageManager.PERMISSION_DENIED;
    }
}
