package com.openhantek.hantek6000.presenters.dialogs;

import com.hantek.ht6000api.Channel;
import com.hantek.ht6000api.MathChannel;
import com.hantek.ht6000api.ht6000.MathOperator;
import com.hantek.ht6000api.ht6000.VoltsPerDivision;
import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

/**
 * Responsible for the business of the math channel dialog.
 */
public class MathDialogPresenter {

    private final MainDataSource mDataSource; // MVP model
    private MathDialogPresenter.View mView; // MVP view

    public MathDialogPresenter(MathDialogPresenter.View view) {
        mView = view;
        mDataSource = MainRepository.getInstance();
    }

    /**
     * Set math channel enabled status.
     * @param enabled math channel enabled status.
     */
    public void setMathEnabled(boolean enabled) {
        Channel mathChannel = mDataSource.getMathChannel();
        if (mathChannel.isEnabled() != enabled) {
            mDataSource.sendSetMathEnabledMessage(enabled);
            syncWithView();
        }
    }

    /**
     * Synchronize the contents of the dialog.
     */
    public void syncWithView() {
        Channel mathChannel = mDataSource.getMathChannel();
        if (mathChannel == null) return;

        mView.updateCheckboxStatus(mathChannel.isEnabled());
        mView.updateElementsEnabledStatus(mathChannel.isEnabled());

        // -1: device not ready, have no meaning.
        int sourceA = mDataSource.getMathSourceAIndex();
        if (-1 != sourceA){
            mView.updateSourceA(sourceA);
        }

        int sourceB = mDataSource.getMathSourceBIndex();
        if (-1 != sourceB) {
            mView.updateSourceB(sourceB);
        }

        mView.updateVoltsPerDiv(mDataSource.getMathVoltsIndex());

        int operator = mDataSource.getMathOperator();
        if (-1 != operator) {
            mView.updateOperator(operator);
        }
    }

    /**
     * Set math channel source A index.
     * @param index 0:CH1...3:CH4
     */
    public void setMathSourceA(int index) {
        mDataSource.setMathSourceAIndex(index);
        mDataSource.sendUpdateScopeViewMathChannelSettingsMessage(mDataSource.getMathChannel());
        syncWithView();
    }

    /**
     * Set math channel source B index.
     * @param index 0:CH1...3:CH4
     */
    public void setMathSourceB(int index) {
        mDataSource.setMathSourceBIndex(index);
        mDataSource.sendUpdateScopeViewMathChannelSettingsMessage(mDataSource.getMathChannel());
        syncWithView();
    }

    /**
     * Set math channel volts per div.
     * @param voltsPerDivision volts per div.
     */
    public void setMathVoltsPerDiv(VoltsPerDivision voltsPerDivision) {
        mDataSource.setMathVoltsPerDiv(voltsPerDivision);
        mDataSource.sendUpdateScopeViewMathChannelSettingsMessage(mDataSource.getMathChannel());
        syncWithView();
    }

    /**
     * Set math operator.
     * @param mathOperator math operator.
     */
    public void setMathOperator(MathOperator mathOperator) {
        MathChannel mathChannel = mDataSource.getMathChannel();
        if (mathChannel.getOperator() != mathOperator) {
            mathChannel.setOperator(mathOperator);
            mDataSource.sendUpdateScopeViewMathChannelSettingsMessage(mathChannel);
            syncWithView();
        }
    }

    /**
     * Methods that MVP REF view should implement.
     */
    public interface View{

        /**
         * Changes the checked state of the check box.
         * @param enabled current math enabled status.
         */
        void updateCheckboxStatus(boolean enabled);

        /**
         * Update the UI element enabled status by math channel enabled state.
         * <p>If the channel is disabled, some UI elements should no be clicked.</p>
         * @param enabled current math channel enabled status.
         */
        void updateElementsEnabledStatus(boolean enabled);

        /**
         * Update source A.
         * @param sourceA source A index. 0:CH1...3:CH4  -1: no meaning
         */
        void updateSourceA(int sourceA);

        /**
         * Update source B.
         * @param sourceB source A index. 0:CH1...3:CH4  -1: no meaning
         */
        void updateSourceB(int sourceB);

        /**
         * Update volts per div.
         * @param mathVoltsIndex volts index.
         */
        void updateVoltsPerDiv(int mathVoltsIndex);

        /**
         * Update operator.
         * @param operator 0:+ 1:- 2:* 3:/
         */
        void updateOperator(int operator);
    }
}
