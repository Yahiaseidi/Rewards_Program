package com.example.yahia.rewards_program;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
//    EditText barCode_editText;

    //Declaring connection variables
    Connection con;
    String un, pass, db, ip;
    //End Declaring connection variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Listens for a barcode being scanned
//        barCode_editText = (EditText)findViewById(R.id.barCode_editText);
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

        //Declaring server ip, database, username, and password
        ip = "rewards-program.database.windows.net";
        db = "Rewards Program";
        un = "sammy";
        pass = "Testing1";
        //Finished declaring server ip, database, username, and password

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

    //Ben added this code.  it's probably useless.
    protected String doInBackground(String... params)
    {
        String z;
        boolean isSuccess;
        String phoneNo = ""; //TODO:  FIND A WAY TO PASS THIS THING A PHONE NUMBER, AND CHECK AGAINST IT!
        //if(phoneNo.trim().equals(""))
          //  z = "Please enter Username and Password";
        //else
        {
            try
            {
                con = connectionclass(un, pass, db, ip);        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else
                {
                    // Change below query according to your own database.
                    String query = "select * from users where user_name= '" + phoneNo.toString() + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next())
                    {
                        z = "Login successful";
                        isSuccess=true;
                        con.close();
                    }
                    else
                    {
                        z = "Invalid Credentials!";
                        isSuccess = false;
                    }
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();
            }
        }
        return z;
    }

    //Ben added this function. it's probably garbage.
    public Connection connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + database + ";user=" + user+ ";password=" + password + ";";
            connection = (Connection) DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

}
