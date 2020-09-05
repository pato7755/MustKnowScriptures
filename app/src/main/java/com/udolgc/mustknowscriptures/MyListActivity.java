package com.udolgc.mustknowscriptures;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import adapters.SampleAdapter;

import static com.udolgc.mustknowscriptures.BooksOfTheBible.scriptureByBook;

public class MyListActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private RecyclerView recyclerView;
    List<ScriptureEntity> scriptureList;
    List<ScriptureEntity> myList = new ArrayList<>();
    SampleAdapter myAdapter;
    TextToSpeech textToSpeech;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        scriptureList = scriptureByBook;

        checkFavourites();

        if (scriptureList.isEmpty()) {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        for (ScriptureEntity scriptureEntity : scriptureList) {

            myList.add(new ScriptureEntity(scriptureEntity.getTitle(), scriptureEntity.getScripture(), scriptureEntity.getFavourite()));

        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        myAdapter = new SampleAdapter(myList, MyListActivity.this);
        recyclerView.setAdapter(myAdapter);

        textToSpeech = new TextToSpeech(this, this);

    }


    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.list);
        relativeLayout = findViewById(R.id.no_fav_layout);

    }

    public void checkFavourites() {
        if (scriptureList.isEmpty()) {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            System.out.println("result: " + result);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
//                button.setEnabled(true);
                System.out.println("Language is supported");

                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {

                        final String keyword = s;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getApplicationContext(), "Started" + keyword, Toast.LENGTH_SHORT).show();
                                System.out.println("Started");
                            }
                        });
                    }

                    @Override
                    public void onDone(String s) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_SHORT).show();
                                System.out.println("Done");
                                SampleAdapter.playFlag = false;
                            }
                        });
                    }

                    @Override
                    public void onError(String s) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }

        } else {
            Toast.makeText(getApplicationContext(), "Init failed", Toast.LENGTH_SHORT).show();
            System.out.println("Init failed");
        }

    }

    public void speakOut(String scriptureContent) {

        System.out.println("speak out");

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            System.out.println("above lollipop");
            try {
//                textToSpeech.setPitch(f);
                textToSpeech.setSpeechRate(0.85f);
                textToSpeech.speak(scriptureContent, TextToSpeech.QUEUE_FLUSH, params, "");
            } catch (Exception ex) {
                System.out.println("speak exception: " + ex.getMessage());
            }

        } else {
            System.out.println("below lollipop");
//            textToSpeech.setPitch(f);
            textToSpeech.setSpeechRate(0.85f);
            textToSpeech.speak(scriptureContent, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void stopSpeaking() {

        textToSpeech.stop();

    }


    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


}
