package com.udolgc.mustknowscriptures;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.SampleAdapter;

import static com.udolgc.mustknowscriptures.BooksOfTheBible.scriptureByBook;

public class MyListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<ScriptureEntity> scriptureList;
    List<ScriptureEntity> myList = new ArrayList<>();
    SampleAdapter myAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViews();

        scriptureList = scriptureByBook;

        for (ScriptureEntity cn : scriptureList) {

            myList.add(new ScriptureEntity(cn.getTitle(), cn.getScripture()));

        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        myAdapter = new SampleAdapter(myList);
        recyclerView.setAdapter(myAdapter);


    }


    private void findViews() {

        recyclerView = (RecyclerView) findViewById(R.id.list);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MyListActivity.this.finish();
                return true;
            case R.id.action_shuffle:
                Collections.shuffle(myList);
                recyclerView.removeAllViews();
                myAdapter.notifyDataSetChanged();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
