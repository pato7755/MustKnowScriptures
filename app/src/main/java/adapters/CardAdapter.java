package adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.udolgc.mustknowscriptures.R;
//import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.HashMap;
import java.util.List;

import utils.EasyFlipView;

public class CardAdapter extends SimpleAdapter {

    //    TextView cardFrontTV;
//    TextView cardBackTV;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    private TextView frontCardTextView;
    private TextView backCardTextView;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    ListView listView;


    //	TextView balLedger;
    //	TextView acc_name;
    //	TextView acc_num;

    public CardAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.in_animation);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

//		textBgImageView = (ImageView) view.findViewById(R.id.text_drawable);
//        cardFrontTV= (TextView) view.findViewById(R.id.frontcard_textview);
//        cardBackTV = (TextView) view.findViewById(R.id.backcard_textview);

        findViews(view);
//        loadAnimations();
//        changeCameraDistance(view);


//        Typeface lato = Typeface.createFromAsset(titleTV.getContext().getAssets(), "Lato-Regular.ttf");
//        Typeface mediumText = Typeface.createFromAsset(titleTV.getContext().getAssets(), "HelveticaMedium.otf");
//        Typeface lightText = Typeface.createFromAsset(titleTV.getContext().getAssets(), "HelveticaLight.otf");
//        Typeface thinText = Typeface.createFromAsset(titleTV.getContext().getAssets(), "HelveticaThin.otf");
//        Typeface regularText = Typeface.createFromAsset(titleTV.getContext().getAssets(), "HelveticaRegular.otf");

//        titleTV.setTypeface(lightText);


        return view;
    }


    private void changeCameraDistance(View view) {
        int distance = 8000;
        float scale = view.getResources().getDisplayMetrics().density * distance;
//        frontCardTextView.setCameraDistance(scale);
//        backCardTextView.setCameraDistance(scale);
    }

    private void findViews(View view) {

//        mCardBackLayout = view.findViewById(R.id.card_back);
//        mCardFrontLayout = view.findViewById(R.id.card_front);
//        frontCardTextView = (TextView) view.findViewById(R.id.frontcard_textview);
//        backCardTextView = (TextView) view.findViewById(R.id.backcard_textview);

        EasyFlipView mYourFlipView = (EasyFlipView) view.findViewById(R.id.flip_view);


        mYourFlipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("flip card");
                View titleTV = (View) view.findViewById(R.id.front);
                View scriptureTV = (View) view.findViewById(R.id.back);

            }
        });


        System.out.println("found views");

//        mCardFrontLayout.sette
    }

//    private void loadAnimations() {
//        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
//        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.in_animation);
//    }

}
