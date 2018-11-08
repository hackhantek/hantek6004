package com.openhantek.hantek6000.presenters.menus.cursor;

import com.hantek.ht6000api.HantekSdk;
import com.openhantek.hantek6000.views.menus.cursor.CursorMenuFragment;

// Responsible for handling cursor menu business logic.
public class CursorMenuPresenter {
    private View mView;

    public CursorMenuPresenter(View view) {
        mView = view;
    }

    /**
     * Handle cursor measure switch changed event
     * @param isChecked true: user want enable cursor measure
     */
    public void handleCursorEnableSwitchChanged(boolean isChecked) {
        if (HantekSdk.isCursorEnabled() == isChecked) return;

        HantekSdk.setCursorEnabled(isChecked);
    }

    /**
     * Synchronize view state with model state.
     */
    public void syncWithView() {
        /* Sync switch status */
        boolean enabled = HantekSdk.isCursorEnabled();
        if (enabled != mView.getSwitchStatus()) {
            mView.setSwitchStatus(enabled);
        }
    }

    public interface View {
        /**
         * Get switch status.
         * @return true: checked.
         */
        boolean getSwitchStatus();

        /**
         * Set switch status.
         * @param enabled true: checked
         */
        void setSwitchStatus(boolean enabled);
    }
}
