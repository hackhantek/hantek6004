package com.openhantek.hantek6000.presenters;

import com.hantek.ht6000api.HantekDeviceListener;
import com.openhantek.hantek6000.models.HtUsbManagerInterface;

public class MainPresenter implements MainContract.Presenter{

    private final MainContract.View mMainView;
    private final HtUsbManagerInterface mHantekUsbManager;

    public MainPresenter(MainContract.View mainView, HtUsbManagerInterface hantekUsbManager) {
        assert mainView != null;
        assert  hantekUsbManager != null;

        mMainView = mainView;
        mHantekUsbManager = hantekUsbManager;
    }

    //region MainContract.Presenter

    //region USB

    // check if specific USB device exist
    //      exist：
    //          check whether permission granted
    //              granted：setDevice
    //              not granted：ask permission
    //      not exist：Ask if user need to load analog device
    @Override
    public void checkDeviceExist(int device_filter) {
        if (mHantekUsbManager.isScopeDeviceExist(device_filter)) {
            if (mHantekUsbManager.hasUsbPermission()) {
                handleUsbPermissionGranted();
            } else {
                mMainView.requestUsbPermission(mHantekUsbManager.getUsbDevice());
            }
        } else {
            mMainView.askToLoadDemoDevice();
        }
    }

    @Override
    public void handleUsbPermissionGranted() {
        if (mMainView.isAskDemoDialogShowing()) {
            mMainView.closeAskDemoDialog();
        }
        boolean success = mHantekUsbManager.setDevice();
        if (success)
            loadRealDevice();
    }

    @Override
    public void handleNotLoadDemoDevice() {

    }

    @Override
    public void loadDemoDevice(int[] colors, Object scopeView) {
        assert colors != null;
        assert scopeView != null;

        mHantekUsbManager.loadDemoDevice(mHtDeviceListener, colors, scopeView);
    }

    //endregion MainContract.Presenter

    private void loadRealDevice() {
        int[] colors = mMainView.getChannelColors();
        Object scopeView = mMainView.getScopeView();

        assert colors != null;
        assert scopeView != null;

        mHantekUsbManager.loadRealDevice(mHtDeviceListener, colors, scopeView);
    }

    // Hantek device listener
    private HantekDeviceListener mHtDeviceListener = new HantekDeviceListener() {
        @Override
        public void onReceivedSamplesData() {
            mMainView.updateScopeView();
        }

        @Override
        public void onScopeSettingsChanged() {

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
    };
}
