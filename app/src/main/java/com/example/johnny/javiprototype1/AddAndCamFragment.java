package com.example.johnny.javiprototype1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AddAndCamFragment extends Fragment {

    Fragment fragment;
    PagerAdapter pagerAdapter;

    TabLayout tabLayout;
    ViewPager pager;
    public final int nb = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_cam_fragment, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.add_cam_tab);
        pager = (ViewPager) v.findViewById(R.id.add_cam_pager);

        pagerAdapter = new PagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);

        return v;
    }

    public class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    fragment = new UploadFragment();
                    break;
                case 1:
                    fragment = new HomeFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return nb;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabTitle = "";
            switch (position){
                case 0:
                    tabTitle = "Add from gallery";
                    break;
                case 1:
                    tabTitle = "Take Picture";
                    break;
            }
            return tabTitle;
        }
    }
}
