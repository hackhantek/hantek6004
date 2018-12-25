package com.openhantek.hantek6000.presenters.menus.tools;

import com.hantek.ht6000api.HantekDeviceListener;
import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

public class ToolsMenuPresenter {

    /**
     * A View that implment {@link View} interface.
     */
    private final View mView;
    private final MainDataSource mDataSource;

    public ToolsMenuPresenter(ToolsMenuPresenter.View view) {
        mView = view;
        mDataSource = MainRepository.getInstance();
    }

    /**
     * The user click factory setup in menu, this method will be called.
     */
    public void handleFactorySetupClick() {
        mView.askWhetherToFactorySetup();
    }

    /**
     * This method is called after user clicked self calibration in menu.
     */
    public void handleSelfCaliClick() {
        if (mDataSource.isInRealDeviceMode()) {
            mView.askSelfCalibration();
        } else {
            mView.promptRealDevice();
        }
    }

    /**
     * This method is called after user clicked ref in menu.
     */
    public void handleRefClick(){
        mView.showRefDialog();
    }

    /**
     * This method is called after user clicked math in menu.
     */
    public void handleMathClick() {
        mView.showMathDialog();
    }

    /**
     * Reset to factory setup.
     */
    public void resetToFactory() {
        mDataSource.resetToFactory();
    }

    /**
     * Start self calibration.
     */
    public void startSelfCalibration() {
        mDataSource.startSelfCalibration();
        mDataSource.addDeviceListener(mDeviceListener);
    }

    /**
     * Stop self calibration.
     */
    public void stopSelfCalibration() {
    }

    // To get notified after self calibration ended.
    private final HantekDeviceListener mDeviceListener = new HantekDeviceListener() {
        @Override
        public void onReceivedSamplesData() {
        }

        @Override
        public void onScopeSettingsChanged() {

        }

        @Override
        public void onSingleCaptureEnded() {

        }

        @Override
        public void onSelfCaliEnded() {
            // Remove listener after calibration ended.
            mDataSource.removeDeviceListener(mDeviceListener);
            mView.closeCalibratingDialog();
        }

        @Override
        public void onAutosetEnded() {

        }

        @Override
        public void updateFreqCounterMeter(int freqResult, int counterResult) {

        }
    };

    public interface View {

        /**
         * Ask user whether to continue factory setup.
         */
        void askWhetherToFactorySetup();

        /**
         * Ask user whether to continue self calibration.
         */
        void askSelfCalibration();

        /**
         * Show reference waveform dialog.
         */
        void showRefDialog();

        /**
         * Show math channel dialog.
         */
        void showMathDialog();

        /**
         * Prompt user self-calibration can only be performed after connecting the real device.
         */
        void promptRealDevice();

        /**
         * Close calibrating dialog.
         */
        void closeCalibratingDialog();
    }
}
