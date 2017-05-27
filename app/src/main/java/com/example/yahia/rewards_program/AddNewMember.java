package com.example.yahia.rewards_program;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.lang.reflect.Field;

public class AddNewMember extends AppCompatActivity implements View.OnClickListener {

    EditText newPhoneNumber;
    EditText cardNumber;
    Button createNewMember_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        newPhoneNumber = (EditText)findViewById(R.id.newPhoneNumber);
        createNewMember_btn = (Button)findViewById(R.id.createNewMember_btn);
        cardNumber = (EditText)findViewById(R.id.enterID_txt);
        createNewMember_btn.setOnClickListener(AddNewMember.this);
        cardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(cardNumber.getText().length() == 0 )
                {
                    cardNumber.setError("Required");
                    createNewMember_btn.setClickable(false);
                }
                else
                {
                    createNewMember_btn.setClickable(true);
                }
            }
        });

        newPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(newPhoneNumber.getText().length() < 10 || newPhoneNumber.getText().length() > 10)
                {
                    newPhoneNumber.setError("Invalid Phone #");
                    createNewMember_btn.setClickable(false);
                }
                else
                {
                    createNewMember_btn.setClickable(true);
                }
            }
        });



        //Calls the helper function to stop basic android animation.
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(AddNewMember.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(AddNewMember.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(AddNewMember.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(AddNewMember.this, AdminView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == createNewMember_btn)
        {
            final String phone = newPhoneNumber.getText().toString();
            final String card_id = cardNumber.getText().toString();
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
                    user.setCard_id(card_id);
                    user.setPhone_number(phone);
                    user.setReward_points("0");
                    mapper.save(user);
                }
            };
            new Thread(runnable).start();

        }

    }

}
