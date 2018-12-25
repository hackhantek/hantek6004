package com.openhantek.hantek6000.presenters;

import com.hantek.ht6000api.Channel;
import com.hantek.ht6000api.HantekDevice;
import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HantekSdk;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.hantek.ht6000api.ht6000.TriggerSweep;
import com.hantek.ht6000api.HtScopeSettings;
import com.openhantek.hantek6000.models.HtUsbManagerInterface;
import com.openhantek.hantek6000.models.MainDataSource;

public class MainPresenter {

    private final MainPresenter.View mView;
    private final HtUsbManagerInterface mHantekUsbManager;
    private final MainDataSource mDataSource;

    public MainPresenter(View view, HtUsbManagerInterface hantekUsbManager, MainDataSource dataSource) {
        assert view != null;
        assert hantekUsbManager != null;
        assert dataSource != null;

        mView = view;
        mHantekUsbManager = hantekUsbManager;
        mDataSource = dataSource;
        mDataSource.addListeners(mDataSourceListener);
    }

    //region Helper methods

    // Hantek device listener
    private HantekDeviceListener mHtDeviceListener = new HantekDeviceListener() {
        @Override
        public void onReceivedSamplesData() {
            mView.updateScopeView();
        }

        @Override
        public void onScopeSettingsChanged() {
            // 1. To support change timebase and volts/div after stopped.
            // 2.Put updateScopeView before syncWithView to fix: horizontal trigger thumb
            // too small when first start APP(#35).
            mView.updateScopeView();
            syncWithView();
        }

        @Override
        public void onSingleCaptureEnded() {
            mView.playSingleCaptureSound();
        }

        @Override
        public void onSelfCaliEnded() {

        }

        @Override
        public void onAutosetEnded() {

        }

        @Override
        public void updateFreqCounterMeter(int freqResult, int counterResult) {

        }
    };

    // 用于接收 MainDtaSource 对象的消息。
    private final MainDataSource.DataSourceListener mDataSourceListener
            = new MainDataSource.DataSourceListener() {

        @Override
        public void onLoadRfcFileSuccessfully(Channel refChannel) {
            mView.updateScopeViewRefChannel(refChannel);
            // 更新参考波形标识符的显示状态，因为参考波形显示了，对应的标识符也需要显示出来。
            mView.updateRefLevelVisibility(true);
            // 更新参考波形标识符的位置
            mView.updateRefLevelPos(mDataSource.getRefChannelPos());
        }

        @Override
        public void onUpdateScopeViewRefChannelSetting(Channel refChannel) {
            mView.updateScopeViewRefChannel(refChannel);
        }

        @Override
        public void onSetRefViewPortPos(int refViewPortPos) {
            mView.setRefViewPortPos(refViewPortPos);
        }

        @Override
        public void onSetRefEnabled(boolean enabled) {
            Channel refChannel = mDataSource.getRefChannel();
            refChannel.setEnabled(enabled);
            // update reference waveform
            mView.updateScopeViewRefChannel(refChannel);
            // update reference marker
            mView.updateRefLevelVisibility(enabled);
        }

        @Override
        public void onSetMathEnabled(boolean enabled) {
            Channel mathChannel = mDataSource.getMathChannel();
            mathChannel.setEnabled(enabled);
            // update math channel waveform
            mView.updateScopeViewMathChannel(mathChannel);
            // update math marker status
            mView.updateMathLevelVisibility(enabled);
            // update math marker position
            mView.updateMathLevelPos(mDataSource.getMathChannelPos());
        }

        @Override
        public void onUpdateScopeViewMathChannelSettings(Channel mathChannel) {
            mView.updateScopeViewMathChannel(mathChannel);
        }
    };

    /**
     * Enter demo mode, show demo mode entered dialog.
     */
    private void enterDemoMode() {
        mHantekUsbManager.loadDemoDevice(mHtDeviceListener, mDataSource.getScopeSettings());

        mView.showEnterDemoModelDialog();
    }

    private boolean isUsbConnected() {
        return mHantekUsbManager.isScopeDeviceExist();
    }

