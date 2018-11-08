package com.openhantek.hantek6000.presenters.menus.about;

import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

/**
 * Implement about menu business logic.
 */
public class AboutMenuPresenter {

    private static final String NO_VERSION_TEXT = "----";
    private final MainDataSource mDataSource;
    private final View mView;

    public AboutMenuPresenter(View view) {
        mDataSource = MainRepository.getInstance();
        mView = view;
    }

    // Use this method sync UI, not sync by get method in View, because in this method
    // you can add business logic. For example: handle return null.
    public void syncWithView() {
        String appVersion = mDataSource.getAppVersion();
        if (appVersion != null) {
            mView.updateAppVersion(appVersion);
        } else {
            mView.updateAppVersion(NO_VERSION_TEXT);
        }

        String fpgaVersion = mDataSource.getFpgaVersion();
        if (fpgaVersion != null) {
            mView.updateFpgaVersion(fpgaVersion);
        } else {
            mView.updateFpgaVersion(NO_VERSION_TEXT);
        }

        String driverVersion = mDataSource.getDriverVersion();
        if (driverVersion != null) {
            mView.updateDriverVersion(driverVersion);
        } else {
            mView.updateDriverVersion(NO_VERSION_TEXT);
        }

        String sn = mDataSource.getProductSn();
        if (sn != null) {
            mView.updateProductSn(sn);
        } else {
            mView.updateProductSn(NO_VERSION_TEXT);
        }
    }

    public interface View {

        /**
         * The {@link View} should update APP version when this method is called.
         * @param appVersion APP version. If failed to get version, ite will be "----".
         */
        void updateAppVersion(String appVersion);

        /**
         * The {@link View} should update FPGA version when this method is called.
         * @param fpgaVersion FPGA firmware version. If failed to get version, ite will be "----".
         */
        void updateFpgaVersion(String fpgaVersion);

        /**
         * The {@link View} should update Driver version when this method is called.
         * @param driverVersion ARM firmware version. If failed to get version, ite will be "----".
         */
        void updateDriverVersion(String driverVersion);

        /**
         * The {@link View} should update product serial number when this method is called.
         * @param sn product serial number. If failed to get version, ite will be "----".
         */
        void updateProductSn(String sn);
    }
}
