package com.openhantek.hantek6000.presenters.dialogs;

import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

/**
 * To handle load data dialog logic business.
 */
public class LoadDataDialogPresenter {
    private MainDataSource mDataSource;
    private View mView;
    /**
     * Constructor.
     * @param view view
     */
    public LoadDataDialogPresenter(LoadDataDialogPresenter.View view) {
        mView = view;
        mDataSource = MainRepository.getInstance();
    }

    /**
     * Get file list which saves the channel data.
     * @param fileExt the extension of files.
     * @return All files which extension is @fileExt.
     */
    public String[] getFiles(String fileExt) {
        return mDataSource.getFiles(fileExt);
    }

    /**
     * Load file which saved the channel data.
     * @param fileName file name
     * @param fileExt the extension of files.
     */
    public void loadData(String fileName, String fileExt) {
        switch (fileExt) {
            case "csv":
                mDataSource.loadCsvData(fileName);
                break;
            case "rfc":
                mDataSource.loadRfcData(fileName);
                break;
        }
    }

    public interface View {

    }
}
