package com.openhantek.hantek6000.models;

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HantekSdk;

public class MainRepository implements MainDataSource{

    private static MainRepository mInstance = new MainRepository();

    public static MainRepository getInstance() {
        return mInstance;
    }

    @Override
    public int getAnalogChannelCount() {
        return HantekSdk.getDevice().getChannels().length;
    }

    @Override
    public boolean isChannelEnabled(int i) {
        return HantekSdk.getChannelStatus()[i];
    }

    @Override
    public void addDeviceListener(HantekDeviceListener listener) {
        HantekSdk.addDeviceListener(listener);
    }

    @Override
    public String getVoltsDivString(int chIndex) {
        return HantekSdk.getVoltsDivString(chIndex);
    }

    @Override
    public String getTimeBaseString() {
        return HantekSdk.getTimeBaseString();
    }

    @Override
    public boolean isRunning() {
        return HantekSdk.isScopeRunning();
    }

    @Override
    public boolean isTriggered() {
        return HantekSdk.isTriggered();
    }

    @Override
    public void switchChannelEnabled(int i) {
        HantekSdk.switchChannelEnabled(i);
    }
}
