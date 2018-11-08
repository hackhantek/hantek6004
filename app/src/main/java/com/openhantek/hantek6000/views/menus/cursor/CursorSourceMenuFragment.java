package com.openhantek.hantek6000.views.menus.cursor;


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

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.cursor.CursorSourceMenuPresenter;

/**
 * Cursor source menu.
 */
public class CursorSourceMenuFragment extends Fragment implements CursorSourceMenuPresenter.View {

    private final CursorSourceMenuPresenter mPresenter;
    private ListView mListView;

    public CursorSourceMenuFragment() {
        // Required empty public constructor
        mPresenter = new CursorSourceMenuPresenter(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cursor_source_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
        // Let list view select the current source channel.
        mPresenter.syncWithView();
    }

    // Setup list view.
    private void setupListView(View rootView) {
        if (getContext() == null) return;

        mListView = rootView.findViewById(R.id.cursor_source_menu_list_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_single_choice,
                getResources().getStringArray(R.array.analog_channel));
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(mItemClickListener);
    }

    // Handle list view item click event.
    private AdapterView.OnItemClickListener mItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPresenter.setCursorSource(position);
        }
    };

    @Override
    public int getSelectedChannel() {
        if (mListView == null) return 0;
        return mListView.getSelectedItemPosition();
    }

    @Override
    public void setSelectedChannel(int chIndex) {
        if (mListView == null) return;

        // uncheck all items.
        for (int i = 0; i < mListView.getCount(); i++) {
            mListView.setItemChecked(i, false);
        }
        mListView.setItemChecked(chIndex, true);
    }
}
