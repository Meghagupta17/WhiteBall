package com.example.ballgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AnimationArea.ChangeLifeScore {

    TextView tvScore;
    TextView tvLives;
    Button btnStart;
    Button btnPause;
    char startBtnState;
    char pauseBtnState;
    int life;
    int score;
    AnimationArea animationArea;
    String TAG = "lifetag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animationArea = findViewById(R.id.animation);

        tvScore = findViewById(R.id.tvScore);
        tvLives = findViewById(R.id.tvLives);
        btnPause = findViewById(R.id.btnPause);
        btnStart = findViewById(R.id.btnStart);
        startBtnState = 'N';
        pauseBtnState = 'P';
        btnPause.setText("PAUSE");
        btnPause.setEnabled(false);
        btnStart.setText("START");
        score = 0;
        life = 3;
        tvScore.setText("Score: "+ score);
        tvLives.setText("Lives: "+ life);
        animationArea.initializedBlackBall();

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pauseBtnState=='P'){
                    btnPause.setText("RESUME");
                    pauseBtnState = 'R';
                    //To pause animation
                    animationArea.pauseAnimation();
                }else {
                    pauseBtnState='P';
                    btnPause.setText("PAUSE");
                    //To Resume the animation
                    animationArea.startAnimation();
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startBtnState=='N') {
                    btnStart.setText("END");
                    startBtnState = 'S';
                    //To Start the animation
                    animationArea.startAnimation();
                    animationArea.changeStartState();
                    btnPause.setEnabled(true);

                } else if (startBtnState == 'S') {
                    btnStart.setText("NEW");
                    startBtnState = 'E';
                    //To End the new animation
                    animationArea.pauseAnimation();
                    btnPause.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_LONG).show();
                    life = 3;
                    score = 0;

                } else {
                    startBtnState='N';
                    btnStart.setText("START");
                    //Balls should vanished and score =0 , lives = 3
                    animationArea.newGame();
                    score = 0;
                    life = 3;
                    tvScore.setText("Score: "+ score);
                    tvLives.setText("Lives: "+ life);
                    pauseBtnState = 'P';
                    btnPause.setText("PAUSE");
                    btnPause.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void changeLife() {
        Log.d(TAG, "changeLife entered: "+life);
        life--;
        Log.d(TAG, "changed life to: "+life);
        tvLives.setText("Lives: "+ life);

        if(life==0){
            Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_LONG).show();
            animationArea.pauseAnimation();
            startBtnState = 'E';
            pauseBtnState = 'P';
            btnPause.setText("PAUSE");
            btnPause.setEnabled(false);
            btnStart.setText("NEW");
            tvLives.setText("Lives: "+ life);
            animationArea.newGame();
            Log.d(TAG, "changeLife exited: "+life);
        }

    }

    @Override
    public void changeScore(int scoreInc) {
        score = score + scoreInc;
        tvScore.setText("Score: "+ score/2);
    }
}
