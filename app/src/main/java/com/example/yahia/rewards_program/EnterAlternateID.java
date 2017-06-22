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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class EnterAlternateID extends AppCompatActivity implements View.OnClickListener {

    EditText phone_editText;
    TextView textView5;
    Button phoneSearch_btn;
    private String number;
    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUsersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_alternate_id);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        phoneSearch_btn = (Button)findViewById(R.id.phoneSearch_btn);
        phone_editText = (EditText)findViewById(R.id.phone_editText);
        phoneSearch_btn.setOnClickListener(EnterAlternateID.this);
        number = phone_editText.getText().toString();
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

        try {
            mClient = new MobileServiceClient(
                    "https://rewards-program.azurewebsites.net",
                    this);


            mUsersTable = mClient.getTable(Users.class);

        } catch (MalformedURLException e) {
            //  createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            //createAndShowDialog(e, "Error");
        }
    }

    public void onClick(View view) {
        if (view == phoneSearch_btn)
        {
            number = phone_editText.getText().toString();

            try {
                numExists(number);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void goToMemberAccount(String points, String card) {
        Intent newActivity = new Intent(getBaseContext(), MemberAccount.class);
        Bundle extras = new Bundle();
        extras.putString("points", points);
        extras.putString("card", card);
        newActivity.putExtras(extras);
        startActivity(newActivity);
    }


    public void numExists(final String s) throws ExecutionException, InterruptedException {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){

            //Runs a query to the database in the background.
            @Override
            protected String doInBackground(Void... voids) {

                String result = "";

                try {
                    List<Users> list = mUsersTable.where().field("numbers").eq(s).execute().get();
                    if(list.size() == 0) {
                        result = "fail";
                    }
                    else
                    {
                        Users user = list.get(0);
                        goToMemberAccount(user.getPoints(), user.getCard());
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
                if(result.equalsIgnoreCase("fail")){
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(EnterAlternateID.this);

                    dlgAlert.setMessage("Oops there is no account linked to this phone number!");
                    dlgAlert.setTitle("Error Message...");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
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


