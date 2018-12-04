package com.udolgc.mustknowscriptures;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static com.udolgc.mustknowscriptures.BooksOfTheBible.scriptureByBook;

public class MyActivity extends AppCompatActivity {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    private TextView frontCardTextView;
    private TextView backCardTextView;
    private ListView list;

    String scriptures = null;
    List<ScriptureEntity> scriptureList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist_activity);
        findViews();
        loadAnimations();
        changeCameraDistance();

        scriptureList = scriptureByBook;


        for (ScriptureEntity cn : scriptureList) {
            String log = "Id: " + cn.getId() + " ,TITLE: " + cn.getTitle() + " ,SCRIPTURE: " + cn.getScripture();
            frontCardTextView.setText(cn.getTitle());
            backCardTextView.setText(cn.getScripture());

            // Writing Contacts to log
            Log.d("Name: ", log);
        }

//        scriptures = getIntent().getExtras().getString("scriptureList");
//        scriptureList = getIntent().getExtras().getString("scriptureList").;

    }

    private void findViews() {
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
        frontCardTextView = findViewById(R.id.frontcard_textview);
        backCardTextView = findViewById(R.id.backcard_textview);

//        mCardFrontLayout.sette
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    public void flipCard(View view) {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }
}
