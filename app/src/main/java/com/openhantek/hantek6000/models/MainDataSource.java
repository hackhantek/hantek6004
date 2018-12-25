package com.openhantek.hantek6000.models;

// This class can't depend on OS specific framework or class, but can depend on pure java class.

import com.hantek.ht6000api.Channel;
import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HtScopeSettings;
import com.hantek.ht6000api.MathChannel;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.AutomeasureType;
import com.hantek.ht6000api.ht6000.CaptureMode;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TimeBase;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.hantek.ht6000api.ht6000.TriggerSweep;
import com.hantek.ht6000api.ht6000.VoltsPerDivision;

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
    void setAttenuationFactor(int chIndex, AttenuationFactor attenuationFactor);

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
     * @return channel input coupling
     */
    InputCoupling getInputCoupling(int chIndex);

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
     * Load data from file(.csv).
     * @param fileName file name
     */
    void loadCsvData(String fileName);

    /**
     * Load data from file(.rfc).
     * @param fileName file name
     */
    void loadRfcData(String fileName);

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

    /**
     * Search Wi-Fi device.
     */
    void searchWifiDevice();

    /**
     * Get time base.
     * @return current time base
     */
    TimeBase getTimeBase();

    /**
     * Is frequency meter enabled.
     * @return true: enabled
     */
    boolean isFrequencyMeterEnabled();

    /**
     * Is counter enabled.
     * @return true: enabled
     */
    boolean isCounterEnabled();

    /**
     * Get capture mode.
     * @return capture mode
     */
    CaptureMode getCaptureMode();

    /**
     * Is channel enabled.
     * @param chIndex channel index. 0:CH1...3:CH4
     * @return true: enabled
     */
    boolean isChEnabled(int chIndex);

    /**
     * Get channel volts per division
     * @param chIndex channel index. 0:CH1...3:CH4
     * @return volts per division
     */
    VoltsPerDivision getVoltsPerDivision(int chIndex);

    /**
     * Get horizontal trigger position
     * @return horizontal trigger position. Range [0, 100]
     */
    int getTriggerXPos();

    /**
     * Get memory depth.
     * @return memory depth.
     */
    int getMemoryDepth();

    /**
     * Get the number of automatic measurement items currently added.
     * @return the number of automatic measurement items currently added.
     */
    int getAutoMeasureNumber();

    /**
     * Populate types of the currently added automatic measurement items into the array.
     * @param autoMeasureTypes the array
     */
    void updateAutoMeasureTypes(int[] autoMeasureTypes);

    /**
     * Populate sources of the currently added automatic measurement items into the array.
     * @param autoMeasureSources the array
     */
    void updateAutoMeasureSources(int[] autoMeasureSources);

    /**
     * Read scope settings from device storage.
     */
    void loadScopeSettings();

    /**
     * Save current scope settings to device storage.
     */
    void saveScopeSettings();

    /**
     * Get the scope settings read from device storage.
     * @return the scope settings
     */
    HtScopeSettings getScopeSettings();

    /**
     * Set timebase.
     * <p> If same as current timebase, do nothing.</p>
     * @param timebaseIndex timebase index 0: 2ns ...35: 1000s
     */
    void setTimebase(int timebaseIndex);

    /**
     * If in scroll capture mode.
     * @return true: in scroll capture mode.
     */
    boolean isInRollMode();

    /**
     * Save Ref file。Save channel data.
     * @param chIndex The channel index to save. 0:CH1...3:CH4
     * @return FAIL: null; SUCCESS: The absolute file path of the saved file.
     */
    String saveRef(int chIndex);

    /**
     * Set the reference channel enabled status.
     * @param enabled new status.
     */
    void setRefEnable(boolean enabled);

    /**
     * Get the reference channel.
     * @return the reference channel
     */
    Channel getRefChannel();

    /**
     * Add message listener.
     * @param listener message listener
     */
    void addListeners(DataSourceListener listener);

    /**
     * Remove message listener.
     * @param listener message listener
     */
    void removeListener(DataSourceListener listener);

    /**
     * Is the reference channel enabled.
     * @return true: enable; false: disabled.
     */
    boolean isRefEnabled();

    /**
     * Send update the scope view's reference channel settings message.
     * @param refChannel current reference channel.
     */
    void sendUpdateScopeViewRefChannelSettingsMessage(Channel refChannel);

    /**
     * Send reference view port position message.
     * @param refViewPortPos reference view new position.
     */
    void sendSetRefViewPortPosMessage(int refViewPortPos);

    /**
     * Get reference channel position.
     * @return reference channel position.
     */
    int getRefChannelPos();

    /**
     * Set reference level position.
     * @param position reference level position.
     */
    void setRefLevelPos(int position);

    /**
     * Get math channel position.
     * @return math channel position.
     */
    int getMathChannelPos();

    /**
     * Set math channel level position.
     * @param position math channel level position.
     */
    void setMathLevelPos(int position);

    /**
     * Send set reference view status message.
     * @param enabled true: show reference view.
     */
    void sendSetRefEnabledMessage(boolean enabled);

    /**
     * Get the math channel.
     * @return the math channel.
     */
    MathChannel getMathChannel();

    /**
     * Send let the scope view to update math channel settings.
     * @param mathChannel current math channel.
     */
    void sendUpdateScopeViewMathChannelSettingsMessage(Channel mathChannel);

    /**
     * Set math channel source A index.
     * @param index 0:CH1...3:CH4
     */
    void setMathSourceAIndex(int index);

    /**
     * Set math channel source B index.
     * @param index 0:CH1...3:CH4
     */
    void setMathSourceBIndex(int index);

    /**
     * Set math channel volts per div.
     * @param voltsPerDivision volts per div.
     */
    void setMathVoltsPerDiv(VoltsPerDivision voltsPerDivision);

    /**
     * Get math channel source A index.
     * @return 0:CH1...3:CH4. -1: no meaning.
     */
    int getMathSourceAIndex();

    /**
     * Get math channel source B index.
     * @return 0:CH1...3:CH4. -1: no meaning.
     */
    int getMathSourceBIndex();

    /**
     * Get math volts index.
     * @return volts index.
     */
    int getMathVoltsIndex();

    /**
     * Get math operator index.
     * @return 0:+  1:-  2:*  3:/  -1: no meaning
     */
    int getMathOperator();

    /**
     * Is the math channel enabled.
     * @return true: enable; false: disabled.
     */
    boolean isMathEnabled();

    /**
     * Send set math enabled status message.
     * @param enabled true: show math channel waveform and math marker.
     */
    void sendSetMathEnabledMessage(boolean enabled);

    /**
     * To listen concrete {@link MainDataSource} object message.
     */
    interface DataSourceListener{

        /**
         *  When load ref file successfully, this method is called.
         * @param refChannel Ref channel
         */
        void onLoadRfcFileSuccessfully(Channel refChannel);

        /**
         * When update the scope view's reference channel settings only, this method is called.
         * @param refChannel current reference channel.
         */
        void onUpdateScopeViewRefChannelSetting(Channel refChannel);

        /**
         * When set reference view port position, this method is called.
         * @param refViewPortPos reference view new position.
         */
        void onSetRefViewPortPos(int refViewPortPos);

        /**
         * When set reference view status, this method is called.
         * @param enabled true: show reference view.
         */
        void onSetRefEnabled(boolean enabled);

        /**
         * When update the scope view's math channel settings only, this method is called.
         * @param mathChannel current math channel.
         */
        void onUpdateScopeViewMathChannelSettings(Channel mathChannel);

        /**
         * When set math channel waveform enabled, this method is called.
         * @param enabled true: show math channel waveform and math marker.
         */
        void onSetMathEnabled(boolean enabled);
    }
}
