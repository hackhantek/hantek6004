package com.openhantek.hantek6000.views.menus;

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
import com.openhantek.hantek6000.views.menus.about.AboutMenuFragment;
import com.openhantek.hantek6000.views.menus.cursor.CursorMenuFragment;
import com.openhantek.hantek6000.views.menus.file.FileMenuFragment;
import com.openhantek.hantek6000.views.menus.measure.MeasureMenuFragment;
import com.openhantek.hantek6000.views.menus.tools.ToolsMenuFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {

    public MainMenuFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupListView(view);
    }

    // Setup list view.
    private void setupListView(View rootView) {
        if (getContext() == null) return;

        // Initiate main menu list view.
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.menu_main));
        ListView listView = rootView.findViewById(R.id.mainMenuListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mOnItemClickListener);
    }

    // Handle list view click event. Popup next level menu.
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
    };
}
