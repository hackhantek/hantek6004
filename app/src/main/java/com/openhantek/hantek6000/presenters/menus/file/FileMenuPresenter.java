package com.openhantek.hantek6000.presenters.menus.file;

/**
 * For handling the business logic of file menu.
 */
public class FileMenuPresenter {
    private View mView;

    public FileMenuPresenter(View view) {
        mView = view;
    }

    /**
     * Check whether permission granted. Yes: do nothing; No: will request permission.
     */
    public void checkPermission() {
        if (!mView.checkHasPermissionWriteExternalStorage()) {
            mView.requestPermissionWriteExternalStorage();
        }
    }

    /**
     * Defines the methods that View should implement.
     */
    public interface View {
        /**
         * Check whether "WRITE_EXTERNAL_STORAGE" is granted.
         * @return true: granted; false: not granted.
         */
        boolean checkHasPermissionWriteExternalStorage();

        /**
         * Request "WRITE_EXTERNAL_STORAGE" permission.
         */
        void requestPermissionWriteExternalStorage();
    }
}
