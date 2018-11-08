package com.openhantek.hantek6000.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.dialogs.LoadDataDialogPresenter;

/**
 * Load channel data dialog, use to select the loaded file.。
 */
public class LoadDataDialog extends DialogFragment implements LoadDataDialogPresenter.View {

    private final LoadDataDialogPresenter mPresenter;
    // File names.
    private String[] fileNames;
    // The selected item's index.
    private int mSelectedItem;

    // To handle "OK" button click event.
    private DialogInterface.OnClickListener mYesButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPresenter.loadData(fileNames[mSelectedItem]);
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
    public LoadDataDialog() {
        mPresenter = new LoadDataDialogPresenter(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        fileNames = mPresenter.getFiles();
        mSelectedItem = 0;

        // Create Alert Dialog with Single Choice List.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.load_data_dialog_select_file)
                .setPositiveButton(R.string.dialog_yes_button, mYesButtonListener)
                .setSingleChoiceItems(fileNames, mSelectedItem, mListClickListener);
        return builder.create();
    }
}
