package com.shayaanfarooq.architech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.onesignal.OneSignal;
import com.shayaanfarooq.architech.contractor.LoginContractor;
import com.shayaanfarooq.architech.contractor.SignupContractor;
import com.shayaanfarooq.architech.customer.LoginCustomer;
import com.shayaanfarooq.architech.customer.SignupCustomer;

public class MainActivity extends AppCompatActivity {

    Button customerButton, contractorButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerButton= findViewById(R.id.customerButton);
        contractorButton= findViewById(R.id.contractorButton);


        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, LoginCustomer.class);
                startActivity(intent);
            }
        });


        contractorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, LoginContractor.class);
                startActivity(intent);
            }
        });



    }


}
