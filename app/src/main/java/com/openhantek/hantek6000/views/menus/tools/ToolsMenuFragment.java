package com.openhantek.hantek6000.views.menus.tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.tools.ToolsMenuPresenter;
import com.openhantek.hantek6000.views.dialogs.MathDialogFragment;
import com.openhantek.hantek6000.views.dialogs.RefDialogFragment;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * A simple {@link ListFragment} subclass to show tools menu.
 */
public class ToolsMenuFragment extends ListFragment implements ToolsMenuPresenter.View {

    /**
     * A {@link ToolsMenuPresenter} to implement business logic of this view.
     */
    private final ToolsMenuPresenter mPresenter;
    // Calibrating dialog. Show when calibrating.
    private AlertDialog mAlertDialog;

    public ToolsMenuFragment() {
        // Required empty public constructor
        mPresenter = new ToolsMenuPresenter(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) return;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.tools_menu_content, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0) {
            mPresenter.handleFactorySetupClick();
        } else if (position == 1) {
            mPresenter.handleSelfCaliClick();
        } else if (position == 2) {
            mPresenter.handleRefClick();
        } else if (position == 3) {
            mPresenter.handleMathClick();
        }
    }

    @Override
    public void askWhetherToFactorySetup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.factory_setup_tip)
                .setPositiveButton(R.string.factory_setup_yes, factoryDialogListener)
                .setNegativeButton(R.string.factory_setup_no, factoryDialogListener)
                .create()
                .show();
    }

    @Override
    public void askSelfCalibration() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.self_cali_tip)
                .setPositiveButton(R.string.self_cali_yes, askDialogListener)
                .setNegativeButton(R.string.self_cali_no, askDialogListener)
                .create()
                .show();
    }

    @Override
    public void showRefDialog(){
        if (getActivity() == null) return;

        RefDialogFragment dialog = new RefDialogFragment();
        dialog.show(getActivity().getSupportFragmentManager(), "ref_dialog");
    }

    @Override
    public void showMathDialog() {
        if (getActivity() == null) return;

        MathDialogFragment dialog = new MathDialogFragment();
        dialog.show(getActivity().getSupportFragmentManager(), "math_dialog");
    }

    @Override
    public void promptRealDevice() {
        Toast.makeText(getActivity(), R.string.self_cali_real_device, Toast.LENGTH_LONG).show();
    }

    @Override
    public void closeCalibratingDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    // handle ask factory setup dialog click event.
    private DialogInterface.OnClickListener factoryDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which ==  BUTTON_POSITIVE) {
                mPresenter.resetToFactory();
            } else if (which == BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    };

    // handle ask self calibration dialog event.
    private DialogInterface.OnClickListener askDialogListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == BUTTON_POSITIVE) {
                showCalibratingDialog();
                mPresenter.startSelfCalibration();
            } else if (which == BUTTON_NEGATIVE) {
                dialog.dismiss();
            }

        }
    };

    // handle self calibration performing dialog event.
    private DialogInterface.OnClickListener calibratingDialogListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == BUTTON_NEGATIVE) {
                dialog.dismiss();
                mAlertDialog = null;
                mPresenter.stopSelfCalibration();
            }

        }
    };

    // Show a dialog, tell user self calibration is performing.
    private void showCalibratingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mAlertDialog = builder.setMessage(R.string.self_cali_performing)
                .setNegativeButton(R.string.self_cali_stop, calibratingDialogListener)
                .setCancelable(false)
                .create();
        mAlertDialog.show();
    }
}
