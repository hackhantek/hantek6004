package com.openhantek.hantek6000.views.menus.cursor;


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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.cursor.CursorMenuPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CursorMenuFragment extends Fragment implements CursorMenuPresenter.View {

    private final CursorMenuPresenter mPresenter;
    // Use to switch cursor measure status.
    private Switch mCursorSwitch;

    public CursorMenuFragment() {
        mPresenter = new CursorMenuPresenter(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cursor_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
        mCursorSwitch = view.findViewById(R.id.cursor_switch);
        mCursorSwitch.setOnCheckedChangeListener(mOnSwitchClick);
        mPresenter.syncWithView();
    }

    private void setupListView(View rootView) {
        if (getContext() == null) return;

        ListView listView = rootView.findViewById(R.id.cursor_menu_list_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.menu_cursor));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(mItemClickListener);
    }

    // Handle menu click event.
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (getActivity() == null) return;
            FragmentManager fm = getActivity().getSupportFragmentManager();
            if (fm == null) return;

            Fragment fragment = null;
            switch (position) {
                case 0: // source
                    fragment = new CursorSourceMenuFragment();
                    break;
            }
            if (fragment == null) return;

            // popup sub menu
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_left,
                            R.anim.enter_from_left,
                            R.anim.exit_to_right)
                    .addToBackStack(null)
                    .replace(R.id.menu_fragment_container, fragment)
                    .commit();
        }
    };

    // Called when switch changed.
    private CompoundButton.OnCheckedChangeListener mOnSwitchClick
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mPresenter.handleCursorEnableSwitchChanged(isChecked);
        }
    };

    @Override
    public boolean getSwitchStatus() {
        return mCursorSwitch.isChecked();
    }

    @Override
    public void setSwitchStatus(boolean enabled) {
        mCursorSwitch.setChecked(enabled);
    }
}
