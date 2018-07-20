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
        return HantekSdk.getAnlogChannelsCount();
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

    @Override
    public void switchRunStop() {
        HantekSdk.switchRunStop();
    }

    @Override
    public void startAutoset() {
        HantekSdk.startAutoset();
    }

    @Override
    public boolean isDisconnected() {
        return HantekSdk.isDisConnected();
    }

    @Override
    public boolean isSelfCalibrating() {
        return HantekSdk.isSelfCalibrating();
    }

    @Override
    public boolean isInRealMode() {
        return HantekSdk.isInRealMode();
    }

    @Override
    public int getChannelPos(int chIndex) {
        if (HantekSdk.getDevice() == null) return -1;
        return HantekSdk.getDevice().getChannels()[chIndex].getLevel();
    }

    @Override
    public void setChannelPos(int i, int position) {
        HantekSdk.setChannelPos(i, position);
    }

    @Override
    public int getTriggerLevelPos() {
        return HantekSdk.getTriggerLevel();
    }

    @Override
    public void setTriggerLevelPos(int position) {
        HantekSdk.setTriggerLevel(position);
    }
}
