package com.openhantek.hantek6000.views.menus.file;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.views.dialogs.LoadDataDialog;
import com.openhantek.hantek6000.views.dialogs.SaveDataDialog;

/**
 * 文件菜单(File Menu).
 */
public class FileMenuFragment extends Fragment {


    public FileMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
    }

    // Setup list view
    private void setupListView(View rootView) {
        if (getContext() == null) return;

        ListView listView = rootView.findViewById(R.id.file_menu_listview);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.menu_file));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(mItemClickListener);
    }

    // Handle menu click event.
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (getActivity() == null) return;
            FragmentManager manager = getActivity().getSupportFragmentManager();
            if (manager == null) return;

            switch (position) {
                case 0: // Save data
                {
                    // Show save data dialog
                    SaveDataDialog dialog = new SaveDataDialog();
                    dialog.show(manager, "save_data_dialog");
                }
                break;
                case 1: // Load data
                {
                    // Show load data dialog
                    LoadDataDialog dialog = new LoadDataDialog();
                    dialog.show(manager, "save_data_dialog");
                }
                break;
            }

        }
    };
}