    private boolean isWifiConnected() {
        return false;
    }

    //endregion Helper methods

    //region Presenter Method

    /**
     * Determine work mode.
     * <p>There are 3 modes: USB/Wi-Fi/Demo</p>
     */
    public void determineWorkMode() {
        if (isUsbConnected()) {
            if (mHantekUsbManager.hasUsbPermission()) {
                handleUsbPermissionGranted();
            } else {
                mView.requestUsbPermission(mHantekUsbManager.getUsbDevice());
            }
        } else {
            // Hantek6xx4 don't support Wi-Fi, We put this code here to compatible with
            // IDSO1070 code.
            boolean isWifiConnected = isWifiConnected();
            if (isWifiConnected) {
                mView.showSearchingDevice();
                mDataSource.searchWifiDevice();
            } else {
                enterDemoMode();
            }
        }
    }

    /**
     * Synchronize mvp view with model status.
     */
    public void syncWithView() {

        for (int i = 0; i < mDataSource.getAnalogChannelCount(); i++) {
            // sync channel zero level position.
            mView.updateZeroLevelPos(i, mDataSource.getChannelPos(i));
            // sync channel marker visible with channel enabled status
            mView.updateChZeroLevelMarkerVisibility(mDataSource.isChannelEnabled(i), i);
            // update coupling in zero level marker
            mView.updateChCouplingIndicator(i, mDataSource.getInputCoupling(i));
        }

        // sync trigger level marker
        mView.updateTriggerLevelPos(mDataSource.getTriggerLevelPos());
        mView.updateTriggerLevelVisibility(true);

        // sync reference view
        if (mDataSource.isRefEnabled()) {
            mView.updateScopeViewRefChannel(mDataSource.getRefChannel());
        }

        // sync math channel view
        if (mDataSource.isMathEnabled()) {
            mView.updateScopeViewMathChannel(mDataSource.getMathChannel());
        }

        // trigger marker color
        mView.updateTriggerLevelColor(mDataSource.getTriggerSource());

        mView.updateCursorVisibility(mDataSource.isCursorMeasureEnabled());
        updateCursorMeasureResult();

        // Set the trigger scroll thumb position when app started
        float vpPos = mView.getViewPortRelativePos();
        mView.setTriggerThumbPos(vpPos);

        // Update horizontal trigger scroll bar thumb width.
        int viewportSize = mView.getViewPortSize();
        mView.updateScrollBarThumbWidth(viewportSize);

        /* Update horizontal trigger marker position and range. */
        int xPos = HantekSdk.getTriggerXPos();
        // Horizontal trigger position when marker moved to most left.
        // Trigger marker can only move within the viewport.
        float xTriggerMax = mView.getViewPortRightRelativePos() * 100;
        // Horizontal trigger position when marker moved to most right.
        float xTriggerMin = mView.getViewPortLeftRelativePos() * 100;
        // Why update the range?
        // Because the viewport position can changed when user drag waveform left and right.
        mView.updateXTriggerMarker(xTriggerMin, xTriggerMax, xPos);

        // If trigger source changed by trigger quick settings popup window,
        // trigger level marker will changed,
        // need to update trigger quick settings popup window position.
        if (mView.isTriggerQuickSettingsShowing()) {
            mView.updateTriggerQuickSettings();
        }

        // If in scroll mode, hide horizontal trigger marker and horizontal trigger scroll bar
        if (mDataSource.isInRollMode()) {
            mView.hideHorizontalTrigger();
        } else {
            mView.showHorizontalTrigger();
        }
    }

    /**
     * Called when usb permission is granted
     */
    public void handleUsbPermissionGranted() {
        if (mView.isAskDemoDialogShowing()) {
            mView.closeAskDemoDialog();
        }
        boolean success = mHantekUsbManager.setDevice();
        if (success)
            mHantekUsbManager.loadRealDevice(mHtDeviceListener, mDataSource.getScopeSettings());
    }

    /**
     * Releases exclusive access to USB, close usb send and receive thread.
     */
    public void releaseDevice() {
        mHantekUsbManager.releaseDevice();
    }

