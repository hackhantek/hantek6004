package com.openhantek.hantek6000.presenters;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MainContract {

    interface View {

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

        void updateUi();
    }

    interface Presenter {

        void checkDeviceExist(int device_filter);

        void handleNotLoadDemoDevice();

        // Use Object to let presenter independent from any other class thant pure java.
        void loadDemoDevice(int[] colors, Object scopeView);

        void handleUsbPermissionGranted();
    }
}
