package com.example.yahia.rewards_program;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.services.dynamodbv2.model.*;

public class EnterAlternateID extends AppCompatActivity implements View.OnClickListener {

    EditText phone_editText;
    TextView textView5;
    Button phoneSearch_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_alternate_id);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        phoneSearch_btn = (Button)findViewById(R.id.phoneSearch_btn);
        phone_editText = (EditText)findViewById(R.id.phone_editText);
        phoneSearch_btn.setOnClickListener(EnterAlternateID.this);

        //Makes sure the button is not pressed before validation of text
        phone_editText.setError("Invalid Phone #");
        phoneSearch_btn.setClickable(false);

        //Validation for phone text box
        phone_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(phone_editText.getText().length() < 10 || phone_editText.getText().length() > 10)
                {
                    phone_editText.setError("Invalid Phone #");
                    phoneSearch_btn.setClickable(false);
                }
                else
                {
                    phoneSearch_btn.setClickable(true);
                }
            }
        });

        //Calls the helper function to stop basic android animation.
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(EnterAlternateID.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(EnterAlternateID.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(EnterAlternateID.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(EnterAlternateID.this, AdminView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

    }

    public void onClick(View view) {
        if (view == phoneSearch_btn)
        {
//            Runnable runnable = new Runnable() {
//                public void run() {
//
//                    CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                            getApplicationContext(),
//                            "us-east-1:58f4f084-a42c-48ee-beb1-b78e5058e168", // Identity Pool ID
//                            Regions.US_EAST_1 // Region
//                    );
//
//                    try {
//                        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
//                        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
//
//                        String phoneNumber = phone_editText.toString();
//
//                        users findUser = new users();
//                        findUser.setPhone_number(phoneNumber);
//
//                        Condition rangeKeyCondition = new Condition()
//                                .withComparisonOperator(ComparisonOperator.EQ);
//
//                        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//                                .withHashKeyValues(findUser)
//                                .withRangeKeyCondition("phone_number", rangeKeyCondition)
//                                .withConsistentRead(false);
//
//                        PaginatedQueryList<users> result = mapper.query(users.class, queryExpression);
//
//                        String results = result.get(0).toString();
//
//                        Toast.makeText(getBaseContext(), "Your ID is " + results + "!", Toast.LENGTH_SHORT ).show();
//                    }
//                    catch (Exception e) {
//                        System.err.println(e.getMessage());
//                    }
//
//                }
//            };
//
//            new Thread(runnable).start();
            goToMemberAccount();
        }

    }

    public void goToMemberAccount() {
        Intent newActivity = new Intent(this, MemberAccount.class);
        startActivity(newActivity);
    }
}
