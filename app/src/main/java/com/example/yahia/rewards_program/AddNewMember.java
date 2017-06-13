package com.example.yahia.rewards_program;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.ExecutableQuery;

import javax.xml.transform.Result;

public class AddNewMember extends AppCompatActivity implements View.OnClickListener{

    private EditText newPhoneNumber;
    private EditText cardNumber;
    private Button createNewMember_btn;
    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUsersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        newPhoneNumber = (EditText)findViewById(R.id.newPhoneNumber);
        createNewMember_btn = (Button)findViewById(R.id.createNewMember_btn);
        cardNumber = (EditText)findViewById(R.id.enterID_txt);
        createNewMember_btn.setOnClickListener(AddNewMember.this);

        //Makes sure the createNewMember button is not clicked before text is validated
        newPhoneNumber.setError("Invalid Phone #");
        cardNumber.setError("Required");
        createNewMember_btn.setClickable(false);

        //Validate Card ID
        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }

            //Validates the length of the text box after each new entry.
            @Override
            public void afterTextChanged(Editable s) {
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

        //Validate Phone Number
        newPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
            }

            //Validates the length of the text box after every new entry.
            @Override
            public void afterTextChanged(Editable s) {
                if (newPhoneNumber.getText().length() < 10 || newPhoneNumber.getText().length() > 10) {
                    newPhoneNumber.setError("Invalid Phone #");
                    createNewMember_btn.setClickable(false);
                } else {
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

        try {
            mClient = new MobileServiceClient(
                    "https://rewards-program.azurewebsites.net",
                    this);


            mUsersTable = mClient.getTable(Users.class);
            newPhoneNumber = (EditText) findViewById(R.id.newPhoneNumber);
            cardNumber = (EditText) findViewById(R.id.enterID_txt);

        } catch (MalformedURLException e) {
            //  createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            //createAndShowDialog(e, "Error");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == createNewMember_btn)
        {

            try {
                numExists(newPhoneNumber.getText().toString());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void goToMemberAdded() {
        Intent newActivity = new Intent(this, MemberAdded.class);
        startActivity(newActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void addItem() {
        if (mClient == null) {
            return;
        }

        final Users item = new Users();
        item.setNumber(newPhoneNumber.getText().toString());
        item.setCard(cardNumber.getText().toString());
        item.setPoints("0");

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final Users entity = addItemInTable(item);
                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };

        runAsyncTask(task);
    }


    public Users addItemInTable(Users item) throws ExecutionException, InterruptedException {
        Users entity = mUsersTable.insert(item).get();
        return entity;
    }

    public void numExists(final String s) throws ExecutionException, InterruptedException {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){

            //This method queries the database table to verify that the new member's phone number is not in the database.
            @Override
            protected String doInBackground(Void... voids) {

                String result = "";

                try {
                    List<Users> list = mUsersTable.where().field("numbers").eq(s).execute().get();
                    if(list.size() == 0) {
                        addItem();
                        result = "success";
                    }
                    else
                    {
                        result = "fail";
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
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(AddNewMember.this);

                    dlgAlert.setMessage("There is an account linked to this phone number already!");
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
                else if(result.equalsIgnoreCase("success"))
                {
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(AddNewMember.this);

                    dlgAlert.setMessage("New member has been added!!");
                    dlgAlert.setTitle("Success...");
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    //Delays the running of method goToMemberAdded() by 3000 milliseconds
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToMemberAdded();
                        }
                    }, 3000);
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
