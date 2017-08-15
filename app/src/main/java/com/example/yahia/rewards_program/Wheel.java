package com.example.yahia.rewards_program;

import android.support.v7.app.AppCompatActivity;

import java.util.Random;

/**
 * Created by Leslie on 8/13/2017.
 */

public class Wheel extends Thread {

    interface WheelListener {
        void newImage(int img);
    }

    private static int[] imgs = {R.drawable.lemon, R.drawable.diamond, R.drawable.potofgold, R.drawable.cherry,
            R.drawable.seven, R.drawable.bell, R.drawable.bananas, R.drawable.dollar, R.drawable.horseshoe};
    public int currentIndex;
    private WheelListener wheelListener;
    private long frameDuration;
    private long startIn;
    private boolean isStarted;

    public Wheel(WheelListener wheelListener, long frameDuration, long startIn) {
        this.wheelListener = wheelListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        Random rand = new Random();
        int x = rand.nextInt(imgs.length);
        if(imgs.length > x){
            currentIndex = x;
        }
        else {
            currentIndex = 0;
        }
        isStarted = true;
    }

    public void nextImg() {
        currentIndex++;

        if (currentIndex == imgs.length) {
            currentIndex = 0;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }

        while(isStarted) {
            try {
                Thread.sleep(frameDuration);
            } catch (InterruptedException e) {
            }

            nextImg();

            if (wheelListener != null) {
                wheelListener.newImage(imgs[currentIndex]);
            }
        }
    }

    public void stopWheel() {
        isStarted = false;
    }
}