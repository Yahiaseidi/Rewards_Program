package com.example.yahia.rewards_program;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EnterOrderAmount extends AppCompatActivity implements View.OnClickListener {

    EditText order_amount;
    Button addPoints;
    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUsersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_order_amount);

        Bundle extras = getIntent().getExtras();
        String cardNumber = extras.getString("card"); //retrieves the variable passed from the previous activity and adds it to cardNumber
        order_amount = (EditText) findViewById(R.id.orderAmount_txt);
        addPoints = (Button) findViewById(R.id.addPoints_btn);
        addPoints.setOnClickListener(EnterOrderAmount.this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(EnterOrderAmount.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(EnterOrderAmount.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(EnterOrderAmount.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(EnterOrderAmount.this, PinView.class);
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
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(EnterOrderAmount.this);

        double orderTotal = Double.parseDouble(order_amount.getText().toString());

            dlgAlert.setMessage("Is $" + orderTotal + " the correct order amount?");
            dlgAlert.setTitle("Confirmation...");

            dlgAlert.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateItem(cardNumber);
                        }
                    });
            dlgAlert.setNegativeButton("Cancel",null);
            dlgAlert.create().show();

    }

    //Retrieves correct customer from data table
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

    //Updates the table with the correct amount of points.
    public void updateItemInTable(Users item) throws ExecutionException, InterruptedException {
        double currentPoints = Integer.parseInt(item.getPoints());
        double addedPoints = Double.parseDouble(order_amount.getText().toString());
        int total = (int) (addedPoints + currentPoints);
        String totalPoints = Integer.toString(total);
        item.setPoints(totalPoints);
        mUsersTable.update(item).get();
        goToMemberAccount(item.getPoints(), item.getCard(), item.getNumber());
}


    //Passes the points and cardNumber of the user to MemberAccount
    public void goToMemberAccount(String points, String card, String number) {
        Intent newActivity = new Intent(getBaseContext(), MemberAccount.class);
        Bundle extras = new Bundle();
        extras.putString("number", number);
        extras.putString("points", points);
        extras.putString("card", card);
        newActivity.putExtras(extras);
        startActivity(newActivity);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}
