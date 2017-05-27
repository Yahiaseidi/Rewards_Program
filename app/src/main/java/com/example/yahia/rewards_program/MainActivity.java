package com.example.yahia.rewards_program;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Calls the helper function to stop basic android animation.
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
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

        //Start of what I added. Here is what makes a mapper and credintail objects.
        Runnable runnable = new Runnable() {
            public void run() {

                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        getApplicationContext(),
                        "us-east-1:58f4f084-a42c-48ee-beb1-b78e5058e168", // Identity Pool ID
                        Regions.US_EAST_1 // Region
                );

                AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
                //Here you make a new user object and either use my getters or setters to get info and change info as you want.
                users user = new users();
                user.setCard_id("34235234");
                user.setPhone_number("7313455084");
                user.setReward_points("44");
                mapper.save(user);
            }
        };
        new Thread(runnable).start();
        //End of what I added.
    }

    //Takes user into the Enter Alternate ID view
    public void goToEnterAlternateID(View view) {
        Intent newActivity = new Intent(this, EnterAlternateID.class);
        startActivity(newActivity);
    }

    //Takes user into the Add New Member view
    public void goToAddNewMember(View view) {
        Intent newActivity = new Intent(this, AddNewMember.class);
        startActivity(newActivity);
    }




    private void database() {






    }
}
