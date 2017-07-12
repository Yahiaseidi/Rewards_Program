package com.example.yahia.rewards_program;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.lang.reflect.Member;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MemberAccount extends AppCompatActivity {


    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUsersTable;
    private ProgressBar progressBar_points = null;
    TextView point_total;
    TextView phone_number;
    TextView points_needed;
    Button enterOrder_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_account);

        enterOrder_btn = (Button)findViewById(R.id.enterOrder_btn);

        progressBar_points = (ProgressBar)findViewById(R.id.progressBar_points);
        Bundle extras = getIntent().getExtras();
        String pointTotal = extras.getString("points");
        String cardNumber = extras.getString("card");
        String phoneNumber = extras.getString("number");

        int maxPoints = 100;
        int points = Integer.parseInt(pointTotal);
        int pointsNeeded = maxPoints - points;

        point_total = (TextView)findViewById(R.id.point_total);
        phone_number = (TextView)findViewById(R.id.phoneNumber);
        points_needed = (TextView)findViewById(R.id.points_needed);

        points_needed.setText("Points until next reward: " + pointsNeeded);
        point_total.setText(points + " points");
        phone_number.setText(phoneNumber);

        progressBar_points.setVisibility(View.VISIBLE);
        progressBar_points.setMax(100);
        progressBar_points.setProgress(points);

        setOnClick(enterOrder_btn, cardNumber);
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
                        Intent intent1 = new Intent(MemberAccount.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(MemberAccount.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(MemberAccount.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(MemberAccount.this, AdminView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

    }

    //Allows the string variable cardNumber to be passed to the goToEnterOrderAmount method on button click
    private void setOnClick(final Button btn, final String str){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEnterOrderAmount(str);
            }
        });
    }

    //Takes a cardNumber and passes the value to EnterOrderAmount activity
    public void goToEnterOrderAmount(String card) {
        Intent newActivity = new Intent(getBaseContext(), EnterOrderAmount.class);
        Bundle extras = new Bundle();
        extras.putString("card", card);
        newActivity.putExtras(extras);
        startActivity(newActivity);
    }

}