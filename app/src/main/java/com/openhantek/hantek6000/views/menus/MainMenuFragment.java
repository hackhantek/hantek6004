package com.openhantek.hantek6000.views.menus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.openhantek.hantek6000.R;
import com.openhantek.hantek6000.views.menus.about.AboutMenuFragment;
import com.openhantek.hantek6000.views.menus.cursor.CursorMenuFragment;
import com.openhantek.hantek6000.views.menus.file.FileMenuFragment;
import com.openhantek.hantek6000.views.menus.measure.MeasureMenuFragment;
import com.openhantek.hantek6000.views.menus.tools.ToolsMenuFragment;

/**
 * A simple {@link ListFragment} subclass to show main menu.
 */
public class MainMenuFragment extends ListFragment{

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainMenuFragment.
     */
    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) return;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.main_menu_content, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    // Handle list view click event. Popup next level menu.
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (getActivity() == null) return;
        if (getActivity().getSupportFragmentManager() == null) return;

        Fragment fragment = null;
        if (position == 0) {        // Create file menu
            fragment = new FileMenuFragment();
        } else if (position == 1) { // Create measure menu
            fragment = new MeasureMenuFragment();
        } else if (position == 2) { // Create cursor menu
            fragment = new CursorMenuFragment();
        } else if (position == 3) { // Create tools menu
            fragment = new ToolsMenuFragment();
        } else if (position == 4) { // Create about menu
            fragment = new AboutMenuFragment();
        }

        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                            R.anim.enter_from_left, R.anim.exit_to_right)
                    .addToBackStack(null)
                    .replace(R.id.menu_fragment_container, fragment)
                    .commit();
        }
    }
}
