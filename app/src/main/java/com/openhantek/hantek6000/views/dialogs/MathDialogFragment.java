package com.openhantek.hantek6000.views.dialogs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.hantek.ht6000api.ht6000.MathOperator;
import com.hantek.ht6000api.ht6000.VoltsPerDivision;
import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.dialogs.MathDialogPresenter;

/**
 * Math channel dialog.
 */
public class MathDialogFragment extends DialogFragment implements MathDialogPresenter.View{

    private MathDialogPresenter mPresenter;
    private CheckBox mOnOffBox; // Switch
    private Spinner mSourceASpinner; // Source A
    private Spinner mSourceBSpinner; // Source B
    private Spinner mVoltsSpinner; // Volts / DIV
    private Spinner mOperatorSpinner; // Operator

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new MathDialogPresenter(this);

        // Request a window without the title.
        // Remove the blank area at the top.
        // Put it in onCreateView() doesn't work.
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.math_dialog, container);

        setupUi(rootView);
        mPresenter.syncWithView();

        return rootView;
    }

    // Setup UI elements
    private void setupUi(View rootView) {
        mOnOffBox = rootView.findViewById(R.id.math_dialog_on_off_checkbox);
        mOnOffBox.setOnCheckedChangeListener(onOnOffBoxChanged);
        // add some padding to the text. 8: 8 pixels.
        mOnOffBox.setPadding(8, 8, 8, 8);

        mSourceASpinner = rootView.findViewById(R.id.math_dialog_source_a_spinner);
        mSourceBSpinner = rootView.findViewById(R.id.math_dialog_source_b_spinner);
        mVoltsSpinner = rootView.findViewById(R.id.math_dialog_voltage_spinner);
        mOperatorSpinner = rootView.findViewById(R.id.math_dialog_operation_spinner);

        mSourceASpinner.setOnItemSelectedListener(mOnSourceASpinnerItemSelected);
        mSourceBSpinner.setOnItemSelectedListener(mOnSourceBSpinnerItemSelected);
        mVoltsSpinner.setOnItemSelectedListener(mOnVoltsSpinnerItemSelected);
        mOperatorSpinner.setOnItemSelectedListener(mOnOperatorSpinnerItemSelected);
    }

    // Respond to the operation of the switch button on or off.
    CompoundButton.OnCheckedChangeListener onOnOffBoxChanged = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mPresenter.setMathEnabled(isChecked);
        }
    };

    // Source a spinner handler.
    private AdapterView.OnItemSelectedListener mOnSourceASpinnerItemSelected =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mPresenter.setMathSourceA(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    // Source b spinner handler.
    private AdapterView.OnItemSelectedListener mOnSourceBSpinnerItemSelected =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mPresenter.setMathSourceB(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    // Volts spinner handler.
    private AdapterView.OnItemSelectedListener mOnVoltsSpinnerItemSelected =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    VoltsPerDivision voltsPerDivision = VoltsPerDivision.values()[position];
                    mPresenter.setMathVoltsPerDiv(voltsPerDivision);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    // Operator spinner handler.
    private AdapterView.OnItemSelectedListener mOnOperatorSpinnerItemSelected =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    MathOperator operator = MathOperator.values()[position];
                    mPresenter.setMathOperator(operator);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    @Override
    public void updateCheckboxStatus(boolean enabled) {
        if (mOnOffBox.isChecked() != enabled) {
            mOnOffBox.setChecked(enabled);
        }
    }

    @Override
    public void updateElementsEnabledStatus(boolean enabled) {
        // these elements enabled status is same, so use mSourceASpinner
        if (mSourceASpinner.isEnabled() != enabled) {
            mSourceASpinner.setEnabled(enabled);
            mSourceBSpinner.setEnabled(enabled);
            mVoltsSpinner.setEnabled(enabled);
            mOperatorSpinner.setEnabled(enabled);
        }
    }

    @Override
    public void updateSourceA(int sourceA) {
        if (mSourceASpinner.getSelectedItemPosition() != sourceA) {
            mSourceASpinner.setSelection(sourceA);
        }
    }

    @Override
    public void updateSourceB(int sourceB) {
        if (mSourceBSpinner.getSelectedItemPosition() != sourceB) {
            mSourceBSpinner.setSelection(sourceB);
        }
    }

    @Override
    public void updateVoltsPerDiv(int mathVoltsIndex) {
        if (mVoltsSpinner.getSelectedItemPosition() != mathVoltsIndex) {
            mVoltsSpinner.setSelection(mathVoltsIndex);
        }
    }

    @Override
    public void updateOperator(int operator) {
        if (mOperatorSpinner.getSelectedItemPosition() != operator) {
            mOperatorSpinner.setSelection(operator);
        }
    }
}
