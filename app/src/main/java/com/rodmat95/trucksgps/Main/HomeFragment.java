package com.rodmat95.trucksgps.Main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.rodmat95.trucksgps.Controller.PagerController;
import com.rodmat95.trucksgps.R;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tab1,tab2;
    PagerController pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar vistas
        tabLayout = view.findViewById(R.id.tap_layout);
        viewPager = view.findViewById(R.id.view_pager);

        // Inicializar tabItems
        tab1 = view.findViewById(R.id.tab_trucks);
        tab2 = view.findViewById(R.id.tap_drivers);

        // Crear un adaptador para el ViewPager
        pagerAdapter = new PagerController(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        // Configurar eventos de clic
        setupTabLayout();

        return view;
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}