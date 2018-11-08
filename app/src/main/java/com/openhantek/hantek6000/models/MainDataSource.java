package com.openhantek.hantek6000.models;

// This class can't depend on OS specific framework or class, but can depend on pure java class.

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.AutomeasureType;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.hantek.ht6000api.ht6000.TriggerSweep;

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
     * Remove a {@link HantekDeviceListener}.
     * @param listener a listener
     */
    void removeDeviceListener(HantekDeviceListener listener);

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
     * Get current trigger sweep.
     * @return current trigger sweep
     */
    TriggerSweep getTriggerSweep();

    /**
     * Set trigger sweep.
     * @param sweep new trigger sweep
     */
    void setTriggerSweep(TriggerSweep sweep);

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

    /**
     * Increase timebase to next bigger.
     */
    void increaseTimebase();

    /**
     * Decrease timebase to previous smaller.
     */
    void decreaseTimebase();

    /**
     * Change selected channel.
     * <p>When adjusting the volts by the pinch gesture, the volts of
     * the selected channel is adjusted.</p>
     * @param chIndex new seleted channel index.
     */
    void setSelectedChannel(int chIndex);

    boolean isLargestVoltsPerDiv();

    boolean isSmallestVoltsPerDiv();

    boolean isLargestTimebase();

    boolean isSmallestTimebase();

    /**
     * Clear all analog channels data.
     */
    void clearChannels();

    /**
     * Add an auto measure item.
     * @param type auto measure type.
     */
    void addAutoMeasure(AutomeasureType type);

    /**
     * Set auto measure source channel.
     * @param chIndex channel index. 0:CH1...
     */
    void setAutoMeasureSource(int chIndex);

    /**
     * Get auto measure source
     * @return source channel index. 0:CH1...
     */
    int getAutoMeasureSource();

    /**
     * Reset to factory setup.
     */
    void resetToFactory();

    /**
     * Is real device connected.
     * @return true: connected. false: no connected
     */
    boolean isInRealDeviceMode();

    /**
     * Start self calibration.
     */
    void startSelfCalibration();

    /**
     * Is cursor measure enabled.
     * @return Is cursor measure enabled.
     */
    boolean isCursorMeasureEnabled();

    /**
     * 保存文件。Save channel data.
     * @param chIndex The channel index to save. 0:CH1...3:CH4
     * @return FAIL: null; SUCCESS: The absolute file path of the saved file.
     */
    String saveData(int chIndex);

    /**
     * Get the file names which extension is {@code ext} in APP directory.
     * @param ext file extension
     * @return all file names
     */
    String[] getFiles(String ext);

    /**
     * Load data from file.
     * @param fileName file name
     */
    void loadData(String fileName);

    /**
     * Get APP version.
     * @return APP version. null if failed to get.
     */
    String getAppVersion();

    /**
     * GET FPGA firmware version.
     * @return null if failed to get.
     */
    String getFpgaVersion();

    /**
     * Get scope driver version.
     * @return null if failed to get.
     */
    String getDriverVersion();

    /**
     * Get product serial number.
     * @return Fail: null; Success: product sn.
     */
    String getProductSn();
}
