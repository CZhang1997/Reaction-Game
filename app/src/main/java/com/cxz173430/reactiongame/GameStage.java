package com.cxz173430.reactiongame;
/**
 * This class is the activity of the game screen
 * @Author:     Churong Zhang
 * @Date:       3/29/2020
 * @Class:      CS 6326
 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class GameStage extends AppCompatActivity {

    // define UI components
    private GameTableView gameView;
    private TextView gameInstructionView;
    private TextView scoreTextView;
    private Button saveButton;

    private String[] colorString;
    private Random random;
                    // colors         Red,          Orange,     Yellow,     Green,          Blue,       Purple, and White
    private int [] colors = new int[]{Color.RED, 0xFFFFA500, Color.YELLOW, Color.GREEN, Color.BLUE, 0xFF800080, Color.WHITE};

    // the true represent circle, false represent square
    boolean circleSelected;
    // the color selected for this game
    int colorSelected;
    private boolean gameOver; // game over after touch 10 correct shapes
    // result

    private long timeUsed;  // the reaction time of the user
    private long shapeMissed;   // the number of correct shapes that the user missed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_stage);
                    // the string name for each color
        colorString = new String[] {"Red", "Orange", "Yellow", "Green", "Blue", "Purple", "White"};
        // declare the random object
        random = new Random();
        circleSelected = random.nextBoolean();  // define the game is base on circle or square
        colorSelected = random.nextInt(colorString.length); // select a random color

        // link UI components
        gameView = (GameTableView) findViewById(R.id.gameTableView);
        gameInstructionView = (TextView) findViewById(R.id.gameInstructionView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        saveButton = (Button) findViewById(R.id.saveButton);

        // define attribute for the save button
        saveButton.setText("Save");
        saveButton.setEnabled(false);
        // set game over to false
        gameOver = false;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });

        // set up the game with the two random variables
        gameView.setCircleSelected(circleSelected);
        gameView.setColorSelected(colorSelected);

        // set up the instruction for the player
        gameInstructionView.setText("Touch 10 more " + colorString[colorSelected] + (circleSelected ? " Circle" : " Square"));
        gameInstructionView.setTextColor(Color.BLACK);

        // show the user how much time he/she used after touch the last shape
        scoreTextView.setText("Time used-> 00:00");

        // whenever the player touch the game screen, this function is going to be called
        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                boolean done = false;
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    // get the x and y where the player was touched
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    // call the function to remove the shape and ge the result after that shape is removed
                    long[] result = gameView.removeShape(x,y);
                    gameInstructionView.setText("Touch "+ (gameView.shapesToPop - result[0])+" more " + colorString[colorSelected] + (circleSelected ? " Circle" : " Square"));
                    // show the user how much time he/she used in mm:ss formate
                    scoreTextView.setText("Time used-> " + toMMSS(result[1]));

                    // the number of correct shapes has been click is store at position 0
                    if(result[0] == gameView.shapesToPop) // game over when it reach gameView.shapesToPop
                    {
                        // store the result
                        timeUsed = result[1];   // how much time used in result come back in position 1
                        shapeMissed = result[2];    // how much correct shape did not clicked and disappear due to its live time
                        gameOver();
                    }
                    done = true;
                }
                return done;
            }
        });
    }
    public void gameOver()
    {
        // enable the button
        saveButton.setEnabled(true);
        gameOver = true;
        gameInstructionView.setTextColor(Color.RED);
        // tell the player the result and see if he/she want to save the result
        gameInstructionView.setText("Game Over!!! \nYou miss " + shapeMissed + " "+ colorString[colorSelected] + " " + (circleSelected ? " Circle" : " Square"));

    }
    public String toMMSS(long time)
    {
        // convert milisecond to mm:ss format
        int seconds = (int)(time / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String mmss = (minutes < 10 ? "0" + minutes : "" + minutes);
        mmss += ":" + (seconds < 10 ? "0" + seconds : "" + seconds);
        return mmss;

    }
    private void saveButtonClick()
    {
        if(gameOver)
        {
            List<Record> records = Record.readFromFile(this.getFilesDir().toString());
            int maxRecord = 12; // maximun of 12 records on the rank list
            if(records.size() > maxRecord)
            {
                Record last = records.get(maxRecord - 1); // get the last record
                if(last.getScore() < timeUsed)      // if this game's score is larger than the lsat record's score
                {                                   // then this score can not be add to the rank list
                    scoreTextView.setText("Your score is lower than \nthe top " + maxRecord + " score\nCannot save to the rank list");
                    saveButton.setEnabled(false);
                    circleSelected = random.nextBoolean();
                    colorSelected = random.nextInt(colorString.length);
                    gameInstructionView.setText("Click on  any shapes to restart!");
                    gameInstructionView.setTextColor(Color.GRAY);
                    gameView.restart(circleSelected, colorSelected);
                    return;
                }
            }
            // pass the two result data to the add activity to get the player's name
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra("timeUsed", timeUsed);
            intent.putExtra("shapeMissed", shapeMissed);
            startActivityForResult(intent, 1, null);

        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        // exit this activity when it come back from the add activity
        super.onActivityResult(reqCode, resultCode, data);
        finish();
    }
}
