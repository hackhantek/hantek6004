package com.openhantek.hantek6000.views.menus.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.measure.MeasureSourceMenuPresenter;

/**
 * A simple {@link ListFragment} subclass to show measure source items.
 */
public class MeasureSourceMenuFragment extends ListFragment implements MeasureSourceMenuPresenter.View{

    private MeasureSourceMenuPresenter mPresenter;

    public MeasureSourceMenuFragment() {
        mPresenter = new MeasureSourceMenuPresenter(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) return;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.analog_channel, android.R.layout.simple_list_item_single_choice);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mPresenter.syncWithView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mPresenter.setMeasureSource(position);
    }

    @Override
    public void updateMeasureSource(int chIndex) {
        // Uncheck all items.
        for (int i = 0; i < getListView().getCount(); i++) {
            getListView().setItemChecked(0, false);
        }
        getListView().setItemChecked(chIndex, true);
    }
}
