package com.openhantek.hantek6000.presenters.dialogs;

import com.hantek.ht6000api.Channel;
import com.hantek.ht6000api.ht6000.TimeBase;
import com.hantek.ht6000api.ht6000.VoltsPerDivision;
import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

/**
 * Responsible for the business of the reference waveform dialog.
 */
public class RefDialogPresenter {

    private RefDialogPresenter.View mView;
    private MainDataSource mDataSource;

    public RefDialogPresenter(RefDialogPresenter.View view) {
        mView = view;
        mDataSource = MainRepository.getInstance();
        mDataSource.addListeners(mDataSourceListener);
    }

    /**
     * Save reference waveform.
     * @param channelIndex channel index,0:CH1...3:CH4
     */
    public void saveRef(int channelIndex) {
        String fullName = mDataSource.saveRef(channelIndex);
        if (fullName == null) {
            mView.showSaveDataFailMessage();
        } else {
            mView.showSaveDataSuccessMessage(fullName);
        }
    }

    /**
     * Check permission.
     * Initiate a request to acquire permission when there is no permission.
     */
    public void checkPermission() {
        // Initiate a request when there is no write permission, get permission.
        if(!mView.checkHasPermissionWriteExternalStorage()) {
            mView.requestPermissionWriteExternalStorage();
        }
    }

    /**
     * Set reference waveform status and reference marker status.
     * @param enabled true: show reference waveform and reference marker.
     */
    public void setRefEnabled(boolean enabled) {
        Channel refChannel = mDataSource.getRefChannel();
        if (refChannel.isEnabled() != enabled) {
            mDataSource.sendSetRefEnabledMessage(enabled);
            syncWithView();
        }
    }

    /**
     * Change voltage.
     * @param position An index in the voltage list.
     */
    public void changeVoltsDiv(int position) {
        Channel refChannel = mDataSource.getRefChannel();
        if (refChannel == null) return;

        if (position == refChannel.getVoltsPerDivision().ordinal()) return;

        VoltsPerDivision vpd = VoltsPerDivision.values()[position];
        refChannel.setVoltsPerDivision(vpd);

        mDataSource.sendUpdateScopeViewRefChannelSettingsMessage(mDataSource.getRefChannel());
    }

    /**
     * Change time base.
     * @param position An index in the time base list.
     */
    public void changeTimeBase(int position) {
        Channel refChannel = mDataSource.getRefChannel();
        if (refChannel == null) return;

        if (position == refChannel.getCurrentTimebase().ordinal()) return;

        TimeBase tb = TimeBase.values()[position];
        refChannel.setCurrentTimebase(tb);

        mDataSource.sendUpdateScopeViewRefChannelSettingsMessage(mDataSource.getRefChannel());
    }

    /**
     * Set reference view position.
     * @param refViewPortPos reference view position.
     */
    public void setRefViewPortPos(int refViewPortPos) {
        mDataSource.sendSetRefViewPortPosMessage(refViewPortPos);
    }

    /**
     * Synchronize the contexts of the reference waveform dialog.
     */
    public void syncWithView() {
        Channel refChannel = mDataSource.getRefChannel();
        if (refChannel == null) return;

        int index = refChannel.getVoltsPerDivision().ordinal();
        mView.updateVoltsSpinner(index);
        index = refChannel.getCurrentTimebase().ordinal();
        mView.updateTimebaseSpinner(index);
        mView.updateCheckBoxStatus(refChannel.isEnabled());
        mView.updateElementsEnabledState(mDataSource.isRefEnabled());
        mView.updateScrollBar(refChannel.getValidLength(), mView.getRefViewPortPos());
    }

    // For receive MainDtaSource object message.
    private final MainDataSource.DataSourceListener mDataSourceListener
            = new MainDataSource.DataSourceListener() {

        @Override
        public void onLoadRfcFileSuccessfully(Channel refChannel) {
            syncWithView();
        }

        @Override
        public void onUpdateScopeViewRefChannelSetting(Channel refChannel) {

        }

        @Override
        public void onSetRefViewPortPos(int refViewPortPos) {

        }

        @Override
        public void onSetRefEnabled(boolean enabled) {

        }

        @Override
        public void onUpdateScopeViewMathChannelSettings(Channel mathChannel) {

        }

        @Override
        public void onSetMathEnabled(boolean enabled) {

        }
    };

    /**
     * Methods that MVP REF view should implement.
     */
    public interface View{

        /**
         * Called when file save fail.
         */
        void showSaveDataFailMessage();

        /**
         * Called when file save successful.
         * @param fullName full path file name.
         */
        void showSaveDataSuccessMessage(String fullName);

        /**
         * Check whether "WRITE_EXTERNAL_STORAGE" is granted.
         * @return true: granted; false: not granted.
         */
        boolean checkHasPermissionWriteExternalStorage();

        /**
         * Request "WRITE_EXTERNAL_STORAGE" permission.
         */
        void requestPermissionWriteExternalStorage();

        /**
         * Update the voltage spinner.
         * @param refOriginalVoltsPerDivision the current voltage per division index.
         */
        void updateVoltsSpinner(int refOriginalVoltsPerDivision);

        /**
         * Update the timebase spinner.
         * @param index the current timebase index.
         */
        void updateTimebaseSpinner(int index);

        /**
         * Changes the checked state of the check box
         * @param refEnabled current reference enabled status.
         */
        void updateCheckBoxStatus(boolean refEnabled);

        /**
         * Update the UI element enabled status by reference enabled state.
         * @param refEnabled current reference enabled status.
         */
        void updateElementsEnabledState(boolean refEnabled);

        /**
         * Get reference channel view port position.
         * @return view port position.
         */
        int getRefViewPortPos();

        /**
         * Update reference scroll bar.
         * @param dataLength reference channel data length
         * @param viewPortPos view port position
         */
        void updateScrollBar(int dataLength, int viewPortPos);
    }
}
