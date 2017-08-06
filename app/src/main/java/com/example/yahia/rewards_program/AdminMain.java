package com.example.yahia.rewards_program;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AdminMain extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


    }

    public void goToAdminView(View view){
        Intent intent = new Intent(AdminMain.this, AdminView.class);
        startActivity(intent);
    }

    public void goToChangePassword(View view){
        Intent intent = new Intent(AdminMain.this, ChangePassword.class);
        startActivity(intent);
    }
}
