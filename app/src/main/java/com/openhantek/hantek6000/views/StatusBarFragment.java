package com.openhantek.hantek6000.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.StatusBarPresenter;

// 顶部状态栏
public class StatusBarFragment extends Fragment implements StatusBarPresenter.View, View.OnClickListener {
    private StatusBarPresenter mPresenter;
    // channel on/off buttons. channels count is 4.
    private ToggleButton[] mChToggleButtons = new ToggleButton[4];
    private TextView mTimeBase; // TimeBase text view.
    private ToggleButton mRunButton; // run button.
    private Button mAutoButton; // auto button.
    private TextView mTriggerStatus; // trigger status text view;
    private Context mContext;
    private AlertDialog mAutosetDialog; // Autoset dialog.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.status_bar_frag, container, false);

        setupView(root);

        mPresenter = new StatusBarPresenter(this);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void setupView(View rootView) {
        // Fill the channel toggle button array.
        ToggleButton button = rootView.findViewById(R.id.ch1ToggleButton);
        button.setOnClickListener(this);
        mChToggleButtons[0] = button;

        button = rootView.findViewById(R.id.ch2ToggleButton);
        button.setOnClickListener(this);
        mChToggleButtons[1] = button;

        button = rootView.findViewById(R.id.ch3ToggleButton);
        button.setOnClickListener(this);
        mChToggleButtons[2] = button;

        button = rootView.findViewById(R.id.ch4ToggleButton);
        button.setOnClickListener(this);
        mChToggleButtons[3] = button;

        mTimeBase = rootView.findViewById(R.id.statusBarTimeBase);

        mRunButton = rootView.findViewById(R.id.runButton);
        mRunButton.setOnClickListener(this);

        mTriggerStatus = rootView.findViewById(R.id.triggerStatusTextView);

        rootView.findViewById(R.id.runButton).setOnClickListener(this);
        mAutoButton = rootView.findViewById(R.id.autoButton);
        mAutoButton.setOnClickListener(this);
        rootView.findViewById(R.id.menuButton).setOnClickListener(this);
    }

    // Handle click event.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ch1ToggleButton:
                mPresenter.switchChannel(0);
                break;
            case R.id.ch2ToggleButton:
                mPresenter.switchChannel(1);
                break;
            case R.id.ch3ToggleButton:
                mPresenter.switchChannel(2);
                break;
            case R.id.ch4ToggleButton:
                mPresenter.switchChannel(3);
                break;
            case R.id.runButton:
                mPresenter.switchRunStop();
                break;
            case R.id.autoButton:
                mPresenter.handleAutoButtonClick();
                break;
        }
    }

    //region MVP View methods
    @Override
    public void updateChannelEnabledUi(final boolean enabled, final int i) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mChToggleButtons[i].isChecked() != enabled) {
                        mChToggleButtons[i].setChecked(enabled);
                    }
                }
            });
        }
    }

    @Override
    public void updateChannelVoltsDiv(final String voltsDivString, final int i) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToggleButton button = mChToggleButtons[i];
                    if (voltsDivString.compareTo(button.getTextOn().toString()) == 0) return;
                    button.setTextOff(voltsDivString);
                    button.setTextOn(voltsDivString);
                    // It is weird that setTextOff and setTextOn don't update the button.
                    // However setText does that.
                    button.setText(voltsDivString);
                }
            });
        }
    }

    @Override
    public void updateTimeBase(final String timeBase) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mTimeBase.getText().toString().compareTo(timeBase) == 0) return;
                    mTimeBase.setText(timeBase);
                }
            });
        }
    }

    @Override
    public void updateRunButton(final boolean running) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRunButton.isChecked() != running) {
                        mRunButton.setChecked(running);
                    }
                }
            });
        }
    }

    @Override
    public void updateTriggerStatus(final boolean triggered) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (triggered) {
                        mTriggerStatus.setTextColor(getResources().getColor(R.color.status_bar_valid_color));
                        mTriggerStatus.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
                    } else {
                        mTriggerStatus.setTextColor(getResources().getColor(R.color.status_bar_invalid_color));
                        mTriggerStatus.setBackgroundColor(getResources().getColor(R.color.status_bar_invalid_bk_color));
                    }
                }
            });
        }
    }

    @Override
    public void showAutosetDialog() {
        // 显示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        mAutosetDialog = builder.setMessage(getResources().getString(R.string.autoset_wait)).create();
        mAutosetDialog.setCancelable(false);
        mAutosetDialog.show();
    }

    @Override
    public void closeAutosetDialog() {
        if (mAutosetDialog != null) {
            mAutosetDialog.dismiss();
            mAutosetDialog = null;
        }
    }

    @Override
    public void setRunButtonEnabled(boolean enabled) {
        if (mAutoButton.isEnabled() != enabled) {
            mAutoButton.setEnabled(enabled);
        }
    }
    //endregion
}
