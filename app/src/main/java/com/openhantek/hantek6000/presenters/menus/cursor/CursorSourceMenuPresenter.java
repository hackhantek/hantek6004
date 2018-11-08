package com.openhantek.hantek6000.presenters.menus.cursor;

import android.util.Log;

import com.hantek.ht6000api.HantekSdk;

// Implement cursor source menu business logic.
public class CursorSourceMenuPresenter {

    private View mView;

    public CursorSourceMenuPresenter(View view) {
        mView = view;
    }

    /**
     * Set cursor measure source channel
     * @param chIndex channel index. 0:CH1...3:CH4
     */
    public void setCursorSource(int chIndex) {
        HantekSdk.setCursorSource(chIndex);
    }

    /**
     * Synchronize view state with model state.
     */
    public void syncWithView() {
        /*Source*/
        int chIndex = HantekSdk.getCursorSource();
        if (mView.getSelectedChannel() != chIndex) {
            mView.setSelectedChannel(chIndex);
        }
    }

    /**
     * Defines the method that concrete {@link View} class should implement.
     */
    public interface View{

        /**
         * Get current selected channel in UI.
         * @return 0:CH1...3:CH4
         */
        int getSelectedChannel();

        /**
         * Set selected channel in UI.
         * @param chIndex 0:CH1...3:CH4
         */
        void setSelectedChannel(int chIndex);
    }
}
