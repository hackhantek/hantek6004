package com.openhantek.hantek6000.views.menus.measure;


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

import com.hantek.ht6000api.HantekSdk;
import com.openhantek.hantek6000.R;

/**
 * A simple {@link Fragment} subclass to show measure menu.
 */
public class MeasureMenuFragment extends Fragment {


    public MeasureMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
    }

    private void setupListView(View rootView) {
        if (getContext() == null) return;

        ListView listView = rootView.findViewById(R.id.measure_menu_list_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.menu_measure));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(mItemClickListener);
    }

    // Handle list view item click event.
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (getActivity() == null) return;
            FragmentManager fm = getActivity().getSupportFragmentManager();
            if (fm == null) return;

            Fragment fragment = null;
            switch (position) {
                case 0: // source
                    fragment = new MeasureSourceMenuFragment();
                    break;
                case 1: // horizontal
                    fragment = new MeasureHorizontalMenuFragment();
                    break;
                case 2: // vertical
                    fragment = new MeasureVerticalMenuFragment();
                    break;
                case 3: // clear
                    HantekSdk.clearAutoMeasureItems();
                    break;
            }

            if (fragment == null) return;

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
}
