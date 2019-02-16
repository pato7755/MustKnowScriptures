package adapters;

import android.content.Context;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udolgc.mustknowscriptures.MustKnow;
import com.udolgc.mustknowscriptures.R;
import com.udolgc.mustknowscriptures.ScriptureEntity;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dbstuff.DatabaseHandler;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> implements TextToSpeech.OnInitListener {

    private List<ScriptureEntity> list;
    private TextToSpeech textToSpeech;
    private Context context;
    String text;
    String title;
    DatabaseHandler dbHandler;

    public SampleAdapter(Context context) {
        this.context = context;

    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            textToSpeech.setLanguage(Locale.ENGLISH);

            TextToSpeechFunction();

        }
    }

//    @Override
//    public void onInit(int status) {
//
//        if (status == TextToSpeech.SUCCESS) {
//
//
//
//            textToSpeech.setLanguage(Locale.US);
//
//            TextToSpeechFunction(text);
//        }
//
//    }
//    private List<HashMap<String, String>> myList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView frontTextView;
        public final TextView backTextView;
        public final ImageButton textToSpeechButton;
        public final ImageButton addToFavButton;
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

    public SampleAdapter(List<ScriptureEntity> list) {
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        System.out.println("get item count: " + getItemCount());
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cards, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        ScriptureEntity entity = list.get(position);
        if (entity.getScripture().length() <= 140){

        } else if (entity.getScripture().length() > 140 && entity.getScripture().length() < 210){
            TextViewCompat.setTextAppearance(holder.backTextView, android.R.style.TextAppearance_Large);
            holder.backTextView.setTextColor(Color.WHITE);
        } else if (entity.getScripture().length() >= 210 && entity.getScripture().length() < 280){
            TextViewCompat.setTextAppearance(holder.backTextView, android.R.style.TextAppearance_Medium);
            holder.backTextView.setTextColor(Color.WHITE);
        } else if (entity.getScripture().length() >= 280){
            TextViewCompat.setTextAppearance(holder.backTextView, android.R.style.TextAppearance_Small);
            holder.backTextView.setTextColor(Color.WHITE);
        }
        holder.frontTextView.setText(entity.getTitle());
        holder.backTextView.setText(entity.getScripture());

        if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.FRONT_SIDE && list.get(
                position).isFlipped) {
            holder.flipView.setFlipDuration(0);
        } else if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE
                && !list.get(position).isFlipped) {
            holder.flipView.setFlipDuration(0);
            holder.flipView.flipTheView();
        }
        holder.flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isFlipped) {
                    list.get(position).isFlipped = false;
                } else {
                    list.get(position).isFlipped = true;
                }
                holder.flipView.setFlipDuration(700);
                holder.flipView.flipTheView();
            }
        });



        holder.textToSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("back text: " + holder.backTextView.getText().toString());
                text = holder.backTextView.getText().toString();
                textToSpeech = new TextToSpeech(MustKnow.getContext(), SampleAdapter.this);
                TextToSpeechFunction();

            }
        });

        holder.addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.addToFavButton.setBackgroundColor(MustKnow.getContext().getResources().getColor(R.color.gold));
                title = holder.frontTextView.getText().toString();
                dbHandler.addFavScripture(title);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void TextToSpeechFunction() {

//        textToSpeech.setPitch(1.0f);
        textToSpeech.setSpeechRate(0.9f);
//        textToSpeech.setVoice("IVONA");
        HashMap<String, String> onlineSpeech = new HashMap<>();
        onlineSpeech.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true");

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, onlineSpeech);

    }





}