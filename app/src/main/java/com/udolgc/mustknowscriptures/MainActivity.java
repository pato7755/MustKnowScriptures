package com.udolgc.mustknowscriptures;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    private Animation animation1;
    private Animation animation2;
    private boolean isBackOfCardShowing = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);
        findViewById(R.id.card_layout).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        ((LinearLayout) findViewById(R.id.card_layout)).clearAnimation();
        ((LinearLayout) findViewById(R.id.card_layout)).setAnimation(animation1);
        ((LinearLayout) findViewById(R.id.card_layout)).startAnimation(animation1);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        if (animation == animation1) {
//            if (isBackOfCardShowing) {
//                ((TextView) findViewById(R.id.card_textview)).setText(R.layout.top);
//            } else {
//                ((ImageView) findViewById(R.id.imageView1)).setImageResource(R.drawable.card_back);
//            }
//            ((LinearLayout) findViewById(R.id.card_layout)).clearAnimation();
//            ((LinearLayout) findViewById(R.id.card_layout)).setAnimation(animation2);
//            ((LinearLayout) findViewById(R.id.card_layout)).startAnimation(animation2);
//        } else {
//            isBackOfCardShowing = !isBackOfCardShowing;
//            findViewById(R.id.button1).setEnabled(true);
//        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
    }
}