package com.openhantek.hantek6000.models;

import com.hantek.ht6000api.HantekDeviceListener;

// To let presenter class independent from Android API by this interface
public interface HtUsbManagerInterface {

    void loadDemoDevice(HantekDeviceListener mHtDeviceListener, int[] colors, Object scopeView);

    void loadRealDevice(HantekDeviceListener mHtDeviceListener, int[] colors, Object scopeView);

    boolean setDevice();

    boolean isScopeDeviceExist(int device_filter);

    boolean hasUsbPermission();

    Object getUsbDevice();

    void releaseDevice();
}
