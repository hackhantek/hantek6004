package com.openhantek.hantek6000.presenters.menus.measure;

import com.hantek.ht6000api.ht6000.AutomeasureType;
import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

public class MeasureHorizontalMenuPresenter {

    private final MeasureHorizontalMenuPresenter.View mView;
    private final MainDataSource mainDataSource;

    public MeasureHorizontalMenuPresenter(MeasureHorizontalMenuPresenter.View view) {
        mView = view;
        mainDataSource = MainRepository.getInstance();
    }

    /**
     * Add an automeasure item.
     * @param index auto measure item index.
     */
    public void addMeasure(int index) {
        if (index < 0) return;

        // horizontal measure type start with Period
        int startIndex = AutomeasureType.Period.ordinal();
        AutomeasureType type = AutomeasureType.values()[startIndex + index];

        mainDataSource.addAutoMeasure(type);
    }

    public interface View {

    }
}
