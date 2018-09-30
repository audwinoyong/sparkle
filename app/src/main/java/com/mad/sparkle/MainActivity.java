package com.mad.sparkle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.mad.sparkle.dummy.DummyContent;
import com.mad.sparkle.model.Store;

/**
 * @author Audwin
 * Created on 14/09/2018
 *
 */
public class MainActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,
        StoreFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;

    final Fragment mStoreFragment = StoreFragment.newInstance();
    final Fragment mProfileFragment = ProfileFragment.newInstance("", "");
    final FragmentManager mFragmentManager = getSupportFragmentManager();
    Fragment active = mStoreFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    mTextMessage.setText(R.string.map);

                    mFragmentManager.beginTransaction().hide(active).commit();

                    return true;
                case R.id.navigation_list:

                    mFragmentManager.beginTransaction().hide(active).show(mStoreFragment).commit();
                    active = mStoreFragment;
//                    mFragmentManager.beginTransaction().hide(active).commit();

                    return true;
                case R.id.navigation_profile:

                    mFragmentManager.beginTransaction().hide(active).show(mProfileFragment).commit();
                    active = mProfileFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager.beginTransaction().add(R.id.contentContainer, mProfileFragment).hide(mProfileFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.contentContainer, mStoreFragment).commit();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setSelectedItemId(R.id.navigation_list);
//        mTextMessage.setText(R.string.list);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Store store) {
//        Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(mainIntent);
    }
}
