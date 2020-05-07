package com.cxz173430.reactiongame;
/**
 * This is the adapter view for the rank list
 * @Author:     Churong Zhang
 * @Date:       3/14/2020
 * @Class:      CS 6326
 * @Reference:  https://www.youtube.com/watch?v=E6vE8fqQPTE
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordListAdapter extends ArrayAdapter<Record> {

    private static final String TAG = "RecordListAdapter";
    private Context context;
    int resource;

    public RecordListAdapter(@NonNull Context context, int resource, @NonNull List<Record> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get the display informations
        String name = getItem(position).getName();
        long score = getItem(position).getScore();
        long date = getItem(position).getDate();

        // build the view
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        // find the different display view
        TextView rankView = (TextView) convertView.findViewById(R.id.rankText);
        TextView nameView = (TextView) convertView.findViewById(R.id.nameText1);
        TextView scoreView = (TextView) convertView.findViewById(R.id.scoreText1);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateText1);
        // set the information
        rankView.setText(""+ (position + 1));
        nameView.setText(name);

        scoreView.setText("" + score);
//        DateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
//        String strDate = dateFormat.format(date);
        dateView.setText(date + "");
        // return the row of that view
        return convertView;
    }
}