    /**
     * Get analog channels count.
     * @return analog channels count.
     */
    public int getAnalogChannelCount() {
        return mDataSource.getAnalogChannelCount();
    }


    /**
     * Called this method after channel zero level marker drag ended.
     * @param i channel index. 0:CH1...
     * @param position new position
     */
    public void handleChZeroMarkerDragEnded(int i, int position) {
        // How much changed
        int delta = position - mDataSource.getChannelPos(i);

        mDataSource.setChannelPos(i, position);

        // If i is trigger source, change trigger level
        if (i == mDataSource.getTriggerSource()) {
            int triggerLevel = mDataSource.getTriggerLevelPos() + delta;
            // Limit value cannot be out of range
            if (triggerLevel < HantekDevice.SAMPLE_MIN) triggerLevel = HantekDevice.SAMPLE_MIN;
            if (triggerLevel > HantekDevice.SAMPLE_MAX) triggerLevel = HantekDevice.SAMPLE_MAX;
            mDataSource.setTriggerLevelPos(triggerLevel);
        }
    }

    /**
     * Change trigger level posisiton.
     * @param position trigger level position.
     */
    public void changeTriggerLevelPos(int position) {
        mDataSource.setTriggerLevelPos(position);
    }

    /**
     * Set channel input coupling.
     * @param chIndex channel index.
     * @param inputCoupling input coupling
     */
    public void setCoupling(int chIndex, InputCoupling inputCoupling) {
        mDataSource.setCoupling(chIndex, inputCoupling);
    }

    /**
     * Get channel input coupling.
     * @param chIndex channel index
     * @return channel input coupling
     */
    public InputCoupling getCoupling(int chIndex) {
        return mDataSource.getCoupling(chIndex);
    }

    /**
     * Get channel attenuation factor.
     * @param chIndex channel index.
     * @return attenuation factor.
     */
    public AttenuationFactor getAttenuationFactor(int chIndex) {
        return mDataSource.getAttenuationFactor(chIndex);
    }

    /**
     * Set attenuation factor.
     * @param chIndex channel index.
     * @param attenuationFactor attenuation factor.
     */
    public void setAttenuationFactor(int chIndex, AttenuationFactor attenuationFactor) {
        mDataSource.setAttenuationFactor(chIndex, attenuationFactor);
    }

    /**
     * Set trigger source.
     * <p>This will change trigger level to the new source channel zero level.</p>
     * @param source new trigger source
     */
    public void setTriggerSource(int source) {
        mDataSource.setTriggerSource(source);

        // Change trigger level to the new source channel zero level
        int level = mDataSource.getChannelPos(source);
        mDataSource.setTriggerLevelPos(level);
    }

    /**
     * Set trigger sweep.
     * @param sweep new trigger sweep
     */
    public void setTriggerSweep(TriggerSweep sweep) {
        mDataSource.setTriggerSweep(sweep);
        mDataSource.clearChannels();
        mView.refreshScopeView();
    }

    /**
     * Set trigger slope.
     * @param slope new trigger slope.
     */
    public void setTriggerSlope(TriggerSlope slope) {
        mDataSource.setTriggerSlope(slope);
    }

    /**
     * Get trigger sweep.
     * @return current trigger sweep
     */
    public TriggerSweep getTriggerSweep() {
        return mDataSource.getTriggerSweep();
    }

    /**
     * Get trigger source.
     * @return trigger source. 0:CH1...
     */
    public int getTriggerSource() {
        return mDataSource.getTriggerSource();
    }

    /**
     * Get trigger slope.
     * @return current trigger slope.
     */
    public TriggerSlope getTriggerSlope() {
        return mDataSource.getTriggerSlope();
    }

    /**
     * Put channel zero level in center.
     * @param chIndex channel index 0: CH1
     */
    public void centerChannelLevel(int chIndex) {
        mDataSource.centerChannelLevel(chIndex);
        mDataSource.centerTriggerLevel();
    }

    /**
     * Put trigger level in center of trigger source channel waveform.
     */
    public void centerTriggerLevel() {
        mDataSource.centerTriggerLevel();
    }

