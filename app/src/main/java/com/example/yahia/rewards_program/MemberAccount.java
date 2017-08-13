package com.example.yahia.rewards_program;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
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

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.id.list;

public class MemberAccount extends AppCompatActivity implements View.OnClickListener {

    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUsersTable;
    private ProgressBar progressBar_points = null;
    TextView point_total;
    TextView phone_number;
    TextView points_needed;
    Button enterOrder_btn;
    Button btn_reward_notification;
    private int winningTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_account);

        enterOrder_btn = (Button)findViewById(R.id.enterOrder_btn);
        enterOrder_btn.setOnClickListener(MemberAccount.this);
        btn_reward_notification = (Button)findViewById(R.id.btn_reward_notification);
        btn_reward_notification.setOnClickListener(MemberAccount.this);
        //winningTotal = 100;
        progressBar_points = (ProgressBar)findViewById(R.id.progressBar_points);
        Bundle extras = getIntent().getExtras();
        String pointTotal = extras.getString("points"); //Number of points in the customers account
        String cardNumber = extras.getString("card"); //Customer's card number
        String phoneNumber = extras.getString("number"); //Customer's phone number
        winningTotal = extras.getInt("winningTotal");
        System.out.println(winningTotal);
        int points = Integer.parseInt(pointTotal);
        int pointsNeeded = pointsNeededForReward(winningTotal, points);
        //Checks the point total. Only after 100 points are available will the button be visible.
        if(points > winningTotal)
        {
            int numberOfRewards = (int)(Math.floor((points / winningTotal)));
            btn_reward_notification.setText("You currently have " + numberOfRewards + " reward(s) available. CLICK TO REDEEM ONE!");
            btn_reward_notification.setVisibility(View.VISIBLE);
        }

        point_total = (TextView)findViewById(R.id.point_total);
        phone_number = (TextView)findViewById(R.id.phoneNumber);
        points_needed = (TextView)findViewById(R.id.points_needed);

        points_needed.setText("Points until next reward: " + pointsNeeded);
        point_total.setText(points + " points");
        phone_number.setText(phoneNumber);

        //Sets progress bar progress
        progressBar_points.setVisibility(View.VISIBLE);
        progressBar_points.setMax(winningTotal);
        progressBar_points.setProgress(points);

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
                        Intent intent4 = new Intent(MemberAccount.this, PinView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        try {
            mClient = new MobileServiceClient(
                    "https://rewards-program.azurewebsites.net",
                    this);

            mUsersTable = mClient.getTable(Users.class);
        } catch (MalformedURLException e) {
            //  createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            //createAndShowDialog(e, "Error");
        }
    }

    public void onClick(View view) {
        Bundle extras = getIntent().getExtras();
        final String cardNumber = extras.getString("card");

        switch (view.getId()) {

            case R.id.enterOrder_btn:
                //Allows the string variable cardNumber to be passed to the goToEnterOrderAmount method on button click
                goToEnterOrderAmount(cardNumber);
                break;

            case R.id.btn_reward_notification:
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MemberAccount.this);

                dlgAlert.setMessage("Would you like to redeem one reward? 100 Points will be deducted from your account!");
                dlgAlert.setTitle("Confirmation...");

                dlgAlert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateItem(cardNumber);

                            }
                        });
                dlgAlert.setNegativeButton("Cancel",null);
                dlgAlert.create().show();
                break;

            default:
                break;
        }
    }

    //Retrieves the correct user from the datatable and calls the updateItemInTable method
    public void updateItem(final String s) {
        if (mClient == null) {
            return;
        }

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    List<Users> list = mUsersTable.where().field("card").eq(s).execute().get();
                    updateItemInTable(list.get(0));
                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);
    }


    //Takes a cardNumber and passes the value to EnterOrderAmount activity
    public void goToEnterOrderAmount(String card) {
        Intent newActivity = new Intent(getBaseContext(), EnterOrderAmount.class);
        Bundle extras = new Bundle();
        extras.putString("card", card);
        newActivity.putExtras(extras);
        startActivity(newActivity);
    }

    //Updates the table with the correct amount of points after the redemption of a reward.
    public void updateItemInTable(Users item) throws ExecutionException, InterruptedException {
        double currentPoints = Integer.parseInt(item.getPoints());
        int pointsAfterRedemption = (int)(currentPoints - winningTotal);
        String newPointTotsl = Integer.toString(pointsAfterRedemption);
        item.setPoints(newPointTotsl);
        mUsersTable.update(item).get();
        goToWinnings();
    }

    //Takes customer to redeem their points.
    public void goToWinnings() {
        Intent newActivity = new Intent(getBaseContext(), Winnings.class);
        startActivity(newActivity);
    }

    //Returns the number of points needed for customer to get another reward
    public int pointsNeededForReward(int rewardIncrement, int customerPoints)
    {
        int pointsNeeded = rewardIncrement - customerPoints;

        if(pointsNeeded < 0)
        {
            pointsNeeded = rewardIncrement - (customerPoints % rewardIncrement);
        }

        return pointsNeeded;
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

}