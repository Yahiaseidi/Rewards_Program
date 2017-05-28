package com.example.yahia.rewards_program;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Winnings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winnings);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(Winnings.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(Winnings.this, AddNewMember.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_enter_phone:
                        Intent intent3 = new Intent(Winnings.this, EnterAlternateID.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent4 = new Intent(Winnings.this, AdminView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }
}
