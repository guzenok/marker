package com.marker.fabel.android_client;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

import com.marker.fabel.android_client.adapters.SheetsAdapter;
import com.marker.fabel.android_client.models.Sheet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton btnCreateSheet;
    private EditText searchText;

    private List<SheetsAdapter> sheetsAdapters;

    @Override
    public void onResume() {
        super.onResume();
        for( SheetsAdapter a : sheetsAdapters ){
            a.refreshData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        sheetsAdapters = new ArrayList<SheetsAdapter>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchText = (EditText) findViewById(R.id.search_text);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager); // setting tab over viewpager

        // Implementing tab selected listener over tablayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition()); // setting current selected item over viewpager
                if( tab.getPosition() == 2 ) {
                    btnCreateSheet.setVisibility(View.GONE);
                    searchText.setVisibility(View.VISIBLE);
                    searchText.requestFocus();
                } else {
                    searchText.setVisibility(View.GONE);
                    btnCreateSheet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnCreateSheet = (FloatingActionButton) findViewById(R.id.btn_new);
        btnCreateSheet.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( App.getUser()==null ){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    // add new sheet
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new:
                Intent intent = new Intent(this, CreateSheetActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void onSheetClick(Sheet s) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("sheet_id", s.getId());
        startActivity(intent);
    }

    // Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        SheetsPagerAdapter adapter = new SheetsPagerAdapter(getSupportFragmentManager());

        addSheetsFragment(adapter, "my", getString(R.string.tab_my));
        addSheetsFragment(adapter, "known", getString(R.string.tab_known));
        addSheetsFragment(adapter, "find", getString(R.string.tab_search));

        viewPager.setAdapter(adapter);
    }

    private void addSheetsFragment(SheetsPagerAdapter adapter, String category, String caption) {
        SheetsFragment frag = new SheetsFragment();
        SheetsAdapter adp = new SheetsAdapter(this, category);
        frag.setAdapter(adp);
        sheetsAdapters.add(adp);
        adapter.addFrag(frag, caption);
        if( category=="find") {
            searchText.addTextChangedListener( adp );
        }
    }

    // View Pager fragments setting adapter class
    class SheetsPagerAdapter extends FragmentPagerAdapter  {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SheetsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        //adding fragments and title method
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
