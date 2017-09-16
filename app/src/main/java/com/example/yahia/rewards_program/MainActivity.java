package com.example.yahia.rewards_program;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Thread thread;
    private Handler handler = new Handler();
    EditText barCode_editText;
    private MobileServiceClient mClient;
    private MobileServiceTable<Rewards> mRewardsTable;
    private MobileServiceTable<Users> mUsersTable;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        inflater = getLayoutInflater();
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Listens for a barcode being scanned
        barCode_editText = (EditText)findViewById(R.id.barCode_editText);

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
                        Intent intent4 = new Intent(MainActivity.this, PinView.class);
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
                            startActivityFromMainThread(x);
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

        try {
            mClient = new MobileServiceClient(
                    "https://rewards-program.azurewebsites.net",
                    this);


            mUsersTable = mClient.getTable(Users.class);
            mRewardsTable = mClient.getTable(Rewards.class);
        } catch (MalformedURLException e) {
            //  createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            //createAndShowDialog(e, "Error");
        }
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

    public void startActivityFromMainThread(final String x){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    numExists(x);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void goToMemberAccount(String points, String card, String number, int win, int high, int medium, int low) {
        System.out.println("main " + high);
        Intent newActivity = new Intent(getBaseContext(), MemberAccount.class);
        Bundle extras = new Bundle();
        extras.putString("number", number);
        extras.putString("points", points);
        extras.putString("card", card);
        extras.putInt("winningTotal", win);
        extras.putInt("highAmount", high);
        extras.putInt("mediumAmount", medium);
        extras.putInt("lowAmount", low);
        newActivity.putExtras(extras);
        startActivity(newActivity);
    }

    public void numExists(final String s) throws ExecutionException, InterruptedException {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){

            ProgressDialog progDailog = new ProgressDialog(MainActivity.this, R.style.AppCompatAlertDialogStyle);

            @Override
            protected void onPreExecute () {
                progDailog.setMessage("Loading...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(true);
                progDailog.show();
            }

            //Runs a query to the database in the background.
            @Override
            protected String doInBackground(Void... voids) {

                String result = "";

                try {
                    List<Users> list = mUsersTable.where().field("card").eq(s).execute().get();
                    List<Rewards> reward = mRewardsTable.where().field("id").eq("7EE175A6-E1C5-417A-9746-8F838C1BA620").execute().get();
                    if(list.size() == 0) {
                        result = "fail";
                    }
                    else
                    {
                        Users user = list.get(0);
                        goToMemberAccount(user.getPoints(), user.getCard(), user.getNumber(), Integer.parseInt(reward.get(0).getTotalWinnings()),
                                Integer.parseInt(reward.get(0).getHighAmount()), Integer.parseInt(reward.get(0).getMediumAmount()),
                                Integer.parseInt(reward.get(0).getLowAmount()));
                        result = "success";
                    }
                } catch (final Exception e) {
                    //e.printStackTrace();
                }

                return result;
            }

            //After the query has been completed, this method runs and shows the messages corresponding to the results.
            @Override
            protected void onPostExecute(String result) {
                progDailog.dismiss();
                if(result.equalsIgnoreCase("fail")){
                    View titleView = inflater.inflate(R.layout.layout, null);
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this)
                            .setCustomTitle(titleView);
                    ((TextView) titleView.findViewById(R.id.Alert)).setText("Error Message...");
                    dlgAlert.setMessage(Html.fromHtml("<Big>"+"There is no account linked to this card. Add member if needed!"+"</Big>"));
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    overridePendingTransition(0, 0);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    finish();
                                    overridePendingTransition(0, 0);

                                    startActivity(intent);
                                }
                            });
                    dlgAlert.create().show();

                }
            }
        };

        task.execute();

    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

}