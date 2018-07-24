package com.openhantek.hantek6000.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.openhantek.hantek6000.R;

// Responsible for the trigger quick set popup
public class TriggerQuickSettingsPop {
    private final int mSource; // current trigger source
    private final TriggerSlope mSlope; // current trigger slope.
    private PopupWindow mPopupWindow; // the PopupWindow
    // the trigger marker view
    private View mTriggerView;
    private Context mContext;
    private LinearLayout mSourceLayout;
    private LinearLayout mSlopeLayout;
    // source button in first level.
    private Button mSourceButton;
    // slope button in first level.
    private Button mSlopeButton;
    // message listener
    private TriggerQuickSettingsPopListener mListener;

    /**
     * Constructor.
     * @param view the clicked view.
     * @param context context
     * @param triggerSource current tirgger source
     * @param triggerSlope current tirgger slope
     */
    TriggerQuickSettingsPop(View view, Context context, int triggerSource, TriggerSlope triggerSlope) {
        mTriggerView = view;
        mContext = context;
        mSource = triggerSource;
        mSlope = triggerSlope;
    }

    /**
     * Set message listener.
     * @param listener message listener.
     */
    void setListener(TriggerQuickSettingsPopListener listener) {
        mListener = listener;
    }

    public void show() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) return;

        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(mContext).inflate(
                R.layout.trigger_quick_settings, null);

        PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // Closes the popup window when touch outside.
        // This method was written informatively in Google's docs.
        popupWindow.setOutsideTouchable(true);

        // Set focus true to make prevent touch event to below view (main layout),
        // which works like a dialog with 'cancel' property => Try it! And you will know what I mean.
        popupWindow.setFocusable(true);

        // mMainPop default background. Without this touch outside don't dismiss popup window.
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       // measure contentView size
        View contentView = popupWindow.getContentView();
        // need to measure first, because in this time PopupWindow is nit pop, width is 0.
        contentView.measure(makeDropDownMeasureSpec(popupWindow.getWidth()),
                makeDropDownMeasureSpec(popupWindow.getHeight()));

        int offsetX = -popupWindow.getContentView().getMeasuredWidth();
        int offsetY = -mTriggerView.getHeight();
        // show at the left edge of the trigger view
        popupWindow.showAsDropDown(mTriggerView, offsetX, offsetY);

        setupUi(contentView);
    }

    // setup UI element
    private void setupUi(View mainView) {
        mSourceLayout = mainView.findViewById(R.id.trigger_quick_settings_source_layout);
        mSlopeLayout = mainView.findViewById(R.id.trigger_quick_settings_slope_layout);

        // source button click event
        mSourceButton = mainView.findViewById(R.id.trigger_quick_settings_source_button);
        mSourceButton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toggleTriggerSettingsVisibility();
               closeSettingsExceptSource();
            }
        });

        // slope button click event
        mSlopeButton = mainView.findViewById(R.id.trigger_quick_settings_slope_button);
        mSlopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlopeSettingsVisibility();
                closeSettingsExceptSlope();
            }
        });

        // setup source button listener
        setupSourceButtonsListener();
        // setup slope button listener
        setupSlopeButtonsListener();

        // setup initial state
        setupInitialState();
    }

    // setup initial state
    private void setupInitialState() {
        switch (mSource) {
            case 0:
                mSourceButton.setText(mContext.getResources().getString(R.string.ch1));
                break;
            case 1:
                mSourceButton.setText(mContext.getResources().getString(R.string.ch2));
                break;
            case 2:
                mSourceButton.setText(mContext.getResources().getString(R.string.ch3));
                break;
            case 3:
                mSourceButton.setText(mContext.getResources().getString(R.string.ch4));
                break;
        }

        switch (mSlope) {
            case Rising:
                mSlopeButton.setText(mContext.getResources().getString(R.string.trigger_slope_rising));
                break;
            case Falling:
                mSlopeButton.setText(mContext.getResources().getString(R.string.trigger_slope_falling));
                break;
        }
    }

    // toggle trigger settings visibility
    private void toggleTriggerSettingsVisibility() {

        if (mSourceLayout.getVisibility() == View.GONE) {
            mSourceLayout.setVisibility(View.VISIBLE);
        } else {
            mSourceLayout.setVisibility(View.GONE);
        }
    }

    // toggle slope settings visibility
    private void toggleSlopeSettingsVisibility() {
        if (mSlopeLayout.getVisibility() == View.GONE) {
            mSlopeLayout.setVisibility(View.VISIBLE);
        } else {
            mSlopeLayout.setVisibility(View.GONE);
        }
    }

    // close settings except trigger source
    private void closeSettingsExceptSource() {
        mSlopeLayout.setVisibility(View.GONE);
    }

    // close settings except slope source
    private void closeSettingsExceptSlope() {
        mSourceLayout.setVisibility(View.GONE);
    }

    // setup source buttons listener
    private void setupSourceButtonsListener() {
        Button button;
        button = mSourceLayout.findViewById(R.id.trigger_quick_settings_source_ch1);
        button.setOnClickListener(mSourceButtonsListener);
        button = mSourceLayout.findViewById(R.id.trigger_quick_settings_source_ch2);
        button.setOnClickListener(mSourceButtonsListener);
        button = mSourceLayout.findViewById(R.id.trigger_quick_settings_source_ch3);
        button.setOnClickListener(mSourceButtonsListener);
        button = mSourceLayout.findViewById(R.id.trigger_quick_settings_source_ch4);
        button.setOnClickListener(mSourceButtonsListener);
    }

    // setup slope buttons listener
    private void setupSlopeButtonsListener() {
        Button button;
        button = mSlopeLayout.findViewById(R.id.trigger_quick_settings_slope_rising);
        button.setOnClickListener(mSlopeButtonsListener);
        button = mSlopeLayout.findViewById(R.id.trigger_quick_settings_slope_falling);
        button.setOnClickListener(mSlopeButtonsListener);
    }

    // source button click event handler.
    private View.OnClickListener mSourceButtonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSourceLayout.setVisibility(View.GONE);
            mSourceButton.setText(((Button)v).getText());

            // get trigger source
            int source = -1;
            switch (v.getId()) {
                case R.id.trigger_quick_settings_source_ch1:
                    source = 0;
                    break;
                case R.id.trigger_quick_settings_source_ch2:
                    source = 1;
                    break;
                case R.id.trigger_quick_settings_source_ch3:
                    source = 2;
                    break;
                case R.id.trigger_quick_settings_source_ch4:
                    source = 3;
                    break;
            }

            if (source != -1 && mListener != null) {
                mListener.onSourceChanged(source);
            }
        }
    };

    // slope buttons click event handler.
    private View.OnClickListener mSlopeButtonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSlopeLayout.setVisibility(View.GONE);
            mSlopeButton.setText(((Button)v).getText());

            // get Trigger slope
            TriggerSlope slope = null;
            switch (v.getId()) {
                case R.id.trigger_quick_settings_slope_rising:
                    slope = TriggerSlope.Rising;
                    break;
                case R.id.trigger_quick_settings_slope_falling:
                    slope = TriggerSlope.Falling;
                    break;
            }

            if (slope != null && mListener != null) {
                mListener.onSlopeChanged(slope);
            }
        }
    };

    // Before popup , need to get PopupWindow size.
    // because contentView is not drawn in this time, width/height is 0.
    // so, need to by measure to get contentView's size.
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    interface TriggerQuickSettingsPopListener{
        void onSourceChanged(int source);
        void onSlopeChanged(TriggerSlope slope);
    }
}
