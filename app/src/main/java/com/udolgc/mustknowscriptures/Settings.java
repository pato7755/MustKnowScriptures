package com.udolgc.mustknowscriptures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import adapters.SettingsAdapter;
import models.SettingsModel;

public class Settings extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] languages = new String[]{"English", "French"};
    private List<SettingsModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        initViews();




    }


    private void initViews(){

        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set dividers in recyclerview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        }

        SettingsModel settingsModel = null;

        for (String language : languages) {

            settingsModel = new SettingsModel(language);
            itemList.add(settingsModel);

        }

        SettingsAdapter adapter = new SettingsAdapter(itemList, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


}