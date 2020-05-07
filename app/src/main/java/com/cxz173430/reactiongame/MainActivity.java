package com.cxz173430.reactiongame;
/**
 * This is the main activity for the app, player can go view the rank or start the game
 * @Author:     Churong Zhang
 * @Date:       3/29/2020
 * @Class:      CS 6326
 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    // define UI components
    private Button startButton;
    private Button rankButton;
    private TextView instructionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button)findViewById(R.id.startButton);
        rankButton = (Button) findViewById(R.id.rankButton);
        instructionTextView = (TextView) findViewById(R.id.instructionTextView);
        String instruction = "Please touch 10 of the specify shape " +
                                "\nand color in the shortest amount of " +
                                "\ntime. Shapes will disappear within 3 " +
                                "\nto 8 second. and generate new shapes within " +
                                "\n3 to 8 second. your score is the accumulate " +
                                "\ntime it take for you to click on a correct " +
                                "\nshape after the it appear. Hints: try to " +
                                "\nnot miss any correct shapes";
        instructionTextView.setText(instruction);
        // set up button on click listener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startButtonClick();
            }
        });
        rankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankButtonClick();
            }
        });

    }
    private void startButtonClick()
    {   // start game
        Intent intent = new Intent(this, GameStage.class);
        startActivityForResult(intent, 1, null);
    }
    private void rankButtonClick()
    {   // show ranking page
        Intent intent = new Intent(this, RankScore.class);
        startActivityForResult(intent, 1, null);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        }
    }
}
