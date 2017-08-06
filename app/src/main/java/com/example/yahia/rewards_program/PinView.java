package com.example.yahia.rewards_program;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
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

            //Runs a query to the database in the background.
            @Override
            protected String doInBackground(Void... voids) {

                String result = "";

                try {
                    List<Admin> list = mAdminTable.where().field("id").eq("97C70718-EA34-4ED3-87D2-EB274BD4B340").execute().get();
                    if(list.get(0).getPassword().equals(enteredPass)) {
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
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(PinView.this);

                    dlgAlert.setMessage("Oops that must be the wrong password!");
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
                    dlgAlert.create().show();
                }
                else {
                    Intent intent = new Intent(PinView.this, AdminMain.class);
                    startActivity(intent);
                }
            }
        };

        task.execute();

    }

}
