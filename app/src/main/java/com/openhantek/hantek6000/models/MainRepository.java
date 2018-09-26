package com.openhantek.hantek6000.models;

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HantekSdk;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.AutomeasureType;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.hantek.ht6000api.ht6000.TriggerSweep;

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
    public void removeDeviceListener(HantekDeviceListener listener) {
        HantekSdk.removeDeviceListener(listener);
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

    @Override
    public void setCoupling(int chIndex, InputCoupling inputCoupling) {
        HantekSdk.setCoupling(chIndex, inputCoupling);
    }

    @Override
    public InputCoupling getCoupling(int chIndex) {
        return HantekSdk.getCoupling(chIndex);
    }

    @Override
    public AttenuationFactor getAttenuationFactor(int chIndex) {
        return HantekSdk.getAttenuationFactor(chIndex);
    }

    @Override
    public void setAttenuationFacotr(int chIndex, AttenuationFactor attenuationFactor) {
        HantekSdk.setAttenuationFacotr(chIndex, attenuationFactor);
    }

    @Override
    public void setTriggerSource(int source) {
        HantekSdk.setTriggerSource(source);
    }

    @Override
    public void setTriggerSlope(TriggerSlope slope) {
        HantekSdk.setTriggerSlope(slope);
    }

    @Override
    public void setTriggerSweep(TriggerSweep sweep) {
        HantekSdk.setTriggerSweep(sweep);
    }

    @Override
    public TriggerSweep getTriggerSweep() {
        return HantekSdk.getTriggerSweep();
    }

    @Override
    public int getTriggerSource() {
        return HantekSdk.getTriggerSource();
    }

    @Override
    public TriggerSlope getTriggerSlope() {
        return HantekSdk.getTriggerSlope();
    }

    @Override
    public void centerChannelLevel(int chIndex) {
        HantekSdk.centerChannelLevel(chIndex);
    }

    @Override
    public void centerTriggerLevel() {
        HantekSdk.centerTriggerLevel();
    }

    @Override
    public InputCoupling getChInputCoupling(int chIndex) {
        return HantekSdk.getCoupling(chIndex);
    }

    @Override
    public void increaseVoltsPerDiv() {
        HantekSdk.increaseVoltsPerDiv();
    }

    @Override
    public void decreaseVoltsPerDiv() {
        HantekSdk.decreaseVoltsPerDiv();
    }

    @Override
    public void increaseTimebase() {
        HantekSdk.increaseTimebase();
    }

    @Override
    public void decreaseTimebase() {
        HantekSdk.decreaseTimebase();
    }

    @Override
    public void setSelectedChannel(int chIndex) {
        HantekSdk.setSelectedChannel(chIndex);
    }

    @Override
    public boolean isLargestVoltsPerDiv() {
        return HantekSdk.isLargestVoltsPerDiv();
    }

    @Override
    public boolean isSmallestVoltsPerDiv() {
        return HantekSdk.isSmallestVoltsPerDiv();
    }

    @Override
    public boolean isLargestTimebase() {
        return HantekSdk.isLargestTimebase();
    }

    @Override
    public boolean isSmallestTimebase() {
        return HantekSdk.isSmallestTimebase();
    }

    @Override
    public void clearChannels() {
        HantekSdk.clearSamples();
    }

    @Override
    public void addAutoMeasure(AutomeasureType type) {
        HantekSdk.addAutoMeasure(type);
    }

    @Override
    public void setAutoMeasureSource(int chIndex) {
        HantekSdk.setAutoMeasureSource(chIndex);
    }

    @Override
    public int getAutoMeasureSource() {
        return HantekSdk.getAutoMeasureSource();
    }

    @Override
    public void resetToFactory() {
        HantekSdk.resetToFactory();
    }

    @Override
    public boolean isInRealDeviceMode() {
        return HantekSdk.isInRealMode();
    }

    @Override
    public void startSelfCalibration() {
        HantekSdk.startSelfCalibration();
    }
}
