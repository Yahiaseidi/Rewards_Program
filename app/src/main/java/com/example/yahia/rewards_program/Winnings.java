package com.example.yahia.rewards_program;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Winnings extends AppCompatActivity {

    private TextView msg;
    private ImageView img1, img2, img3;
    private Wheel wheel1, wheel2, wheel3;
    private Button btn;
    private boolean isStarted;
    private int win, high, medium, low;

    public static final Random RANDOM = new Random();

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winnings);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        btn = (Button) findViewById(R.id.button2);
        msg = (TextView) findViewById(R.id.msg);
        Bundle extras = getIntent().getExtras();
        high = extras.getInt("highAmount");
        medium = extras.getInt("mediumAmount");
        low = extras.getInt("lowAmount");
        win = extras.getInt("winningTotal");
        System.out.println("high: " + high + " " + "medium: " + medium + " low: " + low + " win: " + win);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(false);
        msg.setText("Click for your chance to win big rewards!");

        //*******Lets users know how the game works*****

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Winnings.this);
        dlgAlert.setMessage(Html.fromHtml("<Big>"+"*No matches, small prize!<br />**Two items match, medium prize!<br />***All three match, BIG prize!"+"</Big>"));
//        dlgAlert.setMessage("No matches, small prize!\nTwo items match, medium prize!\nAll three match, BIG prize!");
        dlgAlert.setTitle("Rules...");
        dlgAlert.setNegativeButton("Got it",null);
        dlgAlert.create().show();

        //******************************************

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(Winnings.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(Winnings.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(Winnings.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(Winnings.this, PinView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStarted) {
                    wheel1.stopWheel();
                    wheel2.stopWheel();
                    wheel3.stopWheel();

                    if (wheel1.currentIndex == wheel2.currentIndex && wheel2.currentIndex == wheel3.currentIndex) {
                        msg.setText("You win $" + high + " off your next purchase");
                        //btn.setVisibility(view.INVISIBLE);
                    } else if (wheel1.currentIndex == wheel2.currentIndex || wheel2.currentIndex == wheel3.currentIndex
                            || wheel1.currentIndex == wheel3.currentIndex) {
                        msg.setText("You win $" + medium + " off your next purchase");
                       // btn.setVisibility(view.INVISIBLE);

                    } else {
                        msg.setText("You win $" + low + " off your next purchase");
                        //btn.setVisibility(view.INVISIBLE);

                    }

                    isStarted = false;

                } else {

                    wheel1 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img1.setImageResource(img);
                                }
                            });
                        }
                    }, 90, 0);

                    wheel1.start();

                    wheel2 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img2.setImageResource(img);
                                }
                            });
                        }
                    }, 100, 50);

                    wheel2.start();

                    wheel3 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img3.setImageResource(img);
                                }
                            });
                        }
                    }, 110, 100);

                    wheel3.start();
                    btn.setText("Stop");
                    msg.setText("");
                    isStarted = true;

                }
            }
        });

        MediaPlayer ring= MediaPlayer.create(Winnings.this,R.raw.winning);
        ring.start();
    }
}

