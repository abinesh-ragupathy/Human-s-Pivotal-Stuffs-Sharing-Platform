package com.food.nofoodwaste;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class PermissionHandler {


    private BroadcastReceiver permissionReceiver;
    private OnPermissionResponse listener;
    private Activity activity;

    public void checkPermission(Activity _activity, String[] permission, OnPermissionResponse _listener) {
        listener = _listener;
        activity = _activity;

        if (Build.VERSION.SDK_INT >= 23) {
            for(int i = 0;i<permission.length;i++) {
                if (activity.checkSelfPermission(permission[i]) == PackageManager.PERMISSION_GRANTED) {
                    if(i == permission.length-1) {
                        listener.onPermissionGranted();
                        Log.v("TAG", "Permission is granted");
                    }
                } else {
                    i = permission.length;
                    registerReceiver();
                    ActivityCompat.requestPermissions(activity, permission, 1);
                    Log.v("TAG", "Request permission");
                }
            }

        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            listener.onPermissionGranted();
        }
    }

    /**
     *  One Permission
     */
    public void checkPermission(Activity _activity, String permission, OnPermissionResponse _listener) {
        listener = _listener;
        activity = _activity;

        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                listener.onPermissionGranted();
            } else {
                registerReceiver();
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
            }

        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            listener.onPermissionGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            // You can send your permission list or granted, but we already know what permission we need
            listener.onPermissionGranted();
        }else {
            listener.onPermissionDenied();
        }
    }

    public interface OnPermissionResponse{
        void onPermissionGranted();
        void onPermissionDenied();
    }

    private void registerReceiver() {
        permissionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null)
                {
                    int requestCode = extras.getInt("requestCode");
                    String[] permissions = intent.getStringArrayExtra("permissions");
                    int[] grantResults = intent.getIntArrayExtra("grantResults");
                    onRequestPermissionsResult(requestCode,permissions,grantResults);
                    activity.unregisterReceiver(permissionReceiver);
                }
            }
        };

        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("PERMISSION_RECEIVER");
        activity.registerReceiver(permissionReceiver, localIntentFilter);
    }
}


