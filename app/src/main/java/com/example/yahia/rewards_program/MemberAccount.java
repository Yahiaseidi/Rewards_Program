package com.example.yahia.rewards_program;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MemberAccount extends AppCompatActivity {


    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUsersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_account);
        String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
        TextView textView = (TextView) findViewById(R.id.textViewUser);
        textView.setText("Rewards Points: " + s);

    }


}