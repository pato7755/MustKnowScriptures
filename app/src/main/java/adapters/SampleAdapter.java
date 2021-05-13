package adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.udolgc.mustknowscriptures.MustKnow;
import com.udolgc.mustknowscriptures.MyListActivity;
import com.udolgc.mustknowscriptures.R;
import com.udolgc.mustknowscriptures.ScriptureEntity;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dbstuff.DatabaseHandler;
import utils.UtilityManager;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> /*implements TextToSpeech.OnInitListener*/ {

    private List<ScriptureEntity> list;
    private Context context;
    String text;
    String title;
    DatabaseHandler dbHandler;
    public static boolean playFlag = false;
    UtilityManager utilityManager = new UtilityManager();

    public SampleAdapter(Context context) {
        this.context = context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public TextView frontTextView;
        public TextView backTextView;
        public ImageButton textToSpeechButton;
        public ImageButton addToFavButton;
        EasyFlipView flipView;

        MyViewHolder(View view) {
            super(view);
            this.view = view;
            dbHandler = new DatabaseHandler(MustKnow.getContext());
            frontTextView = (TextView) itemView.findViewById(R.id.frontcard_textview);
            backTextView = (TextView) itemView.findViewById(R.id.backcard_textview);
            textToSpeechButton = (ImageButton) itemView.findViewById(R.id.audio_button);
            addToFavButton = (ImageButton) itemView.findViewById(R.id.fav_button);
            flipView = (EasyFlipView) view.findViewById(R.id.flip_view);
        }
    }

    public SampleAdapter(List<ScriptureEntity> list, Context context) {
        this.list = list;
        this.context = context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        System.out.println("get item count: " + getItemCount());
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ScriptureEntity entity = list.get(holder.getAdapterPosition());

        if (entity.getScripture().length() >= 400) {
            System.out.println("entity.getScripture().length(): " + entity.getScripture().length());
            System.out.println("scripture: " + entity.getTitle());
            TextViewCompat.setAutoSizeTextTypeWithDefaults(holder.backTextView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        }
//
//
//        else if (entity.getScripture().length() > 140 && entity.getScripture().length() < 210) {
//            TextViewCompat.setTextAppearance(holder.backTextView, android.R.style.TextAppearance_Large);
//            holder.backTextView.setTextColor(Color.WHITE);
//        } else if (entity.getScripture().length() >= 210 && entity.getScripture().length() < 280) {
//            TextViewCompat.setTextAppearance(holder.backTextView, android.R.style.TextAppearance_Medium);
//            holder.backTextView.setTextColor(Color.WHITE);
//        }
//        else if (entity.getScripture().length() >= 280) {
//            TextViewCompat.setTextAppearance(holder.backTextView, android.R.style.TextAppearance_Small);
//            holder.backTextView.setTextColor(Color.WHITE);
//        }

        holder.frontTextView.setText(entity.getTitle());
        holder.backTextView.setText(entity.getScripture());

        System.out.println("entity.getFavourite(): " + entity.getFavourite());

        if (entity.getFavourite() != null && entity.getFavourite().equals("YES")) {
            holder.addToFavButton.setBackgroundColor(MustKnow.getContext().getResources().getColor(R.color.gold));
        }

        if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.FRONT_SIDE && list.get(
                holder.getAdapterPosition()).isFlipped) {
            holder.flipView.setFlipDuration(0);
        } else if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE
                && !list.get(holder.getAdapterPosition()).isFlipped) {
            holder.flipView.setFlipDuration(0);
            holder.flipView.flipTheView();
        }

        holder.flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(holder.getAdapterPosition()).isFlipped = !list.get(holder.getAdapterPosition()).isFlipped;
                holder.flipView.setFlipDuration(700);
                holder.flipView.flipTheView();
            }
        });


        holder.textToSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (playFlag) {

                    if (context instanceof MyListActivity) {

                        System.out.println("context instanceof MyListActivity");

                        ((MyListActivity) context).stopSpeaking();

                    }

                    holder.textToSpeechButton.setImageDrawable(MustKnow.getContext().getResources().getDrawable(R.drawable.ic_play));
                    playFlag = false;

                } else {

                    System.out.println("back text: " + holder.backTextView.getText().toString());
                    text = holder.backTextView.getText().toString();

                    if (context instanceof MyListActivity) {

                        System.out.println("context instanceof MyListActivity");

                        ((MyListActivity) context).speakOut(text);

                    }

                    holder.textToSpeechButton.setImageDrawable(MustKnow.getContext().getResources().getDrawable(R.drawable.ic_stop));

                    playFlag = true;

                    handler = new Handler();

                    final int delay = 1000; //milliseconds
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //do something
                            if (playFlag) {
                                handler.postDelayed(this, delay);
                            } else {
                                System.out.println("finished speaking...changing image");
                                handler.removeCallbacksAndMessages(runnable);
                                holder.textToSpeechButton.setImageDrawable(MustKnow.getContext().getResources().getDrawable(R.drawable.ic_play));

                            }

                        }
                    }, delay);

                    runnable = new Runnable() {

                        public void run() {

                            if (playFlag) {
                                handler.postDelayed(this, 1000);
                                holder.textToSpeechButton.setImageDrawable(MustKnow.getContext().getResources().getDrawable(R.drawable.ic_stop));
                            }
                        }
                    };

                    handler.postDelayed(runnable, 1000);


                }


            }
        });

        holder.addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (entity.getFavourite() == null || entity.getFavourite().isEmpty()) {

                    holder.addToFavButton.setBackgroundColor(MustKnow.getContext().getResources().getColor(R.color.gold));
                    title = holder.frontTextView.getText().toString();
                    dbHandler.addFavScripture(title, utilityManager.getSharedPreference(UtilityManager.LANGUAGE));

                } else if (entity.getFavourite() != null && entity.getFavourite().equals("YES")) {

                    holder.addToFavButton.setBackgroundColor(MustKnow.getContext().getResources().getColor(R.color.transparent));
                    title = holder.frontTextView.getText().toString();
                    dbHandler.removeFavScripture(title, utilityManager.getSharedPreference(UtilityManager.LANGUAGE));


//                    if (context instanceof MyListActivity) {
//
//                        System.out.println("check if empty");
//
//                        ((MyListActivity) context).checkFavourites();
//
//                    }
//
//                    notifyDataSetChanged();


                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

/*

    public void textToSpeechFunction() {

//        if (playFlag) {
//
//            textToSpeech.stop();
//
//        } else {

//        textToSpeech.setPitch(1.0f);

        if (!playFlag) {

            System.out.println("speaking");
            textToSpeech.stop();
            playFlag = false;

        } else {

            System.out.println("start speaking");
            textToSpeech.setSpeechRate(0.9f);
//        textToSpeech.setVoice("IVONA");
            HashMap<String, String> speechHashMap = new HashMap<>();
            speechHashMap.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true");
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, speechHashMap);
//            isTTSSpeaking();

        }

    }
*/


    Runnable runnable = null;
    Handler handler = null;
    int iterationCount = 0;

//    public void isTTSSpeaking() {
//
//        handler = new Handler();
//
//        runnable = new Runnable() {
//
//            public void run() {
//
////                if (iterationCount < 10) {
//                if (!playFlag) {
////                    handler.postDelayed(this, 1000);
//                    holder.textToSpeechButton.setImageDrawable(MustKnow.getContext().getResources().getDrawable(R.drawable.ic_stop));
//
//                }
//            }
//        };
//
//        handler.postDelayed(runnable, 1000);
//    }

}