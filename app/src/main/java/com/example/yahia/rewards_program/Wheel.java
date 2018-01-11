package com.example.yahia.rewards_program;

import java.util.Random;

/**
 * Created by Leslie on 8/13/2017.
 */

public class Wheel extends Thread {

    interface WheelListener {
        void newImage(int img);
    }

    private static int[] imgs = {R.drawable.lemon, R.drawable.diamond, R.drawable.potofgold, R.drawable.cherry,
            R.drawable.seven, R.drawable.bell, R.drawable.bananas, R.drawable.dollar, R.drawable.horseshoe,
            R.drawable.triplesevens, R.drawable.business, R.drawable.casino_tokens, R.drawable.clover, R.drawable.grapes, R.drawable.bar,
            R.drawable.strawberry, R.drawable.melon, R.drawable.spades, R.drawable.clubs, R.drawable.hearts, R.drawable.diamonds};
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

    public int returnImgs(int k){
        for(int i = 0; i<imgs.length; i++ ){
            if(k == i) {
                return imgs[i];
            }
        }
        return -1;
    }

    public void nextImg() {
        Random rand = new Random();
        currentIndex = rand.nextInt(imgs.length);

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