package com.udolgc.mustknowscriptures;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.CardAdapter;

import static com.udolgc.mustknowscriptures.BooksOfTheBible.scriptureByBook;

public class MyListActivity extends AppCompatActivity {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;
//    private TextView frontCardTextView;
//    private TextView backCardTextView;
    private ListView listView;

    String scriptures = null;
    List<ScriptureEntity> scriptureList;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist_activity);
        findViews();
        loadAnimations();
        changeCameraDistance();

        scriptureList = scriptureByBook;

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (ScriptureEntity cn : scriptureList) {

            String log = "Id: " + cn.getId() + " ,TITLE: " + cn.getTitle() + " ,SCRIPTURE: " + cn.getScripture();

//        System.out.println("scripyture list size: "+ scriptureList.size());
//        for (int a = 0; a < scriptureList.size(); a++) {


            HashMap<String, String> hm = new HashMap<String, String>();

            System.out.println(cn.getTitle());
            System.out.println(cn.getScripture());

            hm.put("front", cn.getTitle());
            hm.put("back", cn.getScripture());

            aList.add(hm);

        }

            String[] from = {"front", "back"};
            int[] to = {R.id.frontcard_textview, R.id.backcard_textview};

            populateList(from, to, aList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("flip card");
                    String title = ((TextView) view.findViewById(R.id.frontcard_textview)).getText().toString();
                    String scripture = ((TextView) view.findViewById(R.id.backcard_textview)).getText().toString();
                    System.out.println("title: " + title);
                    System.out.println("scripture: " + scripture);
                    flipCard();
                }
            });


            // Writing Contacts to log
//            Log.d("Name: ", log);




    }


    public void populateList(String from[], int to[], List<HashMap<String, String>> listArray) {

        CardAdapter adapter = new CardAdapter(getBaseContext(), listArray, R.layout.my_activity, from, to);

        listView.setAdapter(adapter);

    }


    private void findViews() {

        listView = (ListView) findViewById(R.id.list);

        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
//        frontCardTextView = findViewById(R.id.frontcard_textview);
//        backCardTextView = findViewById(R.id.backcard_textview);

//        mCardFrontLayout.sette
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        listView.setCameraDistance(scale);
//        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    public void flipCard() {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(((View) findViewById(R.id.card_front)));
            mSetLeftIn.setTarget((View) findViewById(R.id.card_back));
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget((View) findViewById(R.id.card_back));
            mSetLeftIn.setTarget((View) findViewById(R.id.card_front));
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }
}
