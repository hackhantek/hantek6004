package com.openhantek.hantek6000.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hantek.ht6000api.HantekSdk;
import com.openhantek.hantek6000.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // 隐藏 Action Bar.通过代码隐藏，不改主题。在 AndroidManifest.xml 中隐藏会更改主题
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        HantekSdk.sdkInitialize(this);
    }

    // After declared USB filter in resource file,
    // this method will be called if Hantek scope device plugged in USB port.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Pass intent to MainFragment
        MainFragment mainFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        if (mainFragment != null) {
            mainFragment.onUsbIntent(intent);
        }
    }
}
