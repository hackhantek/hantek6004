package com.openhantek.hantek6000.models;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HantekSdk;
import com.hantek.ht6000api.HtScopeSettings;
import com.hantek.ht6000api.ht6000.ConnectionStatus;
import com.openhantek.hantek6000.Utils.UsbDeviceFilter;

public class HtUsbManager implements HtUsbManagerInterface {

    private final static String TAG = "HtUsbManager";
    private final Context context;

    private UsbManager mUsbManager;
    private UsbDevice mUsbDevice;
    private UsbDeviceConnection mUsbConnection;
    private UsbInterface mInterface;

    // Resource file that specifies the USB devices that we're interested in.
    private int mUsbFilterId;

    /**
     * Constructor.
     * @param context context
     * @param device_filter Resource file that specifies the USB devices that we're interested in.
     */
    public HtUsbManager(Context context, int device_filter) {
        this.context = context;
        // TODO: Need to re-acquire after insertion
        mUsbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE);

        mUsbFilterId = device_filter;
    }

    @Override
    public void loadDemoDevice(HantekDeviceListener mHtDeviceListener, HtScopeSettings scopeSettings) {
        HantekSdk.loadDemoDevice(mHtDeviceListener, scopeSettings);
    }

    @Override
    public void loadRealDevice(HantekDeviceListener mHtDeviceListener, HtScopeSettings scopeSettings) {
        HantekSdk.loadRealDevice(mHtDeviceListener, mUsbConnection, mInterface, scopeSettings);
        HantekSdk.setConnectionStatus(ConnectionStatus.Connected);
    }

    @Override
    public boolean setDevice() {
        if (mUsbConnection != null) {
            if (mInterface != null) {
                mUsbConnection.releaseInterface(mInterface);
                mInterface = null;
            }
            mUsbConnection.close();
            //mUsbDevice = null;
            mUsbConnection = null;
        }

        if (mUsbDevice != null) {
            if (mUsbDevice.getInterfaceCount() != 1) {
                Log.e(TAG, "could not find interface");
                return false;
            }

            UsbInterface intf = mUsbDevice.getInterface(0);
            // device should have two endpoint
            if (intf.getEndpointCount() != 2) {
                Log.e(TAG, "could not find endpoint");
                return false;
            }

            UsbDeviceConnection connection = mUsbManager.openDevice(mUsbDevice);
            if (connection != null ) {
                Log.d(TAG, "open SUCCESS");
                if (connection.claimInterface(intf, false)) {
                    Log.d(TAG, "claim interface SUCCESS");
                    //mUsbDevice = usbDevice;
                    mInterface = intf;
                    mUsbConnection = connection;
                    return true;
                } else {
                    Log.d(TAG, "claim interface FAIL");
                    connection.close();
                }

            } else {
                Log.d(TAG, "open FAIL");
                mUsbConnection = null;
            }
        }

        return false;
    }

    @Override
    public void releaseDevice() {

        if (mUsbConnection != null) {
            if (mInterface != null) {
                mUsbConnection.releaseInterface(mInterface);
                mInterface = null;
            }
            mUsbConnection.close();
            mUsbDevice = null;
            mUsbConnection = null;
        }

        if (HantekSdk.getDevice() != null) {
            HantekSdk.getDevice().close();
        }
    }

    @Override
    public boolean isScopeDeviceExist() {
        for (UsbDevice device: mUsbManager.getDeviceList().values()) {
            if (isHantekDevice(device, context, mUsbFilterId)) {
                mUsbDevice = device;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasUsbPermission() {
        return mUsbManager.hasPermission(mUsbDevice);
    }

    @Override
    public Object getUsbDevice() {
        return mUsbDevice;
    }

    // Check whether the USB device is a Hantek device
    private boolean isHantekDevice(UsbDevice device, Context ctx, int resourceId) {
        try {
            return UsbDeviceFilter.isDeviceMatch(ctx, resourceId, device);
        } catch (Exception e) {
            Log.w(TAG, "Failed to parse: " + e.getMessage());
            return false;
        }
    }
    //endregion USB
}
