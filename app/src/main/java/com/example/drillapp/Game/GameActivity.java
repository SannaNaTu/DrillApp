package com.example.drillapp.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drillapp.R;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MyAppMessage";
    //Info
    Button button1;

    //Game stuff
    final int MAX_NUMBER = 5;
    final Random randomGen = new Random();

    //new stuff
    TextView resultText;
    EditText guessNumber;
    TextView grattisText;
    TextView grattisText2;
    Button checkButton;
    int numberToFind, numberTries;
    Button newGameButton;

    ProgressBar progressBar;
    private int i = 20;

    //new stuff





    Button startAnimation;
    TextView textAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        button1 = findViewById(R.id.infoButton);
        button1.setOnClickListener(this);


        resultText = findViewById(R.id.resultText);
        guessNumber = findViewById(R.id.guessNumber);

        checkButton =findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);

        grattisText =findViewById(R.id.grattisText);
        grattisText2 =findViewById(R.id.grattisText2);

        newGameButton =findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(this);

        //new stuff



        progressBar=findViewById(R.id.progressBar);



        startAnimation =findViewById(R.id.checkButton);
        textAnimation =findViewById(R.id.resultText);
        newGame();

        //new stuff



    }


    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.infoButton:
                // code for button when user clicks buttonOne.
                Log.i(TAG, "it works");

                Toast.makeText(getApplicationContext(), getString(R.string.infoTextPopUp), Toast.LENGTH_LONG).show();
                break;

            case R.id.checkButton:
                validate();
                startAnimation();
//                progress();
                break;

            case R.id.newGameButton:
                newGame();
                break;


            default:
                break;
        }

    }
    private void newGame() {



        numberToFind = randomGen.nextInt(MAX_NUMBER) + 1;
//        resultText.setVisibility(View.VISIBLE);
        resultText.setText(R.string.firstGuess);
        guessNumber.setText("");
        numberTries = 0;
        grattisText.setText("");
        grattisText2.setText("");
        progressBar.setProgress(0);

        //new stuff


//        checkButton.setVisibility(View.VISIBLE);
    }
    private void validate() {
        int n = Integer.parseInt(guessNumber.getText().toString());
        numberTries++;

        if (numberTries == 6 ){
            Toast.makeText(getApplicationContext(), getString(R.string.gameOver), Toast.LENGTH_LONG).show();
            newGame();

        }

        else if (n == numberToFind) {
//            resultText.setVisibility(View.INVISIBLE);
//            checkButton.setVisibility(View.INVISIBLE);
            progressBar.incrementProgressBy(i);
            grattisText.setText(getResources().getString(R.string.grattisText));
            grattisText2.setText(getResources().getString(R.string.Try1)+numberTries+getResources().getString(R.string.Try2));



//                        newGame();

        } else if (n > numberToFind) {
            resultText.setText(R.string.tooHigh);
            progressBar.incrementProgressBy(i);
        } else if (n < numberToFind) {
            resultText.setText(R.string.tooLow);
            progressBar.incrementProgressBy(i);

        }



    }

    private void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        textAnimation.startAnimation(animation);

    }

    //New stuff


}


