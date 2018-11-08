package com.openhantek.hantek6000.presenters;

import android.util.Log;

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HantekSdk;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.hantek.ht6000api.ht6000.TriggerSweep;
import com.openhantek.hantek6000.models.HtUsbManagerInterface;
import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

public class MainPresenter {

    private final MainPresenter.View mView;
    private final HtUsbManagerInterface mHantekUsbManager;
    private final MainDataSource mDataSource;

    public MainPresenter(MainPresenter.View view, HtUsbManagerInterface hantekUsbManager) {
        assert view != null;
        assert  hantekUsbManager != null;

        mView = view;
        mHantekUsbManager = hantekUsbManager;
        mDataSource = MainRepository.getInstance();
    }

    //region Helper methods
    private void loadRealDevice() {
        int[] colors = mView.getChannelColors();
        Object scopeView = mView.getScopeView();

        assert colors != null;
        assert scopeView != null;

        mHantekUsbManager.loadRealDevice(mHtDeviceListener, colors, scopeView);
    }

    // Hantek device listener
    private HantekDeviceListener mHtDeviceListener = new HantekDeviceListener() {
        @Override
        public void onReceivedSamplesData() {
            mView.updateScopeView();
        }

        @Override
        public void onScopeSettingsChanged(){
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
            mView.updateChCouplingIndicator(i, mDataSource.getChInputCoupling(i));
        }

        // sync trigger level marker
        mView.updateTriggerLevelPos(mDataSource.getTriggerLevelPos());
        mView.updateTriggerLevelVisibility(true);

        // trigger marker color
        mView.updateTriggerLevelColor(mDataSource.getTriggerSource());

        mView.updateCursorVisibility(mDataSource.isCursorMeasureEnabled());
        updateCursorMeasureResult();

        // Set the trigger scroll thumb position when app started
        float vpPos = mView.getViewPortRelativePos();
        mView.setTriggerThumbPos(vpPos);

        /* update horizontal trigger marker */
        int xPos = HantekSdk.getTriggerXPos();
        // 100: 水平触发范围[0,100]
        float xTriggerMax = mView.getViewPortRightRelativePos() * 100;
        float xTriggerMin = mView.getViewPortLeftRelativePos() * 100;
        mView.updateXTriggerMarker(xTriggerMin, xTriggerMax, xPos);
    }
    //endregion Helper methods

    //region Presenter Method

    /**
     * Check if specific USB device exist.
     * <ul>
     *     <li>exist：Check whether permission granted</li>
     *     <ul>
     *         <li>granted：setDevice</li>
     *         <li>not granted：Request permission</li>
     *     </ul>
     *     <li>not exist: Ask if user need to load analog device</li>
     * </ul>
     * @param device_filter an xml file that specifies that any USB device with specified
     *                      attributes should be filtered.
     */
    public void checkDeviceExist(int device_filter) {
        if (mHantekUsbManager.isScopeDeviceExist(device_filter)) {
            if (mHantekUsbManager.hasUsbPermission()) {
                handleUsbPermissionGranted();
            } else {
                mView.requestUsbPermission(mHantekUsbManager.getUsbDevice());
            }
        } else {
            mView.askToLoadDemoDevice();
        }
    }

    public void handleUsbPermissionGranted() {
        if (mView.isAskDemoDialogShowing()) {
            mView.closeAskDemoDialog();
        }
        boolean success = mHantekUsbManager.setDevice();
        if (success)
            loadRealDevice();
    }

    public void releaseDevice() {
        mHantekUsbManager.releaseDevice();
    }

    public void loadDemoDevice(int[] colors, Object scopeView) {
        assert colors != null;
        assert scopeView != null;

        mHantekUsbManager.loadDemoDevice(mHtDeviceListener, colors, scopeView);
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
        mDataSource.setChannelPos(i, position);
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
        mDataSource.setAttenuationFacotr(chIndex, attenuationFactor);
    }

    /**
     * Set trigger source.
     * @param source new trigger source
     */
    public void setTriggerSource(int source) {
        mDataSource.setTriggerSource(source);
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
     * 设置水平触发位置。
     * @param position 想要设置的水平触发位置
     */
    public void changeTriggerXPos(int position) {
        HantekSdk.setTriggerXPos(position);
    }


    /**
     * Set horizontal trigger position to center. (设置水平触发位置和视口位置到中间)。
     */
    public void centerTriggerXPos() {
        // 设置视口位置到中间
        mView.centerViewPort();

        // 设置水平触发位置在中间。水平触发位置范围[0,100], 50 在中间
        changeTriggerXPos(50);
    }

    //endregion Presenter Method

    public interface View {

        void requestUsbPermission(Object object);

        void askToLoadDemoDevice();

        /**
         * Called when need to update Scope View.
         */
        void updateScopeView();

        boolean isAskDemoDialogShowing();

        void closeAskDemoDialog();

        int[] getChannelColors();

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
         * 更新触发标识符
         * @param xTriggerMin 触发表示符调节范围下限
         * @param xTriggerMax 触发标识符调节范围上限
         * @param triggerXPos 当前水平触发位置
         */
        void updateXTriggerMarker(float xTriggerMin, float xTriggerMax, int triggerXPos);

        /**
         * 获取水平触发触发标识符设置范围上限。
         * @return 水平触发触发标识符设置范围上限
         */
        float getViewPortRightRelativePos();

        /**
         * 获取水平触发触发标识符设置范围下限。
         * @return 水平触发触发标识符设置范围下限
         */
        float getViewPortLeftRelativePos();

        /**
         * 调整视口位置至中间(Set view port position to center).
         */
        void centerViewPort();
    }
}
