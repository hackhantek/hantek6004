package com.openhantek.hantek6000.views.menus.measure;


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

import  com.openhantek.hantek6000.presenters.menus.measure.MeasureHorizontalMenuPresenter;

/**
 * A simple {@link Fragment} subclass to show horizontal measure items.
 */
public class MeasureHorizontalMenuFragment extends Fragment
        implements MeasureHorizontalMenuPresenter.View {

    private final MeasureHorizontalMenuPresenter mPresenter;

    public MeasureHorizontalMenuFragment() {
        mPresenter = new MeasureHorizontalMenuPresenter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure_horizontal_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
    }

    private void setupListView(View rootView) {
        if (getContext() == null) return;

        ListView listView = rootView.findViewById(R.id.measure_horizontal_list_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.menu_measure_horizontal));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(mItemClickListener);
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPresenter.addMeasure(position);
        }
    };
}
