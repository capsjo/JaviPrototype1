package com.example.johnny.javiprototype1;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;

import static com.example.johnny.javiprototype1.Cheeses.CHEESES;

/**
 * Created by Johnny on 3/30/2017.
 */

public class GridSearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private SearchView mSearchView;
    private GridView mGridView;
    private final String[] mStrings = CHEESES ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grid_search_fragment, container, false);
        mSearchView= (SearchView)v.findViewById(R.id.search_view);
        mGridView = (GridView) v.findViewById(R.id.grid_view);
        mGridView.setAdapter();

        mListView.setTextFilterEnabled(true);
        setupSearchView();
        return v;
    }
/*
    public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.search_fragment);

        mSearchView= (SearchView) findViewById(R.id.search_view);
        mListView= (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStrings));
        mListView.setTextFilterEnabled(true);
        setupSearchView();
        }

*/

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }
}
