package com.example.yahia.rewards_program;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Thread thread;
    private Handler handler = new Handler();
    EditText barCode_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Listens for a barcode being scanned
        barCode_editText = (EditText)findViewById(R.id.barCode_editText);
//        barCode_editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//
//                if(keyEvent.getAction() == keyEvent.KEYCODE_ENTER)
//                {
//                    Intent intent10 = new Intent(MainActivity.this, MemberAccount.class);
//                    startActivity(intent10);
//                    return true;
//                }
//
//                return false;
//            }
//        });


        //Calls the helper function to stop basic android animation.
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(MainActivity.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(MainActivity.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(MainActivity.this, AdminView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }

        };



        new Thread(new Runnable(){
            public void run() {
                while(true)
                {
                    try {
                        String x = barCode_editText.getText().toString();
                        if(x.length() > 0) {
                           startActivityFromMainThread();
                            break;
                        }
                            Thread.sleep(500);
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }


    //Takes user into the Add New Member view
    public void goToAddNewMember(View view) {
        Intent newActivity = new Intent(this, AddNewMember.class);
        startActivity(newActivity);
    }

    //Takes user into the Enter Alternate ID view
    public void goToEnterAlternateID(View view) {
        Intent newActivity = new Intent(this, EnterAlternateID.class);
        startActivity(newActivity);
    }

    public void startActivityFromMainThread(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (MainActivity.this, AddNewMember.class);
                startActivity(intent);
            }
        });
    }

}