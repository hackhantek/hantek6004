package com.openhantek.hantek6000.views.menus.about;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.about.AboutMenuPresenter;

/**
 * About menu.
 */
public class AboutMenuFragment extends Fragment implements AboutMenuPresenter.View {


    private final AboutMenuPresenter mPresenter;
    // APP version
    private TextView mAppVersion;
    // FPGA version
    private TextView mFpgaVersion;
    // Scope driver version
    private TextView mDriverVersion;
    // Product serial number
    private TextView mProductSn;

    public AboutMenuFragment() {
        mPresenter = new AboutMenuPresenter(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAppVersion = view.findViewById(R.id.about_menu_app_version_text_view);
        mFpgaVersion = view.findViewById(R.id.about_menu_fpga_version_text_view);
        mDriverVersion = view.findViewById(R.id.about_menu_driver_version_text_view);
        mProductSn = view.findViewById(R.id.about_menu_sn_text_view);

        mPresenter.syncWithView();
    }

    @Override
    public void updateAppVersion(String appVersion) {
        mAppVersion.setText(appVersion);
    }

    @Override
    public void updateFpgaVersion(String fpgaVersion) {
        mFpgaVersion.setText(fpgaVersion);
    }

    @Override
    public void updateDriverVersion(String driverVersion) {
        mDriverVersion.setText(driverVersion);
    }

    @Override
    public void updateProductSn(String sn) {
        mProductSn.setText(sn);
    }
}
