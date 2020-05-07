package com.cxz173430.reactiongame;
/**
 * This activity is for player to add a new record to rank
 * @Author:     Churong Zhang
 * @Date:       3/14/2020
 * @Class:      CS 6326
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    // UI Views
    TextView nameText;
    TextView scoreText;
    TextView dateText;
    Button saveButton;

    long timeUsed;
    long shapeMissed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nameText = (TextView) findViewById(R.id.nameText);
        scoreText = (TextView) findViewById(R.id.scoreText);
        dateText = (TextView) findViewById(R.id.dateText);

        Intent intent = getIntent();
        timeUsed = intent.getLongExtra("timeUsed", 0);
        shapeMissed = intent.getLongExtra("shapeMissed", 0);

        // set placeholder for the three text view
        nameText.setHint("(Jack Smit)");
        scoreText.setText("" + timeUsed);
        scoreText.setEnabled(false);
        dateText.setText("" + shapeMissed);
        dateText.setEnabled(false);
        saveButton = (Button) findViewById(R.id.saveButton);
        // called saveClicked() when the button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveClicked();
            }
        });
    }
    public void saveClicked()
    {
        // get the user inputs
        String name = nameText.getText().toString();
        if(name.length() == 0)
        {
            showMessage("Name cannot be empty!");
            return;
        }
        // create a new record;
        Record r = new Record(name, timeUsed, shapeMissed);
        Intent intent = new Intent(this, RankScore.class);
        intent.putExtra("record", r);
        startActivityForResult(intent, 1, null);
    }

    /**
     * @reference: https://stackoverflow.com/questions/33968333/how-to-check-if-a-string-is-date
     * @param inDate    the string to test if valid date
     * @return true if is valid
     */
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        finish();
    }
    // use toast to show a message
    public void showMessage(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
