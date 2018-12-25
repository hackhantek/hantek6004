package com.openhantek.hantek6000.views;

import android.support.v7.app.AppCompatActivity;

// To be able to mock permission access/requests
public interface PermissionHandler {
    // Code for write external storage.
    int REQUEST_CODE_EXTERNAL_STORAGE = 100;

    /**
     * Check whether "WRITE_EXTERNAL_STORAGE" is granted.
     * @param activity The target activity.
     * @param permission The name of the permission being checked.
     * @return true: granted; false: not granted
     */
    boolean checkHasPermissionWriteExternalStorage(AppCompatActivity activity, String permission);

    /**
     * Requests permissions to be granted to this application.
     * @param activity The target activity.
     * @param permissions The requested permissions. Must me non-null and not empty.
     * @param requestCode requestCode Application specific request code to match with a result
     *    Should be >= 0.
     */
    void requestPermissionWriteExternalStorage(AppCompatActivity activity, String[] permissions, int requestCode);
}
