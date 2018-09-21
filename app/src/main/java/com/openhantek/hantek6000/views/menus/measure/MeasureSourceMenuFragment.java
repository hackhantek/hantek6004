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
import com.openhantek.hantek6000.presenters.menus.measure.MeasureSourceMenuPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureSourceMenuFragment extends Fragment implements MeasureSourceMenuPresenter.View{

    private MeasureSourceMenuPresenter mPresenter;
    private ListView mListView;

    public MeasureSourceMenuFragment() {
        mPresenter = new MeasureSourceMenuPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure_source_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
        mPresenter.syncWithView();
    }

    private void setupListView(View rootView) {
        if (getContext() == null) return;

        mListView = rootView.findViewById(R.id.measure_source_list_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_single_choice,
                getResources().getStringArray(R.array.anlog_channel));
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(mItemClickListener);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPresenter.setMeasureSource(position);
        }
    };

    @Override
    public void updateMeasureSource(int chIndex) {
        // uncheck all items.
        for (int i = 0; i < mListView.getCount(); i++) {
            mListView.setItemChecked(0, false);
        }
        mListView.setItemChecked(chIndex, true);
    }
}
