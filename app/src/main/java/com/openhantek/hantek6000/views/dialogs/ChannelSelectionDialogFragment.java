package com.openhantek.hantek6000.views.dialogs;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.openhantek.hantek6000.R;

/**
 *  Channel selection dialog.
 */
public class ChannelSelectionDialogFragment extends DialogFragment implements View.OnClickListener {

    // Channel radio button combo box.
    private RadioGroup mChannelRadioGroup;
    // selected channel.
    private int mSelectChannel = -1;
    // Message for listening to the select channel dialog closed.
    private ChannelSelectionDialogFragmentCloseListener mCloseListener;

    void setCloseListener(ChannelSelectionDialogFragmentCloseListener listener) {
        mCloseListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.ref_save_dialog_layout, container);

       setupUi(view);

        return view;
    }

    // Response button click operation.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ref_save_data_dialog_cancel_button: // Click the cancel button,close dialog.
                this.dismiss();
                break;
            case R.id.ref_save_data_dialog_ok_button: // Click the ok button
                // Need to save the file before closing the dialog.
                saveFile();
                this.dismiss();
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCloseListener != null) {
            mCloseListener.handleDialogClose(mSelectChannel);
        }
    }

    // Setup UI elements.
    private void setupUi(View rootView) {
        rootView.findViewById(R.id.ref_save_data_dialog_cancel_button).setOnClickListener(this);
        rootView.findViewById(R.id.ref_save_data_dialog_ok_button).setOnClickListener(this);

        mChannelRadioGroup = rootView.findViewById(R.id.ref_save_data_dialog_channel_radio_group);
    }

    // Save file,when the ok button is clicked.
    private void saveFile() {
        int radioButtonId = mChannelRadioGroup.getCheckedRadioButtonId();
        if (-1 == radioButtonId) return;

        switch (radioButtonId) {
            case R.id.ref_save_data_dialog_ch1:
                mSelectChannel = 0;
                break;
            case R.id.ref_save_data_dialog_ch2:
                mSelectChannel = 1;
                break;
            case R.id.ref_save_data_dialog_ch3:
                mSelectChannel = 2;
                break;
            case R.id.ref_save_data_dialog_ch4:
                mSelectChannel = 3;
                break;
        }
    }

    /**
     * Listen to the message that the channel selection dialog is closed.
     */
    interface ChannelSelectionDialogFragmentCloseListener {

        /**
         * Called When DialogFragment is Dismissed
         * @param channelIndex The channel index. -1 means not select any channel.
         */
        void handleDialogClose(int channelIndex);
    }
}
