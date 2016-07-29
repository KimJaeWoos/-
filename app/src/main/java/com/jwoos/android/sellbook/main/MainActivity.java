package com.jwoos.android.sellbook.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BackPressCloseHandler;
import com.jwoos.android.sellbook.base.BaseActivity;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.page1.myinfo.Myinfo_detail_Activity;
import com.jwoos.android.sellbook.utils.ObjectUtils;

import butterknife.BindView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity {

    private BackPressCloseHandler backPressCloseHandler;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initialize_ad();
        initialize_fcm();
        if(Gloval.getUser_nik() != null){
            showToast(Gloval.getUser_nik()+"님 반갑습니다 :)");
        }

        Dlog.d("노티데이터 : "+Gloval.getNoti_book_id());

        if (!ObjectUtils.isEmpty(Gloval.getNoti_book_id())) {
            Intent intent = new Intent(this,Myinfo_detail_Activity.class);
            intent.putExtra("book_id", Gloval.getNoti_book_id());
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    private void initialize_fcm() {
        String token = FirebaseInstanceId.getInstance().getToken();
        ServiceGenerator.getService().set_fcm(token, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Dlog.d("토큰셋팅");
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void initialize_ad() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void initialize() {
        toolbar.setTitle("셀북");
        setSupportActionBar(toolbar);
        backPressCloseHandler = new BackPressCloseHandler(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = Fragment.instantiate(getBaseContext(), MainPage1_Fragment.class.getName());
                    break;
                case 1:
                    fragment = Fragment.instantiate(getBaseContext(), MainPage2_Fragment.class.getName());
                    break;
                case 2:
                    fragment = Fragment.instantiate(getBaseContext(), MainPage3_Fragment.class.getName());
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "판매중";
                case 1:
                    return "책 등록하기";
                case 2:
                    return "설정";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count > 0) {
            getSupportFragmentManager().popBackStack();

        }else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
