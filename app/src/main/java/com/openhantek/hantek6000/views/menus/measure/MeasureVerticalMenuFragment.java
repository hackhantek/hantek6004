package com.openhantek.hantek6000.views.menus.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.presenters.menus.measure.MeasureVerticalMenuPresenter;

/**
 * A simple {@link ListFragment} subclass to show vertical measure items.
 */
public class MeasureVerticalMenuFragment extends ListFragment {

    private MeasureVerticalMenuPresenter mPresenter;

    public MeasureVerticalMenuFragment() {
        mPresenter = new MeasureVerticalMenuPresenter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) return;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.vertical_measure_menu_content, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mPresenter.addMeasure(position);
    }
}
