package com.example.yahia.rewards_program;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PinView extends AppCompatActivity {

    private TextView mTextMessage;
    private Thread thread;
    private Handler handler = new Handler();
    private MobileServiceClient mClient;
    private MobileServiceTable<Admin> mAdminTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_view);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(PinView.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(PinView.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(PinView.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(PinView.this, PinView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });


        onPin();
        try {
            mClient = new MobileServiceClient(
                    "https://rewards-program.azurewebsites.net",
                    this);


            mAdminTable = mClient.getTable(Admin.class);

        } catch (MalformedURLException e) {
            //  createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            //createAndShowDialog(e, "Error");
        }

    }

    public void onPin() {
        Pinview pinview = (Pinview)findViewById(R.id.pinView);
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(final Pinview pinview, boolean b) {
                // Toast.makeText(PinView.this, ""+pinview.getValue(), Toast.LENGTH_SHORT).show();
                getCurrentPin(pinview.getValue());
            }
        });

    }

    public void getCurrentPin(final String enteredPass) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){

            ProgressDialog progDailog = new ProgressDialog(PinView.this, R.style.AppCompatAlertDialogStyle);

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
                    List<Admin> list = mAdminTable.where().field("id").eq("97C70718-EA34-4ED3-87D2-EB274BD4B340").execute().get();
                    String id = list.get(0).getId();
                    if(list.get(0).getPassword().equals(getSecurePassword(enteredPass, id))) {
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
                progDailog.dismiss();
                if(result.equalsIgnoreCase("fail")){
                    final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(PinView.this);
                    dlgAlert.setMessage(Html.fromHtml("<Big>"+"Oops that must be the wrong password!"+"</Big>"));
                    dlgAlert.setTitle("Error Message...");
                    dlgAlert.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
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
                    dlgAlert.setCancelable(true);
                }
                else {
                    Intent intent = new Intent(PinView.this, AdminMain.class);
                    startActivity(intent);
                }
            }
        };

        task.execute();
    }

    public String getSecurePassword(String passwordToHash, String   salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
