package com.example.yahia.rewards_program;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddNewMember extends AppCompatActivity {

    EditText newPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);

        newPhoneNumber = (EditText)findViewById(R.id.newPhoneNumber);

        newPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(newPhoneNumber.getText().length() < 10)
                {
                    newPhoneNumber.setError("Invalid Phone #");
                }
            }
        });

    }
}
