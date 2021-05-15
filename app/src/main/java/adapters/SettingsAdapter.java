package adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillId;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.udolgc.mustknowscriptures.R;

import java.util.List;

import models.SettingsModel;
import utils.UtilityManager;


public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {

    private List<SettingsModel> list;
    private Context context;
    UtilityManager utilityManager = new UtilityManager();
    private int lastSelectedPosition = -1;


    public SettingsAdapter() {
    }

    public SettingsAdapter(List<SettingsModel> list, Context context) {
        this.list = list;
        this.context = context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View view;
        private final TextView languageNameTextView;
        private final ImageView flagImageView;
        private final RadioButton radioButton;


        MyViewHolder(View view) {
            super(view);
            this.view = view;
            languageNameTextView = view.findViewById(R.id.language_name_textview);
            flagImageView = view.findViewById(R.id.flag_imageview);
            radioButton = view.findViewById(R.id.radioButton);

            view.setOnClickListener(this);
//            editButton.setOnClickListener(this);
//            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("recyclerview clicked");

            String selectedLanguage = languageNameTextView.getText().toString();
//            radioButton.setChecked(true);

            System.out.println("selected: " + selectedLanguage);

            lastSelectedPosition = getAdapterPosition();
            notifyDataSetChanged();

            Toast.makeText(context,
                    "You have selected " + languageNameTextView.getText(),
                    Toast.LENGTH_LONG).show();


//            itemCheckChanged(view);

            utilityManager.setPreferences(UtilityManager.LANGUAGE, selectedLanguage);

        }
    }
//
//    private void itemCheckChanged(View v) {
//        selectedPosition = (Integer) v.getTag();
//        System.out.println("selectedPosition: " + selectedPosition);
//        notifyDataSetChanged();
//    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        SettingsModel modelObject = list.get(position);

        holder.languageNameTextView.setText(modelObject.getLanguageName());

        switch (modelObject.getLanguageName()) {
            case "English":
                holder.flagImageView.setImageResource(R.drawable.ic_english);
                break;
            case "French":
                holder.flagImageView.setImageResource(R.drawable.ic_french);
                break;

        }

        holder.radioButton.setChecked(lastSelectedPosition == position);

    }

    public void deselect(){

    }



    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}

//utilities.getSharedPreference(MainApplication.getContext(), Utilities.USER_ID)
