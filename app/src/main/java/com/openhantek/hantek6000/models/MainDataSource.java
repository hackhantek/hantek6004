package com.openhantek.hantek6000.models;

// This class can't depend on OS specific framework or class, but can depend on pure java class.

import com.hantek.ht6000api.HantekDeviceListener;

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
}
