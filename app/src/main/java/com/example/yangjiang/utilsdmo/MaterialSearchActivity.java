package com.example.yangjiang.utilsdmo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * Created by yangc on 2017/5/4.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:  搜索
 */
public class MaterialSearchActivity extends AppCompatActivity  implements MaterialSearchView.OnQueryTextListener{
    public static final String TAG = "MaterialSearchActivity";

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serach_view);
        searchView= (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
