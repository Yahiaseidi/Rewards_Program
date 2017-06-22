package com.example.yahia.rewards_program;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EnterOrderAmount extends AppCompatActivity {

    TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_order_amount);

        Bundle extras = getIntent().getExtras();
        String cardNumber = extras.getString("card");

        textView5 = (TextView)findViewById(R.id.textView5);
        textView5.setText("Your card number is: " + cardNumber);

    }
}
