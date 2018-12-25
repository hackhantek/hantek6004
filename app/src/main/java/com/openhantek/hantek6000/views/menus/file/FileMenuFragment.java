package com.openhantek.hantek6000.views.menus.file;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.file.FileMenuPresenter;
import com.openhantek.hantek6000.views.Injection;
import com.openhantek.hantek6000.views.PermissionHandler;
import com.openhantek.hantek6000.views.dialogs.LoadDataDialog;
import com.openhantek.hantek6000.views.dialogs.SaveDataDialog;

import static com.openhantek.hantek6000.views.PermissionHandler.REQUEST_CODE_EXTERNAL_STORAGE;

/**
 * A simple {@link ListFragment} subclass to show file menu.
 */
public class FileMenuFragment extends ListFragment implements FileMenuPresenter.View {

    // Use to handle business logic.
    private final FileMenuPresenter mPresenter;
    private PermissionHandler mPermissionHandler;

    public FileMenuFragment() {
        // Required empty public constructor
        mPresenter = new FileMenuPresenter(this);
        mPermissionHandler = Injection.providePermissionHandler();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) return;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.file_menu_content, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (getActivity() == null) return;
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager == null) return;

        switch (position) {
            case 0: // Save data
            {
                mPresenter.checkPermission();
                // Show save data dialog
                SaveDataDialog dialog = new SaveDataDialog();
                dialog.show(manager, "save_data_dialog");
            }
            break;
            case 1: // Load data
            {
                mPresenter.checkPermission();
                // Show load data dialog
                LoadDataDialog dialog = LoadDataDialog.newInstance("csv");
                dialog.show(manager, "save_data_dialog");
            }
            break;
        }
    }

    @Override
    public boolean checkHasPermissionWriteExternalStorage() {
        return mPermissionHandler.checkHasPermissionWriteExternalStorage((AppCompatActivity)getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void requestPermissionWriteExternalStorage() {
        mPermissionHandler.requestPermissionWriteExternalStorage((AppCompatActivity)getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_EXTERNAL_STORAGE);
    }
}
