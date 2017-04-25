package com.example.johnny.javiprototype1;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

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
        //GridViewAdapter GridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, getData());
        GridSearchAdapter GridAdapter = new GridSearchAdapter(getContext(), R.layout.grid_item_layout, getData());
        mGridView.setAdapter(GridAdapter);

        mGridView.setTextFilterEnabled(true);
        setupSearchView();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("title", item.getTitle());

                // intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
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
            mGridView.clearTextFilter();
        } else {
            mGridView.setFilterText(newText.toString());
        }
        return true;
    }
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;

    }
}
