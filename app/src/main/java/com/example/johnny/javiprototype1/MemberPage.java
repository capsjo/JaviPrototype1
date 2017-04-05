package com.example.johnny.javiprototype1;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MemberPage extends AppCompatActivity {

    private TextView mTextMessage;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    Bundle args;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    fragment = new HomeFragment();
                    //    args.putInt("id", 1);
                    //    fragment.setArguments(args);
                    break;
                case R.id.navigation_search:
                    mTextMessage.setText("Search");
                    fragment = new CardFragment();
                    //    args.putInt("id", 2);
                    //    fragment.setArguments(args);
                    break;
                case R.id.navigation_add:
                    mTextMessage.setText("Add");
                    fragment = new CardFragment();
                    //    args.putInt("id", 3);
                    //    fragment.setArguments(args);
                    break;
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    fragment = new ProfileFragment();
                    //    args.putInt("id", 4);
                    //    fragment.setArguments(args);
                    break;
                case R.id.navigation_setting:
                    mTextMessage.setText("Setting");
                    fragment = new CardFragment();
                    //    args.putInt("id", 5);
                    //    fragment.setArguments(args);
                    break;
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);
        args = new Bundle();

        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
