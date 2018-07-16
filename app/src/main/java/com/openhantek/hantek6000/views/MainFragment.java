package com.openhantek.hantek6000.views;

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

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.models.HtUsbManager;
import com.openhantek.hantek6000.presenters.MainContract;
import com.openhantek.hantek6000.presenters.MainPresenter;

import com.hantek.ht6000api.HtScopeView;

/**
 * Include ScopeView and zero level markers.
 */
public class MainFragment extends Fragment implements MainContract.View{

    private final static String TAG = "MainFragment";
    private MainPresenter mPresenter;
    // Request USB permission ID
    private static final String ACTION_USB_PERMISSION = "com.openhantek.ht6000.USB_PERMISSION";
    private HtScopeView mScopeView;
    private AlertDialog mAskDemoDialog; // Ask whether to load the demo device dialog.
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_frag, container, false);

        mScopeView = root.findViewById(R.id.scopeView);

        mPresenter = new MainPresenter(this, new HtUsbManager(mContext));
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
        if (mUsbReceiver != null) {
            mContext.unregisterReceiver(mUsbReceiver);
        }
        super.onDestroy();
    }

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
    public void updateScopeView() {
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mScopeView.update();
                mScopeView.invalidate();
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
                .setPositiveButton(R.string.alert_dialog_yes, demoDialogClickListener)
                .setNegativeButton(R.string.alert_dialog_no, demoDialogClickListener)
                //.setIcon(R.drawable.ic_dialog_alert)
                .create();
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
                case DialogInterface.BUTTON_NEGATIVE:   // No button clicked, don't load
                    mPresenter.handleNotLoadDemoDevice();
                    break;
            }
        }
    };

    // listen USB notification
    private void setupUsbReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mUsbReceiver, filter);
    }

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
                            getResources().getString(R.string.usb_permmsion_not_granted),
                            Toast.LENGTH_SHORT).show();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.d(TAG, "USB device pulled out");
                //UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                //onUsbDetached(device);
            }
        }
    };
}