    /**
     * Increase timebase to ndex bigger.
     */
    public void increaseTimebase() {
        if (mDataSource.isLargestTimebase()) {
            mView.promptLargestTimebase();
            return;
        }

        mDataSource.increaseTimebase();
    }

    /**
     * Decrease timebase to previous smaller.
     */
    public void decreaseTimebase() {
        if (mDataSource.isSmallestTimebase()) {
            mView.promptSmallestTimebase();
            return;
        }
        
        mDataSource.decreaseTimebase();
    }

    /**
     * Change the selected channel.
     * @param chIndex new selected channel.
     */
    public void changeSelectedChannel(int chIndex) {
        mDataSource.setSelectedChannel(chIndex);
        // To support change selected channel after stop
        mView.updateScopeView();
    }

    /**
     * Decrease the volts per div of current selected channel.
     */
    public void decreaseVoltsPerDiv() {
        if(mDataSource.isSmallestVoltsPerDiv()) {
            mView.promptSmallestVoltsPerDiv();
            return;
        }

        mDataSource.decreaseVoltsPerDiv();
    }

    /**
     * Increase the volts per div of current selected channel.
     */
    public void increaseVoltsPerDiv() {
        if(mDataSource.isLargestVoltsPerDiv()) {
            mView.promptLargestVoltsPerDiv();
            return;
        }
        
        mDataSource.increaseVoltsPerDiv();
    }

    /**
     * Update Y1 cursor result. It show the voltage difference to the channel zero level.
     */
    public void updateCursorY1() {
        int position = mView.getY1Position();
        String voltage = HantekSdk.getVoltageOrAmpereStringAtPosition(position);
        mView.updateY1Result(voltage);
    }

    /**
     * Update Y2 cursor result. It show the voltage difference to the channel zero level.
     */
    public void updateCursorY2() {
        int position = mView.getX1Position();
        String voltage = HantekSdk.getVoltageOrAmpereStringAtPosition(position);
        mView.updateY2Result(voltage);
    }

    /**
     * Update cursor measure result.
     */
    public void updateCursorMeasureResult() {
        if (!HantekSdk.isCursorEnabled()) return;

        /* Update voltage */
        // the voltage delta between two cursors to be positive.
        int delta = Math.abs(mView.getY1Position() - mView.getY2Position());
        String voltage = HantekSdk.getVoltageOrAmpereStringOfDelta(delta);
        mView.updateCursorResultDeltaV(voltage);

        /* Update time delta and frequency */
        delta = mView.getX1Position() - mView.getX2Position();
        String time = HantekSdk.getTimeStringOfDelta(delta);
        mView.updateCursorResultDeltaTime(time);
        String freq = HantekSdk.getFrequencyStringOfDelta(delta);
        mView.updateCursorResultFreq(freq);
    }

    /**
     * Change horizontal trigger position.
     * @param position New horizontal trigger position.
     */
    public void changeTriggerXPos(int position) {
        HantekSdk.setTriggerXPos(position);
    }

    /**
     * Set horizontal trigger position to center.
     */
    public void centerTriggerXPos() {
        mView.centerViewPort();

        // Set horizontal trigger to center which range is [0,100].
        changeTriggerXPos(50);
    }

