package com.openhantek.hantek6000.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.dialogs.SaveDataDialogPresenter;

/**
 * Save channel data dialog, use to select the channel.
 */
public class SaveDataDialog extends DialogFragment implements SaveDataDialogPresenter.View {

    private final SaveDataDialogPresenter mPresenter;

    // The index of the selected item.
    private int mSelectedItem;

    // To handle "OK" button click event.
    private DialogInterface.OnClickListener mYesButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPresenter.saveData(mSelectedItem);
        }
    };

    // To handle single choice radio buttons list click event.
    private DialogInterface.OnClickListener mListClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mSelectedItem = which;
        }
    };

    // Constructor use to create mPresenter.
    public SaveDataDialog() {
        mPresenter = new SaveDataDialogPresenter(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The first item is selected by default.
        mSelectedItem = 0;

        // Create Alert Dialog with Single Choice List.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.save_data_dialog_channel_choose)
                .setPositiveButton(R.string.dialog_yes_button, mYesButtonListener)
                .setSingleChoiceItems(R.array.analog_channel, mSelectedItem, mListClickListener);
        return builder.create();
    }

    @Override
    public void showSaveDataFailMessage() {
        Toast.makeText(getContext(), R.string.save_data_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSaveDataSuccessMessage(String fullName) {
        String msg = getString(R.string.save_data_ok) + fullName;

        // Why not use Toast?
        // Use dialog to let user have enough time to see clearly the saved directory.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg)
                .setPositiveButton(R.string.alert_dialog_ok, null);
        builder.show();
    }
}
