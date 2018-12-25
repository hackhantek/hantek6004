package com.openhantek.hantek6000.presenters;

// For portability, don't depend on any Android-specific classes in this class.
import com.hantek.ht6000api.HantekDeviceListener;
import com.openhantek.hantek6000.models.MainDataSource;

public class StatusBarPresenter  {
    private final MainDataSource mDataSource;
    private final StatusBarPresenter.View mView;

    public StatusBarPresenter(View view, MainDataSource dataSource) {
        mView = view;
        mDataSource = dataSource;
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
            mView.updateTimeBase(mDataSource.getTimeBase().ordinal());

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
        // Clear waves. Fix: After single trigger has triggered, press RUN button
        // waves still there.
        if (mDataSource.isRunning()) {
            mDataSource.clearChannels();
            mView.refreshScopeView();
        }
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

    /**
     * Handle MENU button click event.
     * <p>Should popup menu.</p>
     */
    public void handleMenuButtonClick() {

        mView.showMenu();
    }

    /**
     * Set timebase.
     * @param timebaseIndex timebase index 0: 2ns ...35: 1000s
     */
    public void setTimebase(int timebaseIndex) {
        mDataSource.setTimebase(timebaseIndex);
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
         * @param timebaseIndex current time base index. 0:2ns 1: 5ns ...35: 1000s
         */
        void updateTimeBase(final int timebaseIndex);

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

        /**
         * Refresh scope view.
         */
        void refreshScopeView();

        /**
         * Popup main MENU.
         */
        void showMenu();
    }
}
