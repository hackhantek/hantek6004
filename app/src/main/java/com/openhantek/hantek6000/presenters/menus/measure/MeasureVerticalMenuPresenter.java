package com.openhantek.hantek6000.presenters.menus.measure;

import com.hantek.ht6000api.ht6000.AutomeasureType;
import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

public class MeasureVerticalMenuPresenter {
    private MainDataSource mDataSource;

    public MeasureVerticalMenuPresenter() {
        mDataSource = MainRepository.getInstance();
    }

    /**
     * Add a vertical measurement item.
     * @param index index to {@link AutomeasureType}.
     *              index = 0, is the first of {@link AutomeasureType} which is {@code Max}
     */
    public void addMeasure(int index) {
        if (index < 0) return;

        AutomeasureType type = AutomeasureType.values()[index];
        mDataSource.addAutoMeasure(type);
    }
}
