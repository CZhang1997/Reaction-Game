package com.cxz173430.reactiongame;
/**
 * This activity is to show the player the rank of this game
 * @Author:     Churong Zhang
 * @Date:       3/14/2020
 * @Class:      CS 6326
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class RankScore extends AppCompatActivity {

    private final int MAXRECORD = 12;
    List<Record> records;
    ListView recordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_score);

        Intent intent = getIntent();
        Serializable s = intent.getSerializableExtra("record");
        // call the FileIO function to read the data from file
        records = Record.readFromFile(this.getFilesDir().toString());
        Collections.sort(records);
        recordView = (ListView) findViewById(R.id.recordView);
        // assign the adapter such that change as the list of record change
        RecordListAdapter adapter = new RecordListAdapter(this, R.layout.adapter_view_layout, records);
        // set the adapter to the listview
        recordView.setAdapter(adapter);
        if(s != null)
        {
            Record r = (Record)s;
            addAndSave(r);

        }
    }
    private void addAndSave(Record r)
    {
        records.add(r);
//        Collections.sort(records, Collections.<Record>reverseOrder());
        Collections.sort(records);
        // remove the last record if the reach the size of MAXRECORD + 1
        if(records.size() == MAXRECORD + 1)
            records.remove(MAXRECORD);
        save(); // save the data
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // get the serializable object and cast it into Record
            Serializable r = data.getSerializableExtra("record");
            Record re = (Record) r;
            // add the record into the list and sort it
            addAndSave(re);
        }
    }
    private void save()
    {
        String data = "";
        // build the data string
        for(Record r : records)
        {
            data += r.toString();
            data += "\n";
        }
        // call the FileIO function to write to a file
        Record.writeToFile(this.getFilesDir().toString(), data);
    }


}
