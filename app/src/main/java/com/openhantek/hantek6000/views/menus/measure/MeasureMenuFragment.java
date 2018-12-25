package com.openhantek.hantek6000.views.menus.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hantek.ht6000api.HantekSdk;
import com.openhantek.hantek6000.R;

/**
 * A simple {@link ListFragment} subclass to show measure menu.
 */
public class MeasureMenuFragment extends ListFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) return;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.measure_menu_content, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

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
}
