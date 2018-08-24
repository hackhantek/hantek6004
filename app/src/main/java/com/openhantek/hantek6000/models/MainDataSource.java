package com.openhantek.hantek6000.models;

// This class can't depend on OS specific framework or class, but can depend on pure java class.

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;

public interface MainDataSource {
    /**
     * Get analog channels count.
     * <p>In hantek6074 this count is 4.</p>
     * @return anlog channels count.
     */
    int getAnalogChannelCount();

    /**
     * Is channel enabled
     * @param i channel index. 0: CH1; 1: CH2; 2:CH3; 3:CH4.
     * @return is channel enabled.
     */
    boolean isChannelEnabled(int i);

    /**
     * Add a Hantek device message listener.
     * @param listener listener
     */
    void addDeviceListener(HantekDeviceListener listener);

    /**
     * Get channel volts per DIV.
     * @param chIndex channel index. 0:CH1
     * @return channel volts per DIV.
     */
    String getVoltsDivString(int chIndex);

    /**
     * Get current time base in string
     * @return time base
     */
    String getTimeBaseString();

    /**
     * Is device running.
     * @return true: run; false: stop.
     */
    boolean isRunning();

    /**
     * Is triggered. No meaning in roll mode.
     * @return true: triggered; false: triggered.
     */
    boolean isTriggered();

    /**
     * Switch channel enabled status
     * @param i channel index. 0: CH1
     */
    void switchChannelEnabled(int i);

    /**
     * Switch scope RUN&STOP status.
     */
    void switchRunStop();

    /**
     * Start autoset.
     */
    void startAutoset();

    /**
     * Is in Disconnected status.
     * @return true: in disconnected status.
     */
    boolean isDisconnected();

    /**
     * Is self calibrating.
     * @return true: self calibrating.
     */
    boolean isSelfCalibrating();

    /**
     * Is in real mode.
     * @return true: in demo mode
     */
    boolean isInRealMode();

    /**
     * Get channel zero level position
     * @param chIndex channel index. 0:CH1...
     * @return current zero level position.
     */
    int getChannelPos(int chIndex);

    /**
     * Set channel zero level position.
     * @param i channel index. 0: CH1...
     * @param position new position.
     */
    void setChannelPos(int i, int position);

    /**
     * Get trigger level.
     * @return trigger level.
     */
    int getTriggerLevelPos();

    /**
     * Set trigger level position.
     * @param position trigger level position.
     */
    void setTriggerLevelPos(int position);

    /**
     * Set channel input coupling.
     * @param chIndex channel index.
     * @param inputCoupling input coupling
     */
    void setCoupling(int chIndex, InputCoupling inputCoupling);

    /**
     * Get channel input coupling.
     * @param chIndex channel index
     * @return channel input coupling
     */
    InputCoupling getCoupling(int chIndex);

    /**
     * Get channel attenuation factor.
     * @param chIndex channel index.
     * @return attenuation factor.
     */
    AttenuationFactor getAttenuationFactor(int chIndex);

    /**
     * Set attenuation factor.
     * @param chIndex channel index.
     * @param attenuationFactor attenuation factor.
     */
    void setAttenuationFacotr(int chIndex, AttenuationFactor attenuationFactor);

    /**
     * Set trigger source.
     * @param source new trigger source
     */
    void setTriggerSource(int source);

    /**
     * Set trigger slope.
     * @param slope new trigger slope.
     */
    void setTriggerSlope(TriggerSlope slope);

    /**
     * Get trigger source
     * @return trigger source. 0:CH1...
     */
    int getTriggerSource();

    /**
     * Get trigger slope.
     * @return current trigger slope.
     */
    TriggerSlope getTriggerSlope();

    /**
     * Put channel zero level in center.
     * @param chIndex channel index. 0:CH1...
     */
    void centerChannelLevel(int chIndex);

    /**
     * Put trigger level in center of trigger source channel waveform.
     */
    void centerTriggerLevel();

    /**
     * Get channel input coupling
     * @param chIndex 0:CH1...3：CH4
     * @return channel input cooupling
     */
    InputCoupling getChInputCoupling(int chIndex);

    /**
     * Set voltage per DIV of channel of chIndex to next bigger.
     */
    void increaseVoltsPerDiv();

    /**
     * Set voltage per DIV of channel of chIndex to prvious smaller.
     */
    void decreaseVoltsPerDiv();
}
