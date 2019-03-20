package com.example.ballgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class AnimationArea extends View{

    public interface ChangeLifeScore{
        public void changeLife();

        public void changeScore(int score);
    }

    private Paint paint = new Paint();
    List<WhiteBalls> whiteBallsList;
    Balls blackBall = new Balls();
    Context refContext;
    Boolean flagPause;
    double distance;
    Boolean flagStartState;
    ChangeLifeScore changeLifeScore;
    boolean first;
    boolean touched = false;
    String TAG = "MainAct";

    public AnimationArea(Context context) {
        super(context);
        refContext= context;
        flagPause =true;
        flagStartState = false;
        changeLifeScore = (ChangeLifeScore) context;
        whiteBallsList = new ArrayList<>();
        first = true;
    }

    public AnimationArea(Context context, AttributeSet attrs){
        super(context, attrs);
        refContext=context;
        flagPause =true;
        flagStartState = false;
        changeLifeScore = (ChangeLifeScore) context;
        whiteBallsList = new ArrayList<>();
        first = true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(100, 0, 0, 0);
        paint.setColor(Color.BLACK);

        if (first){
            initializedBlackBall();
            first =false;
        }
        canvas.drawCircle(blackBall.x, blackBall.y, blackBall.radius, paint);

        paint.setColor(Color.WHITE);
        //Log.d(TAG, "for loop entered");
        for (WhiteBalls whiteBall : whiteBallsList) {
            if(whiteBall.y<canvas.getHeight()){
                if(!flagPause) {
                    whiteBall.y = whiteBall.y + whiteBall.speed;
                }
            }else{
                whiteBall.y = whiteBall.radius;
                whiteBall.speed =(int)(whiteBall.speed*1.25);
                whiteBall.score++;
                changeLifeScore.changeScore(whiteBall.score);
            }
            canvas.drawCircle(whiteBall.x, whiteBall.y, whiteBall.radius, paint);
            distance = Math.sqrt(((whiteBall.x - blackBall.x) * (whiteBall.x - blackBall.x)) + ((whiteBall.y - blackBall.y) * (whiteBall.y - blackBall.y)));
            if (distance <= (whiteBall.radius + blackBall.radius) && (!touched)) {
                changeLifeScore.changeLife();
                whiteBall.y = whiteBall.radius;
            }

        }

        invalidate();
    }

    void initializedBlackBall(){
        blackBall.x = this.getWidth()/2;
        blackBall.y = this.getHeight()-70;
        blackBall.radius =50;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                break;
            }
        }

        long timeDiff = event.getEventTime()-event.getDownTime();

        if(flagStartState){
            int x = (int) event.getX();
            if (x<this.getWidth()/2){
                blackBall.x=blackBall.x-15;
            }else{
                blackBall.x=blackBall.x+15;
            }

        }else {
            WhiteBalls whiteBalls =new WhiteBalls();
            whiteBalls.x = (int) event.getX();
            whiteBalls.y = (int) event.getY();;
            whiteBalls.radius = (int) timeDiff/30;
            whiteBalls.speed= 4;
            whiteBalls.score=0;
            whiteBallsList.add(whiteBalls);
        }

        return true;
    }

    public void pauseAnimation(){
        flagPause = true;
    }

    public void startAnimation(){
        flagPause = false;
    }

    public void newGame(){
        whiteBallsList = new ArrayList<WhiteBalls>();
        initializedBlackBall();
        flagStartState =false;
        flagPause = true;
    }

    public void changeStartState(){
        flagStartState = true;
    }
}

