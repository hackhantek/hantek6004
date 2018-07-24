package com.openhantek.hantek6000.presenters;

// For portability, don't depend on any Android-specific classes in this class.
import com.hantek.ht6000api.HantekDeviceListener;
import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

public class StatusBarPresenter  {
    private final MainDataSource mDataSource;
    private final StatusBarPresenter.View mView;


    public StatusBarPresenter(StatusBarPresenter.View view) {
        mView = view;
        mDataSource = MainRepository.getInstance();
        mDataSource.addDeviceListener(mHtDeviceListener);
    }

    // synchronize UI
    private void syncWithView() {
        for (int i = 0; i < mDataSource.getAnalogChannelCount();i++) {
            // sync channel enabled status.
            mView.updateChannelEnabledUi(mDataSource.isChannelEnabled(i), i);

            // sync volts per div. +1: CH1
            String prefix = "CH" + (i+1) + " ";
            mView.updateChannelVoltsDiv(prefix + mDataSource.getVoltsDivString(i), i);

            // sync time base
            prefix = "TB ";
            mView.updateTimeBase(prefix + mDataSource.getTimeBaseString());

            // sync run button status
            mView.updateRunButton(mDataSource.isRunning());
        }

        // user can't press AUTO button when scope device is not connected.
        // In the future will support autoset in demo mode, so this is no needed.
        mView.setRunButtonEnabled(mDataSource.isInRealMode());
    }

    // Hantek device listener
    private HantekDeviceListener mHtDeviceListener = new HantekDeviceListener() {
        @Override
        public void onReceivedSamplesData() {
            // sync trigger status
            mView.updateTriggerStatus(mDataSource.isTriggered());
        }

        @Override
        public void onScopeSettingsChanged() {
            syncWithView();
        }

        @Override
        public void onSingleCaptureEnded() {}

        @Override
        public void onSelfCaliEnded() {}

        @Override
        public void onAutosetEnded() {
            mView.closeAutosetDialog();
        }

        @Override
        public void updateFreqCounterMeter(int freqResult, int counterResult) {}
    };

    /**
     * Switch channel enabled status.
     * @param i channel index. 0: CH1
     */
    public void switchChannel(int i) {
        mDataSource.switchChannelEnabled(i);
    }

    /**
     * Switch scope RUN&STOP status.
     */
    public void switchRunStop() {
        mDataSource.switchRunStop();
    }

    /**
     * Start autoset.
     */
    public void startAutoset() {
        mDataSource.startAutoset();
    }

    /**
     * Handle AUTO button click event.
     * <p>Will ask the user if he/she want to autoset.</p>
     */
    public void handleAutoButtonClick() {
        if (mDataSource.isSelfCalibrating()) return;

        mDataSource.startAutoset();

        mView.showAutosetDialog();
    }

    public interface View {

        /**
         * Update view by channel enabled status.
         * @param enabled channel enabled status.
         * @param i channel index. 0: CH1; 1: CH2; 2:CH3; 3:CH4.
         */
        void updateChannelEnabledUi(final boolean enabled, final int i);

        /**
         * Update channel volts per DIV in View.
         * @param voltsDivString current volts per DIV.
         * @param i channel index. 0: CH1
         */
        void updateChannelVoltsDiv(final String voltsDivString, final int i);

        /**
         * Update time base in view.
         * @param timebase current time base in string.
         */
        void updateTimeBase(final String timebase);

        /**
         * Update run button status.
         * @param running true: run status; false: stop status.
         */
        void updateRunButton(final boolean running);

        /**
         * Update trigger status.
         * @param triggered true: triggered; false: no triggered.
         */
        void updateTriggerStatus(final boolean triggered);

        /**
         * Show a dialog, so user can't do any operation when auto setting.
         */
        void showAutosetDialog();

        /**
         * Hide autoset dialog.
         */
        void closeAutosetDialog();

        /**
         * Set autoset button enabled status
         * @param enabled in real mode
         */
        void setRunButtonEnabled(boolean enabled);
    }
}
