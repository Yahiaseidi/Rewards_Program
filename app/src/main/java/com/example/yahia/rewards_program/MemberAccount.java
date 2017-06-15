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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MemberAccount extends AppCompatActivity {


    private MobileServiceClient mClient;
    private MobileServiceTable<Users> mUsersTable;
    private ProgressBar progressBar_points = null;
    TextView point_total;
    TextView points_needed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_account);

        progressBar_points = (ProgressBar)findViewById(R.id.progressBar_points);
        String s = getIntent().getStringExtra("EXTRA_SESSION_ID");

        int maxPoints = 100;
        int points = Integer.parseInt(s);
        int pointsNeeded = maxPoints - points;

        point_total = (TextView)findViewById(R.id.point_total);
        points_needed = (TextView)findViewById(R.id.points_needed);

        points_needed.setText("Points until next reward: " + pointsNeeded);
        point_total.setText(points + " points");

        progressBar_points.setVisibility(View.VISIBLE);
        progressBar_points.setMax(100);
        progressBar_points.setProgress(points);

    }


}