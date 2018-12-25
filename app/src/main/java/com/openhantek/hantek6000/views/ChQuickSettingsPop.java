package com.openhantek.hantek6000.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.openhantek.hantek6000.R;

// Responsible for pup-up channel quick settings UI.
// Consists of two levels of PopupWindow.
// MainPopup
// --CouplingPopup
// --ProbePopup
public class ChQuickSettingsPop {
    private Context mContext;
    private View mParentView; // The initial trigger view.
    private PopupWindow mMainPop; // Main PopupWindow
    private PopupWindow mCouplingPop; // Coupling settings PopupWindow
    private PopupWindow mProbePop; // Pobe settings PopupWindow
    private Button mCouplingButton; // Coupling button. It shows the current coupling settings.
    private Button mProbeButton; // Probe button.
    private int mChIndex; // channel index.
    private ChQuickSettingsListener mListener; // Message listener.
    private InputCoupling mInputCoupling; // current input coupling
    private AttenuationFactor mAttenuationFactor; // current attenuation factor.

    /**
     * Create a popup.
     * @param parentView The initial trigger view.
     * @param context context
     * @param chIndex channel index. 0:CH1...
     * @param inputCoupling current input coupling
     * @param attenuationFactor current attenuation factor
     */
    ChQuickSettingsPop(View parentView, Context context, int chIndex,
                              InputCoupling inputCoupling, AttenuationFactor attenuationFactor) {
        mParentView = parentView;
        mContext = context;
        mChIndex = chIndex;
        mInputCoupling = inputCoupling;
        mAttenuationFactor = attenuationFactor;
    }

    /**
     * Set message listner.
     * @param listener message listener.
     */
    void setListener(ChQuickSettingsListener listener) {
        mListener = listener;
    }

    // Show main Popup.
    public void show() {
        if (mMainPop != null && mMainPop.isShowing()) {
            return;
        }

        int[] location = new int[2];
        mParentView.getLocationOnScreen(location);
        Point p = new Point();
        p.x = location[0];
        p.y = location[1];

        // The main PopupWindow is on the right side of the trigger view.
        mMainPop = showPopup(null, R.layout.ch_quick_settings, p.x+mParentView.getWidth(), p.y);

        setMainPopButtonListeners(mMainPop.getContentView());

        syncView();
    }

    // Set view's initial state.
    private void syncView() {
        switch (mInputCoupling) {
            case AC:
                mCouplingButton.setText(R.string.coupling_ac);
                break;
            case DC:
                mCouplingButton.setText(R.string.coupling_dc);
                break;
            case GND:
                mCouplingButton.setText(R.string.coupling_gnd);
                break;
        }

        switch (mAttenuationFactor) {
            case X1:
                mProbeButton.setText(R.string.probe_x1);
                break;
            case X10:
                mProbeButton.setText(R.string.probe_x10);
                break;
            case X100:
                mProbeButton.setText(R.string.probe_x100);
                break;
            case X1000:
                mProbeButton.setText(R.string.probe_x1000);
                break;
            case X10000:
                mProbeButton.setText(R.string.probe_x10000);
                break;
            case X20:
                break;
            case X10A_CC65:
                mProbeButton.setText(R.string.probe_x20);
                break;
            case X100A_CC65:
                mProbeButton.setText(R.string.probe_x10a_cc65);
                break;
            case X100A_CC650:
                mProbeButton.setText(R.string.probe_x100a_cc65);
                break;
            case X1000A_CC650:
                mProbeButton.setText(R.string.probe_x100a_cc650);
                break;
            case X100A_CC1100:
                mProbeButton.setText(R.string.probe_x1000a_cc650);
                break;
            case X1000A_CC1100:
                mProbeButton.setText(R.string.probe_1000a_cc1100);
                break;
        }
    }

    // Show coupling settings.
    private void showCouplingPopup(View v) {
        if (mCouplingPop != null && mCouplingPop.isShowing()) {
            return;
        }

        // trigger view position
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        Point p = new Point();
        p.x = location[0];
        p.y = location[1];

        // TODO: 如果下面放不开，放上面
        // in the bottom of trigger view.
        mCouplingPop = showPopup(v, R.layout.ch_quick_settings_coupling, p.x,
                p.y + v.getHeight());

        setCouplingPopButtons(mCouplingPop.getContentView());
    }

    // Show probe settings
    private void showProbePopup(View v) {
        if (mProbePop != null && mProbePop.isShowing()) {
            return;
        }

        // trigger view position
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        Point p = new Point();
        p.x = location[0];
        p.y = location[1];

        // in the bottom of trigger view.
        mProbePop = showPopup(v, R.layout.ch_quick_settings_probe, p.x,
                p.y + v.getHeight());

        setProbePopButtons(mProbePop.getContentView());
    }

