package com.jwoos.android.sellbook.page1.grouplist;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.BaseActivity;

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;
import butterknife.BindView;


public class Grouplist_Activity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    private int stack_count = 0;
    private FragmentTransaction ft;
    private MenuItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_container);
        setToolbar("중고책 검색");
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initialize_search();



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Grouplist_Fragment())
                    .commit();
        }
    }

    private void initialize_search() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle argruments = new Bundle();
                argruments.putString("keyword", query);

                Grouplist_search_Fragment search = new Grouplist_search_Fragment();
                search.setArguments(argruments);
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                ft.add(R.id.container, search);
                ft.addToBackStack(null);
                ft.commit();
                item.setVisible(false);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewOpened() {
                // Do something once the view is open.
            }

            @Override
            public void onSearchViewClosed() {
                // Do something once the view is closed.
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);

                searchView.setQuery(suggestion, false);
            }
        });
        searchView.adjustTintAlpha(0.8f);
    }


    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        stack_count = 0;

        if (searchView.isOpen()) {
            //서치뷰가 열려있을경우
            searchView.closeSearch();
        } else if (count > 0) {
            //stack에 1개이상 저장되어있을경우 진입
            getSupportActionBar().setTitle("중고책 검색");
            getSupportFragmentManager().popBackStack();
            item.setVisible(true);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.action_search);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                //메뉴아이템 클릭
                searchView.openSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelToast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelToast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //searchView.clearSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}


