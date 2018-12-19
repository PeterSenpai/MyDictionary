package com.example.petersenpai.mydictionary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {
    private int resourceId;
    WordAdapter(Context context, int textViewResourceId, List<Word> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        Word word_item = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        TextView word_text_view = view.findViewById(R.id.word);
        TextView translation_text_view = view.findViewById(R.id.translation);
        assert word_item != null;
        word_text_view.setText(word_item.getSpell());
        if (word_item.getShow() == true){
            translation_text_view.setText(word_item.getTranslation());
        }else {
            translation_text_view.setText("");
        }
        return view;
    }
}