    /**
     * Get current scope settings.
     * @return current scope settings
     */
    public HtScopeSettings getCurrentScopeSettings() {
        HtScopeSettings settings = new HtScopeSettings();

        for (int i = 0; i < mDataSource.getAnalogChannelCount(); i++) {
            settings.setChEnabled(i, mDataSource.isChEnabled(i));
            settings.setChZeroLevel(i, mDataSource.getChannelPos(i));
            settings.setVoltsPerDivision(i, mDataSource.getVoltsPerDivision(i).ordinal());
            settings.setAttenuationFactor(i, mDataSource.getAttenuationFactor(i).ordinal());
            settings.setInputCoupling(i, mDataSource.getInputCoupling(i).ordinal());
        }

        settings.setTriggerSweep(mDataSource.getTriggerSweep().ordinal());
        settings.setTriggerSource(mDataSource.getTriggerSource());
        settings.setTriggerLevel(mDataSource.getTriggerLevelPos());
        settings.setTriggerXPos(mDataSource.getTriggerXPos());
        settings.setTriggerSlope(mDataSource.getTriggerSlope().ordinal());

        settings.setRunning(mDataSource.isRunning());
        settings.setMemoryDepth(mDataSource.getMemoryDepth());
        settings.setTimeBase(mDataSource.getTimeBase().ordinal());
        settings.setCaptureMode(mDataSource.getCaptureMode().ordinal());
        settings.setFrequencyMeterEnabled(mDataSource.isFrequencyMeterEnabled());
        settings.setCounterEnabled(mDataSource.isCounterEnabled());

        settings.setAutoMeasureNumber(mDataSource.getAutoMeasureNumber());
        for (int i = 0; i < settings.getAutoMeasureNumber(); i++ ) {
            mDataSource.updateAutoMeasureTypes(settings.getAutoMeasureTypes());
        }
        for (int i = 0; i < settings.getAutoMeasureNumber(); i++ ) {
            mDataSource.updateAutoMeasureSources(settings.getAutoMeasureSources());
        }

        return settings;
    }

    /**
     * Called after view created
     */
    public void handleViewCreatedEvent() {
        mView.keepScreenOn();
    }

    /**
     * Read scope settings from device storage.
     */
    public void loadScopeSettings() {
        mDataSource.loadScopeSettings();
    }

    /**
     * Save current scope settings to device storage.
     */
    public void saveScopeSettings() {
        mDataSource.saveScopeSettings();
    }

    /**
     * Change reference level position.
     * @param position reference level position
     */
    public void changeRefLevelPos(int position) {
        mDataSource.setRefLevelPos(position);
    }

    /**
     * Change math channel level position.
     * @param position math channel level position.
     */
    public void changeMathLevelPos(int position) {
        mDataSource.setMathLevelPos(position);
    }
    //endregion Presenter Method

    public interface View {

        void requestUsbPermission(Object object);

        /**
         * Called when need to update Scope View.
         */
        void updateScopeView();

        boolean isAskDemoDialogShowing();

        void closeAskDemoDialog();

        Object getScopeView();

        /**
         * Update channel zero level position.
         * @param i channel index. 0:CH1...
         * @param zeroLevelPos current channel zero position.
         */
        void updateZeroLevelPos(final int i, final int zeroLevelPos);

        /**
         * Update channel zero marker visibility
         * @param channelEnabled channel enabled status
         * @param i channel index. 0:CH1...
         */
        void updateChZeroLevelMarkerVisibility(boolean channelEnabled, int i);

        /**
         * Update ref marker visibility.
         * @param visible true: show false: hide
         */
        void updateRefLevelVisibility(boolean visible);

        void updateTriggerLevelPos(int triggerLevelPos);

        void updateTriggerLevelVisibility(boolean visible);

        void updateTriggerLevelColor(int triggerSource);

        /**
         * Update channel input coupling in zero level marker.
         * @param chIndex channel index. 0:CH1
         * @param inputCoupling input coupling
         */
        void updateChCouplingIndicator(int chIndex, InputCoupling inputCoupling);

        void promptLargestVoltsPerDiv();

        void promptSmallestVoltsPerDiv();

        void promptLargestTimebase();

        void promptSmallestTimebase();

        /**
         * Refresh HtScopeView.
         */
        void refreshScopeView();

        /**
         * Play a sound, let user know single triggered.
         */
        void playSingleCaptureSound();

        /**
         * UI should update cursor visibility when this method is called.
         * @param cursorMeasureEnabled whether cursor measure is enabled.
         */
        void updateCursorVisibility(boolean cursorMeasureEnabled);

        /**
         * UI should update cursor result delta voltage after this method is called.
         * @param voltage the delta voltage between the Y1 and Y2 cursor.
         */
        void updateCursorResultDeltaV(String voltage);

        /**
         * UI should update cursor result delta time after this method is called.
         * @param time the delta voltage between the X1 and X2 cursor.
         */
        void updateCursorResultDeltaTime(String time);

