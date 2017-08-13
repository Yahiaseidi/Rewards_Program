package com.example.yahia.rewards_program;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yahia on 5/15/2017.
 */

public class AdminView extends AppCompatActivity implements View.OnClickListener {

    private EditText easy;
    private EditText medium;
    private EditText hard;
    private EditText target;
    private Handler handler = new Handler();
    private MobileServiceClient mClient;
    private MobileServiceTable<Rewards> mRewardsTable;
    Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        update_btn = (Button) findViewById(R.id.update_btn);
        easy = (EditText) findViewById(R.id.easy);
        medium = (EditText) findViewById(R.id.medium);
        hard = (EditText) findViewById(R.id.hard);
        target = (EditText) findViewById(R.id.target);
        update_btn.setOnClickListener(AdminView.this);
        //Calls the helper function to stop basic android animation.
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(AdminView.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(AdminView.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(AdminView.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(AdminView.this, AdminView.class);
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

            mRewardsTable = mClient.getTable(Rewards.class);

        } catch (MalformedURLException e) {
            //  createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            //createAndShowDialog(e, "Error");
        }
    }


    public void onClick(View view) {
        queryItems();
    }

    public void queryItems() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            //Runs a query to the database in the background.
            @Override
            protected String doInBackground(Void... voids) {

                String result = "";

                try {
                    List<Rewards> list = mRewardsTable.where().field("id").eq("7EE175A6-E1C5-417A-9746-8F838C1BA620").execute().get();
                    if (list.size() != 0) {
                        {
                            updateItemInTable(list.get(0));
                            result = "success";
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return result;
            }

            //After the query has been completed, this method runs and shows the messages corresponding to the results.

            protected void onPostExecute(String result) {
                if (result.equalsIgnoreCase("success")) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AdminView.this);
                    dlgAlert.setMessage("Winnging total and amounts have been successfully changed!");
                    dlgAlert.setTitle("Success...");
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    //Delays the running of method goToMemberAdded() by 3000 milliseconds
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToAdminMain();
                        }
                    }, 2500);
                }
            }
        };
        task.execute();
    }


    public void goToAdminMain() {
        Intent intent = new Intent(AdminView.this, AdminMain.class);
        startActivity(intent);
    }

    public void updateItemInTable(Rewards item) throws ExecutionException, InterruptedException {
        item.setTotalWinnings(target.getText().toString());
        item.setHighAmount(hard.getText().toString());
        item.setMediumAmount(medium.getText().toString());
        item.setLowAmount(easy.getText().toString());
        mRewardsTable.update(item).get();
    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}

