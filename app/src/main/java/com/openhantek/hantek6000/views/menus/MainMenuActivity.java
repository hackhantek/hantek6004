package com.openhantek.hantek6000.views.menus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.openhantek.hantek6000.R;

// To let the menu not full screen, you must set the activity style to "Theme.AppCompat.Dialog"
// in AndroidManifest.xml

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null) // support press system back button to return last menu
                    .replace(R.id.menu_fragment_container, MainMenuFragment.newInstance())
                    .commit();
        }
    }

    // Set the menu position to the bottom of trigger button, the default position is in the center of screen.
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        final View view = getWindow().getDecorView();
        final WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.TOP | Gravity.END;
        lp.y = 40; // y position
        getWindowManager().updateViewLayout(view, lp);
    }
}
