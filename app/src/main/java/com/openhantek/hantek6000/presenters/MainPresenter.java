package com.openhantek.hantek6000.presenters;

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;
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
            syncWithUi();
        }

        @Override
        public void onSingleCaptureEnded() {

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
    private void syncWithUi() {

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
     * Set trigger slope.
     * @param slope new trigger slope.
     */
    public void setTriggerSlope(TriggerSlope slope) {
        mDataSource.setTriggerSlope(slope);
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
    }
}
