package com.udolgc.mustknowscriptures;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import adapters.SampleAdapter;
import utils.UtilityManager;

import static com.udolgc.mustknowscriptures.BooksOfTheBible.scriptureByBook;

public class MyListActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private RecyclerView recyclerView;
    List<ScriptureEntity> scriptureList;
    List<ScriptureEntity> myList = new ArrayList<>();
    SampleAdapter myAdapter;
    TextToSpeech textToSpeech;
    RelativeLayout relativeLayout;
    UtilityManager utilityManager = new UtilityManager();
    private static final String TAG = "MyListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        try {
            scriptureList = scriptureByBook;
        } catch (Exception ex) {
            Log.e(TAG, "scriptureList");
            System.out.println(ex.getMessage());
            FirebaseCrashlytics.getInstance().log("scriptureList");
        }

        checkFavourites();


        try {

            for (ScriptureEntity scriptureEntity : scriptureList) {

                myList.add(new ScriptureEntity(scriptureEntity.getTitle(), scriptureEntity.getScripture(), scriptureEntity.getFavourite()));

            }
        } catch (Exception exception) {
            Log.e(TAG, "for loop - adding scriptures in a book to myList");
            FirebaseCrashlytics.getInstance().log("for loop - adding scriptures in a book to myList");
        }

        try {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            myAdapter = new SampleAdapter(myList, MyListActivity.this);
            recyclerView.setAdapter(myAdapter);
        } catch (Exception ex) {
            Log.e(TAG, "setting adapter");
            FirebaseCrashlytics.getInstance().log("setting adapter");
        }


        try {
            new Thread() {
                public void run() {
                    textToSpeech = new TextToSpeech(MyListActivity.this, MyListActivity.this, "com.google.android.tts");
                }
            }.start();

        } catch (Exception ex) {
            Log.e(TAG, "onInit");
            System.out.println(ex.getMessage());
            FirebaseCrashlytics.getInstance().log("onInit");
        }


    }


    private void initViews() {

        recyclerView = findViewById(R.id.list);
        relativeLayout = findViewById(R.id.no_fav_layout);

    }

    public void checkFavourites() {

        try {

            if (scriptureList != null && scriptureList.isEmpty()) {
                relativeLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ex) {
            Log.e(TAG, "checkFavourites");
            System.out.println(ex.getMessage());
            FirebaseCrashlytics.getInstance().log("checkFavourites");
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

            int result = utilityManager.getSharedPreference(UtilityManager.LANGUAGE).equals("English") ? textToSpeech.setLanguage(Locale.US) : textToSpeech.setLanguage(Locale.FRANCE);
            System.out.println("result: " + result);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                runOnUiThread(() -> Toast.makeText(MyListActivity.this, "Language not supported", Toast.LENGTH_SHORT).show());

            } else {
                System.out.println("Language is supported");

                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {

                        final String keyword = s;
                        runOnUiThread(() -> System.out.println("Started"));
                    }

                    @Override
                    public void onDone(String s) {

                        runOnUiThread(() -> {
                            System.out.println("Done");
                            SampleAdapter.playFlag = false;
                        });
                    }

                    @Override
                    public void onError(String s) {

                        runOnUiThread(() -> Toast.makeText(MyListActivity.this, "Error", Toast.LENGTH_SHORT).show());
                    }
                });


            }

        } else {
            runOnUiThread(() -> Toast.makeText(MyListActivity.this, "Text to speech Initialisation failed", Toast.LENGTH_SHORT).show());
            System.out.println("Init failed");
        }

    }


    public void speakOut(String scriptureContent) {

        System.out.println("speak out");

        try {

            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                System.out.println("above lollipop");
                try {
//                textToSpeech.setPitch(f);
                    textToSpeech.setSpeechRate(0.80f);
                    textToSpeech.speak(scriptureContent, TextToSpeech.QUEUE_FLUSH, params, "");
                } catch (Exception ex) {
                    System.out.println("speak exception: " + ex.getMessage());
                }

            } else {
                System.out.println("below lollipop");
//            textToSpeech.setPitch(f);
                textToSpeech.setSpeechRate(0.80f);
                textToSpeech.speak(scriptureContent, TextToSpeech.QUEUE_FLUSH, null);
            }

        } catch (Exception ex) {
            Log.e(TAG, "speakOut");
            System.out.println(ex.getMessage());
            FirebaseCrashlytics.getInstance().log("speakOut");
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
        SampleAdapter.playFlag = false;
//        System.out.println("onDestroy - play flag: " + SampleAdapter.playFlag);
        super.onDestroy();
    }


}
