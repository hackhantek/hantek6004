package com.openhantek.hantek6000.views;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * The concrete class of {@link PermissionHandler} interface.
 */
public class PermissionHandlerAndroid implements PermissionHandler {
    @Override
    public boolean checkHasPermissionWriteExternalStorage(AppCompatActivity activity, String permission) {
        if (activity == null) return false;
        if (permission == null) return false;

        // No need to request permission bellow android 6.0(API 23).
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return true; }

        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermissionWriteExternalStorage(AppCompatActivity activity, String[] permissions, int requestCode) {
        if (activity == null) return;
        if (permissions == null) return;

        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
