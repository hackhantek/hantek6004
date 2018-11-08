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
     * @return All files which extension is csv.
     */
    public String[] getFiles() {
        return mDataSource.getFiles("csv");
    }

    /**
     * Load file which saved the channel data.
     * @param fileName file name
     */
    public void loadData(String fileName) {
        mDataSource.loadData(fileName);
    }


    public interface View {

    }
}
