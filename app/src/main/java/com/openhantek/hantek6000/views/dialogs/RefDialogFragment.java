package com.openhantek.hantek6000.views.dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.hantek.ht6000api.HtScopeView;
import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.dialogs.RefDialogPresenter;
import com.openhantek.hantek6000.views.Injection;
import com.openhantek.hantek6000.views.MainFragment;
import com.openhantek.hantek6000.views.PermissionHandler;

/**
 * Reference waveform dialog。
 * It is used to load the save reference waveform,set the voltage of the reference
 * waveform,time base,and adjust the position of the reference waveform.
 */
public class RefDialogFragment extends DialogFragment implements View.OnClickListener,
        RefDialogPresenter.View{

    private RefDialogPresenter mPresenter;
    // Used to handle permission-related business
    private PermissionHandler mPermissionHandler;
    // Used to call the getRefViewPortPos method
    private HtScopeView mScopeView;
    // Voltage adjustment
    private Spinner mVoltsSpinner;
    // Time base adjustment
    private Spinner mTimebaseSpinner;
    // Switch check box
    private CheckBox mOnOffBox;
    // Used to control the position of the reference waveform
    private SeekBar mSeekBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new RefDialogPresenter(this);
        mPermissionHandler = Injection.providePermissionHandler();

        mScopeView = MainFragment.getScopeViewAlias();

        // Request a window without the title.
        // Remove the blank area at the top.
        // Put it in onCreateView() doesn't work.
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ref_dialog, container);

        setupUi(rootView);
        mPresenter.syncWithView();

        return rootView;
    }

    // Response button click operation.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ref_dialog_save_button: // Clicked the save button.
                mPresenter.checkPermission();
                showRefSaveDialog();
                break;
            case R.id.ref_dialog_load_button: // Clicked the load button.
                mPresenter.checkPermission();
                showRefLoadDialog();
                break;
        }
    }

    // Setup UI elements.
    private void setupUi(View rootView) {
        rootView.findViewById(R.id.ref_dialog_save_button).setOnClickListener(this);
        rootView.findViewById(R.id.ref_dialog_load_button).setOnClickListener(this);

        mVoltsSpinner = rootView.findViewById(R.id.ref_dialog_voltage_spinner);
        mTimebaseSpinner = rootView.findViewById(R.id.ref_dialog_timebase_spinner);

        mOnOffBox = rootView.findViewById(R.id.ref_dialog_on_off_checkbox);
        mOnOffBox.setOnCheckedChangeListener(onOnOffBoxChanged);

        mVoltsSpinner.setOnItemSelectedListener(mOnItemSelected);
        mTimebaseSpinner.setOnItemSelectedListener(mOnItemSelected);

        mSeekBar = rootView.findViewById(R.id.ref_dialog_seek_bar);
        mSeekBar.setOnSeekBarChangeListener(mOnSeekChangeListener);
    }

    /**
     * Show REF channel selection dialog.
     */
    private void showRefSaveDialog() {
        if (getFragmentManager() == null) return;

        ChannelSelectionDialogFragment dialog = new ChannelSelectionDialogFragment();
        dialog.setCloseListener(new ChannelSelectionDialogFragment.
                ChannelSelectionDialogFragmentCloseListener() {
            @Override
            public void handleDialogClose(int channelIndex) {
                if (channelIndex == -1) return;

                mPresenter.saveRef(channelIndex);
            }
        });
        dialog.show(getFragmentManager(), "ref_save_dialog");
    }

    /**
     * Show reference load dialog.
     */
    private void showRefLoadDialog() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager == null) return;

        // Show load data dialog
        LoadDataDialog dialog = LoadDataDialog.newInstance("rfc");
        dialog.show(manager, "ref_load_dialog");
    }

    /**
     * Response to the operation of the switch button on or off.
     */
    CompoundButton.OnCheckedChangeListener onOnOffBoxChanged = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mPresenter.setRefEnabled(isChecked);
        }
    };

    /**
     * Response to switching voltage or time base operation.
     */
    private AdapterView.OnItemSelectedListener mOnItemSelected =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getId() == mVoltsSpinner.getId()) {
                        mPresenter.changeVoltsDiv(position);
                    } else if (parent.getId() == mTimebaseSpinner.getId()) {
                        mPresenter.changeTimeBase(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    /**
     * Response to changing the position of the scroll bar.
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mPresenter.setRefViewPortPos(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mPresenter.setRefViewPortPos(seekBar.getProgress());
                }
            };

    //region Overloaded method.
    @Override
    public void showSaveDataFailMessage() {
        Toast.makeText(getContext(),R.string.save_data_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSaveDataSuccessMessage(String fullName) {
        String msg = getString(R.string.save_data_ok) + fullName;

        // Why not use Toast?
        // Use dialog to let user have enough time to see clearly the saved directory.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg).setPositiveButton(R.string.alert_dialog_ok, null);
        builder.show();
    }

    @Override
    public boolean checkHasPermissionWriteExternalStorage() {
        return mPermissionHandler.checkHasPermissionWriteExternalStorage(
                (AppCompatActivity) getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void requestPermissionWriteExternalStorage() {
        mPermissionHandler.requestPermissionWriteExternalStorage(
                (AppCompatActivity) getActivity(),
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PermissionHandler.REQUEST_CODE_EXTERNAL_STORAGE);
    }

    @Override
    public void updateVoltsSpinner(int refOriginalVoltsPerDivision) {
        if (refOriginalVoltsPerDivision < 0) return;
        mVoltsSpinner.setSelection(refOriginalVoltsPerDivision);
    }

    @Override
    public void updateTimebaseSpinner(int index) {
        if (index < 0) return;
        mTimebaseSpinner.setSelection(index);
    }

    @Override
    public void updateCheckBoxStatus(boolean enabled) {
        if (mOnOffBox.isChecked() != enabled) {
            mOnOffBox.setChecked(enabled);
        }
    }

    @Override
    public void updateElementsEnabledState(boolean refEnabled) {
        mVoltsSpinner.setEnabled(refEnabled);
        mTimebaseSpinner.setEnabled(refEnabled);
        mOnOffBox.setEnabled(refEnabled);
        mSeekBar.setEnabled(refEnabled);
    }

    @Override
    public int getRefViewPortPos() {
        return mScopeView.getRefViewPortPos();
    }

    @Override
    public void updateScrollBar(int dataLength, int viewPortPos) {
        mSeekBar.setMax(dataLength);
        mSeekBar.setProgress(viewPortPos);
    }
    //endregion Overloaded method.
}
