package com.openhantek.hantek6000.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hantek.ht6000api.HtMarkerView;
import com.hantek.ht6000api.HtMarkerViewListener;
import com.hantek.ht6000api.HtScopeViewListener;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.models.HtUsbManager;
import com.openhantek.hantek6000.presenters.MainPresenter;

import com.hantek.ht6000api.HtScopeView;

/**
 * Include ScopeView and zero level markers.
 */
public class MainFragment extends Fragment implements MainPresenter.View,
        HtScopeViewListener{

    private final static String TAG = "MainFragment";
    private MainPresenter mPresenter;
    // Request USB permission ID
    private static final String ACTION_USB_PERMISSION = "com.openhantek.ht6000.USB_PERMISSION";
    private HtScopeView mScopeView;
    // Ask whether to load the demo device dialog.
    private AlertDialog mAskDemoDialog;
    private Context mContext;
    // channel zero markers
    private HtMarkerView[] mChLevers;
    // Trigger Level Marker
    private HtMarkerView mTriggerLevelMarker;
    private ChQuickSettingsPop mChQuickSettingsPop; // channel quick settings popup window handler.
    private TriggerQuickSettingsPop mTriggerSettingsPop; // trigger quick setttings popup window handler.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag, container, false);

        mPresenter = new MainPresenter(this, new HtUsbManager(mContext));

        setupUiElements(root);

        mPresenter.checkDeviceExist(R.xml.device_filter);
        setupUsbReceiver();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroy() {
        System.out.println("MainFragment:" + "onDestroy");
        if (mUsbReceiver != null) {
            mContext.unregisterReceiver(mUsbReceiver);
        }
        mPresenter.releaseDevice();
        super.onDestroy();

        // Solve: press the Back button to exit APP, enter again, can not see the waveform
        // mScopeView don't call onDraw
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    //region Helper methods
    // setup UI elements
    @SuppressLint("ClickableViewAccessibility")
    private void setupUiElements(View root) {
        mChLevers = new HtMarkerView[mPresenter.getAnalogChannelCount()];

        mChLevers[0] = root.findViewById(R.id.ch1LevelMarker);
        mChLevers[1] = root.findViewById(R.id.ch2LevelMarker);
        mChLevers[2] = root.findViewById(R.id.ch3LevelMarker);
        mChLevers[3] = root.findViewById(R.id.ch4LevelMarker);

        mTriggerLevelMarker = root.findViewById(R.id.triggerLevelMarker);

        mScopeView = root.findViewById(R.id.scopeView);
        // Reset the range of levers after scope view position changed.
        mScopeView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateLeversRange();
            }
        });
        mScopeView.setListener(this);

        // Set channel zero level marker event listener
        for (HtMarkerView marker: mChLevers) {
            marker.setListener(markerViewListener);
        }
        mTriggerLevelMarker.setListener(markerViewListener);
    }

    // Channel quick settings popup window message listener.
    private ChQuickSettingsPop.ChQuickSettingsListener mChQuickSettingsListener =
            new ChQuickSettingsPop.ChQuickSettingsListener() {
                @Override
                public void onCouplingChanged(int chIndex, InputCoupling inputCoupling) {
                    mPresenter.setCoupling(chIndex, inputCoupling);
                }

                @Override
                public void onAttenuationFactor(int chIndex, AttenuationFactor attenuationFactor) {
                    mPresenter.setAttenuationFactor(chIndex, attenuationFactor);
                }
            };

    // Trigger quick settings PopupWindow message listener.
    private TriggerQuickSettingsPop.TriggerQuickSettingsPopListener mTriggerSettingsPopListener =
            new TriggerQuickSettingsPop.TriggerQuickSettingsPopListener() {
                @Override
                public void onSourceChanged(int source) {
                    mPresenter.setTriggerSource(source);
                }

                @Override
                public void onSlopeChanged(TriggerSlope slope) {
                    mPresenter.setTriggerSlope(slope);
                }
            };

    // marker event listener.
    private HtMarkerViewListener markerViewListener = new HtMarkerViewListener() {

        // single tap event
        @Override
        public void onMarkerSingleTapped(View view) {

            int chIndex = 0;
            boolean isChSettings = true;

            switch (view.getId()) {
                case R.id.ch1LevelMarker:
                    chIndex = 0;
                    break;
                case R.id.ch2LevelMarker:
                    chIndex = 1;
                    break;
                case R.id.ch3LevelMarker:
                    chIndex = 2;
                    break;
                case R.id.ch4LevelMarker:
                    chIndex = 3;
                    break;
                case R.id.triggerLevelMarker:
                    isChSettings = false;
                    break;
            }

            // popup window according to button clicked
            if (isChSettings) {
                if (mChQuickSettingsPop!=null && mChQuickSettingsPop.isShowing()) return;

                mChQuickSettingsPop = new ChQuickSettingsPop(view, mContext, chIndex,
                        mPresenter.getCoupling(chIndex), mPresenter.getAttenuationFactor(chIndex));
                mChQuickSettingsPop.show();
                mChQuickSettingsPop.setListener(mChQuickSettingsListener);
            } else {
                mTriggerSettingsPop = new TriggerQuickSettingsPop(view, mContext,
                        mPresenter.getTriggerSource(),
                        mPresenter.getTriggerSlope());
                mTriggerSettingsPop.show();
                mTriggerSettingsPop.setListener(mTriggerSettingsPopListener);
            }
        }

        // double tap event
        @Override
        public void onMarkerDoubleTapped(View view) {
            switch (view.getId()){
                case R.id.ch1LevelMarker:
                    mPresenter.centerChannelLevel(0);
                    break;
                case R.id.ch2LevelMarker:
                    mPresenter.centerChannelLevel(1);
                    break;
                case R.id.ch3LevelMarker:
                    mPresenter.centerChannelLevel(2);
                    break;
                case R.id.ch4LevelMarker:
                    mPresenter.centerChannelLevel(3);
                    break;
            }

            switch (view.getId()) {
                case R.id.triggerLevelMarker:
                    mPresenter.centerTriggerLevel();
                    break;
            }
        }

        // drag ended
        @Override
        public void onMarkerDragEnded(View view, int position) {
            System.out.println("MainFragment:" + "DragEnded event received." + position);
            switch (view.getId()) {
                case R.id.ch1LevelMarker:
                    mPresenter.handleChZeroMarkerDragEnded(0, position);
                    break;
                case R.id.ch2LevelMarker:
                    mPresenter.handleChZeroMarkerDragEnded(1, position);
                    break;
                case R.id.ch3LevelMarker:
                    mPresenter.handleChZeroMarkerDragEnded(2, position);
                    break;
                case R.id.ch4LevelMarker:
                    mPresenter.handleChZeroMarkerDragEnded(3, position);
                    break;
                case R.id.triggerLevelMarker:
                    mPresenter.changeTriggerLevelPos(position);
                    break;
            }
        }
    };

    // Set the range of the levers movement and converted range.
    private void updateLeversRange() {
        float minRange = mScopeView.getTop();
        float maxRange = mScopeView.getBottom();
        // the most big sample vale is on top of scope view.
        int minRangeValue = getResources().getInteger(R.integer.sample_max);
        // the converted vale when marker moved to bottom
        int maxRangeValue = getResources().getInteger(R.integer.sample_min);

        for (HtMarkerView marker: mChLevers) {
            marker.setRange(minRange, maxRange);
            marker.setConvertRange(minRangeValue, maxRangeValue);
        }

        mTriggerLevelMarker.setRange(minRange, maxRange);
        mTriggerLevelMarker.setConvertRange(minRangeValue, maxRangeValue);
    }
    //endregion

    //region MVP view methods

    @Override
    public int[] getChannelColors() {
        return new int[]{
                getResources().getColor(R.color.colorCha),
                getResources().getColor(R.color.colorChb),
                getResources().getColor(R.color.colorChc),
                getResources().getColor(R.color.colorChd)};
    }

    @Override
    public Object getScopeView() {
        return mScopeView;
    }

    @Override
    public void updateZeroLevelPos(final int i, final int zeroLevelPos) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChLevers[i].setPosition(zeroLevelPos);
            }
        });
    }

    @Override
    public void updateChZeroLevelMarkerVisibility(final boolean channelEnabled, final int i) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((mChLevers[i].getVisibility() != View.VISIBLE) && channelEnabled) {
                    mChLevers[i].setVisibility(View.VISIBLE);
                } else if (mChLevers[i].getVisibility() != View.INVISIBLE && !channelEnabled) {
                    mChLevers[i].setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void updateChCouplingIndicator(final int chIndex, final InputCoupling inputCoupling) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               mChLevers[chIndex].setInputCoupling(inputCoupling);
               mChLevers[chIndex].invalidate();
            }
        });
    }

    @Override
    public void promptLargestTimebase() {
        Toast.makeText(getContext(), getResources().getString(R.string.largest_timebase), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void promptSmallestTimebase() {
        Toast.makeText(getContext(), getResources().getString(R.string.smallest_timebase), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void promptLargestVoltsPerDiv() {
        Toast.makeText(getContext(), getResources().getString(R.string.largest_volts_div), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void promptSmallestVoltsPerDiv() {
        Toast.makeText(getContext(), getResources().getString(R.string.smallest_volts_div), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateTriggerLevelPos(final int triggerLevelPos) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTriggerLevelMarker.setPosition(triggerLevelPos);
            }
        });
    }

    @Override
    public void updateTriggerLevelVisibility(final boolean visible) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((mTriggerLevelMarker.getVisibility() != View.VISIBLE) && visible) {
                    mTriggerLevelMarker.setVisibility(View.VISIBLE);
                } else if (mTriggerLevelMarker.getVisibility() != View.INVISIBLE && !visible) {
                    mTriggerLevelMarker.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void updateTriggerLevelColor(int triggerSource) {
        if(getActivity() == null) return;

        int color = -1;
        switch (triggerSource){
            case 0:
                color = getResources().getColor(R.color.colorCha);
                break;
            case 1:
                color = getResources().getColor(R.color.colorChb);
                break;
            case 2:
                color = getResources().getColor(R.color.colorChc);
                break;
            case 3:
                color = getResources().getColor(R.color.colorChd);
                break;
        }

        if (-1 != color && getActivity() != null) {
            final int finalColor = color;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTriggerLevelMarker.setMarkerColor(finalColor);
                }
            });
        }
    }

    @Override
    public void updateScopeView() {
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mScopeView.update();
            }
        });
    }

    @Override
    public boolean isAskDemoDialogShowing() {
        return mAskDemoDialog != null && mAskDemoDialog.isShowing();
    }

    @Override
    public void closeAskDemoDialog() {
        if (mAskDemoDialog == null) return;
        mAskDemoDialog.dismiss();
    }

    @Override
    public void requestUsbPermission(Object device) {
        Context context = getContext();
        if (context == null) return;
        UsbManager usbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        if (usbManager == null) return;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission((UsbDevice) device, pendingIntent);
    }

    @Override
    public void askToLoadDemoDevice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        mAskDemoDialog = builder.setTitle(R.string.ask_demo_dialog_title)
                .setMessage(R.string.ask_demo_dialog_message)
                .setPositiveButton(R.string.alert_dialog_ok, demoDialogClickListener)
                .create();
        mAskDemoDialog.setCancelable(false);
        mAskDemoDialog.show();
    }

    // Whether to load demo device dialog callback.
    DialogInterface.OnClickListener demoDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:   // Yes button clicked, load
                    mPresenter.loadDemoDevice(getChannelColors(), mScopeView);
                    break;
            }
        }
    };
    //endregion

    /**
     * Listen USB notification.
     */
    private void setupUsbReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mUsbReceiver, filter);
    }

    /**
     * Handle USB permission notification.
     */
    BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // USB permission request reply
            if (ACTION_USB_PERMISSION.equals(action)) {
                boolean granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED,
                        false);
                if (granted) {
                    mPresenter.handleUsbPermissionGranted();
                } else {
                    Toast.makeText(context,
                            getResources().getString(R.string.usb_permission_not_granted),
                            Toast.LENGTH_SHORT).show();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.d(TAG, "USB device pulled out");
                //UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                //onUsbDetached(device);
            }
        }
    };

    //region Handle gesture

    // why put gesture handler in here?
    // because can pop-up custom prompts in different languages.

    @Override
    public void horizontalPinchDetected(boolean expand) {
        if (expand) {
            mPresenter.increaseTimebase();
        } else {
            mPresenter.decreaseTimebase();
        }
    }

    @Override
    public void selectedChannelChanged(int chIndex) {
        mPresenter.changeSelectedChannel(chIndex);
    }

    @Override
    public void verticalPinchDetected(boolean expand) {
        if (expand) {
            mPresenter.decreaseVoltsPerDiv();
        } else {
            mPresenter.increaseVoltsPerDiv();
        }
    }
    //endregion Handle gesture
}
