package com.openhantek.hantek6000.views.menus.tools;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.tools.ToolsMenuPresenter;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * A simple {@link Fragment} subclass to show tools menu.
 */
public class ToolsMenuFragment extends Fragment implements ToolsMenuPresenter.View {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tools_menu, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
    }

    // Initialize List view.
    private void setupListView(View rootView){
        Context context = getContext();
        if (context == null) return;

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.menu_tools_content));
        ListView listView = rootView.findViewById(R.id.tools_menu_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mItemClickListener);
    }

    // Handle list view click event.
    private AdapterView.OnItemClickListener mItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                mPresenter.handleFactorySetupClick();
            } else if (position == 1) {
                mPresenter.handleSelfCaliClick();
            }
        }
    };

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
