package com.can.uploadimage.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by LENOVO on 26/10/2017.
 */

public class permissionActivity extends AppCompatActivity {

    public static final int DENIED = 1;
    public static final int GRANDED = 0;

    private static final int PERMISSION_REQQUEST_CODE = 0;
    private static  final String EXTRA_PERMISSION =  "com.can.uploadimage.EXTRA_PERMISSION";
    private static final String PACK_URL_SCHEMA = "package:";

    private PermissinCheker cheker;
    private boolean requresCheck;

    public static void  startActivityForResult(Activity activity, int reqCode, String... permission){
        Intent intent  = new Intent(activity, permissionActivity.class  );
        intent.putExtra(EXTRA_PERMISSION, permission);
        ActivityCompat.startActivityForResult(activity, intent, reqCode, null);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if (getIntent()  == null || !getIntent().hasExtra(EXTRA_PERMISSION)){
            throw new RuntimeException("activity ini menampilkaan metode startActivityForResult()");

        }

        cheker = new PermissinCheker(this);
        requresCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requresCheck){
            String[] permission = getPermissions();


            if (cheker.lacksPermissions(permission)){
                requestPermission(permission);
            }else {
                allPermissionGranted();
            }
        }else {
            requresCheck = true;
        }
    }

    private void allPermissionGranted() {
        setResult(DENIED);
        finish();
    }

    private void requestPermission(String[] permission) {
        ActivityCompat.requestPermissions(this, permission, PERMISSION_REQQUEST_CODE);
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQQUEST_CODE && hasAllPermissionGranted(grantResults)){
            requresCheck  =true;
            allPermissionGranted();

        }else {
            requresCheck     = false;
            showMissingPermissionDialog();
        }


    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder al =  new AlertDialog.Builder(this);
        al.setTitle("Diloag");
        al.setMessage("ayo ayo ayo");
        al.setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(DENIED);
                finish();
            }
        });
        al.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startAppSetting();
            }
        });
        al.show();
    }

    private void startAppSetting() {
        Intent ii = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        ii.setData(Uri.parse(PACK_URL_SCHEMA + getPackageName()));
        startActivity(ii);
    }

    private boolean hasAllPermissionGranted(int[] grantResults) {
        for (int grandResult : grantResults){
            if (grandResult == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return   true   ;


    }
}