        /**
         * UI should update cursor result frequency after this method is called.
         * @param freq the frequency. It assume it is a full cycle signal between
         *             the X1 and X2 cursor.
         */
        void updateCursorResultFreq(String freq);

        /**
         * UI should update Y1 cursor indicator result text.
         * @param voltage the delta voltage between the cursor and the channel zero level.
         */
        void updateY1Result(String voltage);

        /**
         * UI should update Y2 cursor indicator result text.
         * @param voltage the delta voltage between the cursor and the channel zero level.
         */
        void updateY2Result(String voltage);

        int getY1Position();

        int getY2Position();

        int getX1Position();

        int getX2Position();

        /**
         * Get view port position.
         * @return range is [0.0, 1.0].
         */
        float getViewPortRelativePos();

        /**
         * Set horizontal trigger thumb position.
         * @param pos range is [0.0, 1.0]
         */
        void setTriggerThumbPos(float pos);

        /**
         * Update horizontal trigger marker.
         * @param xTriggerMin Horizontal trigger marker range lower value.
         * @param xTriggerMax Horizontal trigger marker range higher value.
         * @param triggerXPos Current horizontal trigger position.
         */
        void updateXTriggerMarker(float xTriggerMin, float xTriggerMax, int triggerXPos);

        /**
         * Get the position of the right side of the viewport within the memory depth.
         * <br><br> Range is [0, 1.0]. When the viewport position is at the far left, the relative position is 0,
         * and at the far right, the relative position is 1.0.
         * <br> The viewport position is the position of the middle of the viewport
         * @return the position of the right side of the viewport within the memory depth.
         */
        float getViewPortRightRelativePos();

        /**
         * Get the position of the left side of the viewport within the memory depth.
         * <br><br> Range [0, 1.0]. When the viewport position is at the far left, the relative position is 0,
         * and at the far right, the relative position is 1.0.
         * <br> The viewport position is the position of the middle of the viewport
         * @return the position of the left side of the viewport within the memory depth.
         */
        float getViewPortLeftRelativePos();

        /**
         * 调整视口位置至中间(Set view port position to center).
         */
        void centerViewPort();

        /**
         * {@link View} should show demo mode entered dialog.
         */
        void showEnterDemoModelDialog();

        /**
         * Get the number of points that can be drawn in the viewport
         * under the current RUN/STOP status and timebase.
         * @return the number of points that can be drawn in the viewport
         */
        int getViewPortSize();

        /**
         * Update the horizontal scroll bar thumb width.
         * @param viewportSize viewport size.
         */
        void updateScrollBarThumbWidth(int viewportSize);

        /**
         * Indicate whether trigger quick settings popup window is showing on screen.
         * @return true if the popup is showing, false otherwise
         */
        boolean isTriggerQuickSettingsShowing();

        /**
         * Updates the position of the trigger quick settings popup window.
         */
        void updateTriggerQuickSettings();

        void showSearchingDevice();

        /**
         * Keep the screen on.
         */
        void keepScreenOn();

        /**
         * Hide horizontal trigger marker and horizontal trigger scroll bar.
         */
        void hideHorizontalTrigger();

        /**
         * Show horizontal trigger marker and horizontal trigger scroll bar.
         */
        void showHorizontalTrigger();

        /**
         * Update the scope view's reference channel.
         * @param refChannel current reference channel.
         */
        void updateScopeViewRefChannel(Channel refChannel);

        /**
         * Set reference view port position.
         * @param refViewPortPos reference view new position.
         */
        void setRefViewPortPos(int refViewPortPos);

        /**
         * Update reference level marker position.
         * @param position reference level marker position.
         */
        void updateRefLevelPos(int position);

        /**
         * Update the scope view's math channel.
         * @param mathChannel current math channel.
         */
        void updateScopeViewMathChannel(Channel mathChannel);

        /**
         * Update math marker visibility.
         * @param visible true: show false: hide
         */
        void updateMathLevelVisibility(boolean visible);

        /**
         * Update math marker position.
         * @param position math marker position
         */
        void updateMathLevelPos(int position);
    }
}
