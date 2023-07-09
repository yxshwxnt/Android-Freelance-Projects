package com.example.spaceshooter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {
    private Context context;
    private List<Score> scores;
    public ScoreAdapter(Context context, List<Score> scores) {
        super(context, 0, scores);
        this.context = context;
        this.scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.score_item, parent, false);
        }

        Score score = scores.get(position);

        TextView scoreTextView = view.findViewById(R.id.score_text_view);
        scoreTextView.setText(Integer.toString(score.getScore()));

        TextView dateTextView = view.findViewById(R.id.date_text_view);
        dateTextView.setText(score.getDate());

        return view;
    }

}
