package com.example.yahia.rewards_program;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class EnterAlternateID extends AppCompatActivity {

    private TextView mTextMessage;
    public BottomNavigationView bottom;

    public void bottomNav() {
        bottom = (BottomNavigationView)findViewById(R.id.navigation);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(EnterAlternateID.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(EnterAlternateID.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent3 = new Intent(EnterAlternateID.this, AdminView.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_alternate_id);
        bottomNav();
    }
}
