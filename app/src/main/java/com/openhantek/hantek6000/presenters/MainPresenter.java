package com.openhantek.hantek6000.presenters;

import com.hantek.ht6000api.HantekDeviceListener;
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
        }

        // sync trigger level marker
        mView.updateTriggerLevelPos(mDataSource.getTriggerLevelPos());
        mView.updateTriggerLevelVisibility(true);
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
    }
}
