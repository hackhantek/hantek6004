package com.openhantek.hantek6000.presenters.dialogs;

import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

/**
 * Implement business logic of the save data dialog.
 */
public class SaveDataDialogPresenter {

    private final View mView;
    private final MainDataSource mDataSource;

    public SaveDataDialogPresenter(SaveDataDialogPresenter.View view) {
        mView = view;
        mDataSource = MainRepository.getInstance();
    }

    /**
     * Save channel data.
     * @param chIndex channel index. 0:CH1...3:CH4
     */
    public void saveData(int chIndex) {
        String fullName = mDataSource.saveData(chIndex);
        if (fullName == null) {
            mView.showSaveDataFailMessage();
        } else {
            mView.showSaveDataSuccessMessage(fullName);
        }
    }

    /**
     * Defines method that View(MVP) should implement.
     */
    public interface View {

        /**
         * Show save data failed message.
         */
        void showSaveDataFailMessage();

        /**
         * Show save data success message.
         * @param fullName null: FAILED; the file full path: SUCCESS
         */
        void showSaveDataSuccessMessage(String fullName);
    }
}
