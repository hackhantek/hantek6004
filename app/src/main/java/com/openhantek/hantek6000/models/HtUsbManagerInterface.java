package com.openhantek.hantek6000.models;

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HtScopeSettings;

// To let presenter class independent from Android API by this interface
public interface HtUsbManagerInterface {

    void loadDemoDevice(HantekDeviceListener mHtDeviceListener, HtScopeSettings scopeSettings);

    void loadRealDevice(HantekDeviceListener mHtDeviceListener, HtScopeSettings scopeSettings);

    boolean setDevice();

    boolean isScopeDeviceExist();

    boolean hasUsbPermission();

    Object getUsbDevice();

    /**
     * Releases exclusive access to USB, close usb send and receive thread.
     */
    void releaseDevice();
}