    /**
     * Show a PopupWindow
     * @param triggerView trigger view
     * @param layout_id layout id
     * @param x x position
     * @param y y position
     * @return the popupWindow
     */
    private PopupWindow showPopup(View triggerView, int layout_id, int x, int y) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(layout_id,
                null);

        PopupWindow popupWindow;
        // No trigger view, will let the PopupWindow wrap it's content.
        // With trigger view, will let the PopupWindow's width same as trigger view
        if (triggerView == null) {
            popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow = new PopupWindow(layout, triggerView.getWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);

        }

        // Closes the popup window when touch outside.
        // This method was written informatively in Google's docs.
        popupWindow.setOutsideTouchable(true);

        // Set focus true to make prevent touch event to below view (main layout),
        // which works like a dialog with 'cancel' property => Try it! And you will know what I mean.
        popupWindow.setFocusable(true);

        // mMainPop default background. Without this touch outside don't dismiss popup window.
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // show bellow the trigger view.
        // the parent view must be the same with the first level PopupWindow's parent view.
        popupWindow.showAtLocation(mParentView, Gravity.NO_GRAVITY, x, y);

        return popupWindow;
    }

    // Set first level button click event listener.
    private void setMainPopButtonListeners(View view) {
        mCouplingButton = view.findViewById(R.id.ch_settings_coupling_button);
        mCouplingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCouplingPopup(v);
            }
        });
        mProbeButton = view.findViewById(R.id.ch_settings_probe_button);
        mProbeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProbePopup(v);
            }
        });
    }


    // Set coupling PopupWindow button click event listener.
    private void setCouplingPopButtons(View parentView) {
        Button button;
        button = parentView.findViewById(R.id.coupling_layout_ac);
        setCouplingButtonListener(button);
        button = parentView.findViewById(R.id.coupling_layout_dc);
        setCouplingButtonListener(button);
        button = parentView.findViewById(R.id.coupling_layout_gnd);
        setCouplingButtonListener(button);
    }

    // Set probe PopupWindow button click event listener.
    private void setProbePopButtons(View parentView) {
        Button button;

        button = parentView.findViewById(R.id.probe_layout_x1);
        setProbeButtonListener(button);
        button = parentView.findViewById(R.id.probe_layout_x10);
        setProbeButtonListener(button);
        button = parentView.findViewById(R.id.probe_layout_x100);
        setProbeButtonListener(button);
        button = parentView.findViewById(R.id.probe_layout_x1000);
        setProbeButtonListener(button);
        button = parentView.findViewById(R.id.probe_layout_x10000);
        setProbeButtonListener(button);

    }

    // Set Coupling PopupWindow button listener
    private void setCouplingButtonListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCouplingButton.setText(((Button)v).getText());
                mCouplingPop.dismiss();

                // get coupling
                InputCoupling inputCoupling = null;
                switch (v.getId()) {
                    case R.id.coupling_layout_ac:
                        inputCoupling = InputCoupling.AC;
                        break;
                    case R.id.coupling_layout_dc:
                        inputCoupling = InputCoupling.DC;
                        break;
                    case R.id.coupling_layout_gnd:
                        inputCoupling = InputCoupling.GND;
                        break;
                }

                if (mListener != null && inputCoupling != null) {
                    mListener.onCouplingChanged(mChIndex, inputCoupling);
                }
            }
        });
    }

    // Set Probe PopupWindow button listener
    private void setProbeButtonListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProbeButton.setText(((Button)v).getText());
                mProbePop.dismiss();

                // get attenuation factor
                AttenuationFactor attenuationFactor = null;
                switch (v.getId()) {
                    case R.id.probe_layout_x1:
                        attenuationFactor = AttenuationFactor.X1;
                        break;
                    case R.id.probe_layout_x10:
                        attenuationFactor = AttenuationFactor.X10;
                        break;
                    case R.id.probe_layout_x100:
                        attenuationFactor = AttenuationFactor.X100;
                        break;
                    case R.id.probe_layout_x1000:
                        attenuationFactor = AttenuationFactor.X1000;
                        break;
                    case R.id.probe_layout_x10000:
                        attenuationFactor = AttenuationFactor.X10000;
                        break;
                }

                if (mListener != null && attenuationFactor != null) {
                    mListener.onAttenuationFactor(mChIndex, attenuationFactor);
                }
            }
        });
    }

    /**
     * Indicate whether this popup window is showing on screen.
     * @return true if the popup is showing, false otherwise
     */
    boolean isShowing() {
        return  (mMainPop != null ) && mMainPop.isShowing();
    }

    /**
     * Define the message this class will send out.
     */
    public interface ChQuickSettingsListener{
        /**
         * Called after coupling changed.
         * @param chIndex channel index.
         * @param inputCoupling input coupling
         */
        void onCouplingChanged(int chIndex, InputCoupling inputCoupling);

        void onAttenuationFactor(int chIndex, AttenuationFactor attenuationFactor);
    }
}
