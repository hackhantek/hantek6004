package com.openhantek.hantek6000.views.menus.cursor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.cursor.CursorSourceMenuPresenter;

/**
 * A simple {@link ListFragment} subclass to show cursor source menu.
 */
public class CursorSourceMenuFragment extends ListFragment implements CursorSourceMenuPresenter.View {

    private final CursorSourceMenuPresenter mPresenter;

    public CursorSourceMenuFragment() {
        // Required empty public constructor
        mPresenter = new CursorSourceMenuPresenter(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) return;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.analog_channel, android.R.layout.simple_list_item_single_choice);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Let list view select the current source channel.
        mPresenter.syncWithView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mPresenter.setCursorSource(position);
    }

    @Override
    public int getSelectedChannel() {
        return getListView().getSelectedItemPosition();
    }

    @Override
    public void setSelectedChannel(int chIndex) {
        // Uncheck all items.
        for (int i = 0; i < getListView().getCount(); i++) {
            getListView().setItemChecked(i, false);
        }
        getListView().setItemChecked(chIndex, true);
    }
}
