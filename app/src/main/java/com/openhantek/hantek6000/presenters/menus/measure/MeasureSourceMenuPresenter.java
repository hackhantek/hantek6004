package com.openhantek.hantek6000.presenters.menus.measure;

import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

public class MeasureSourceMenuPresenter {
    private MainDataSource mDataSource;
    private MeasureSourceMenuPresenter.View mView;

    public MeasureSourceMenuPresenter(MeasureSourceMenuPresenter.View view) {
        mDataSource = MainRepository.getInstance();
        mView = view;
    }

    // Set auto measurement source.
    public void setMeasureSource(int chIndex) {
        if (chIndex < 0) return;
        mDataSource.setAutoMeasureSource(chIndex);
        syncWithView();
    }

    // Sync view states with model.
    public void syncWithView() {
        mView.updateMeasureSource(mDataSource.getAutoMeasureSource());
    }

    /**
     * Defines methods that View should implements.
     */
    public interface View {
        void updateMeasureSource(int chIndex);
    }
}
