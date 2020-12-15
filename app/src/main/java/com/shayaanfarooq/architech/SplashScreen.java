package com.shayaanfarooq.architech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shayaanfarooq.architech.contractor.MainPageContractor;
import com.shayaanfarooq.architech.customer.MainPageCustomer;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user== null)
        {
            System.out.println("CURRENT USER IS NULL");
            //goint to main
            Intent goToMain= new Intent(SplashScreen.this, MainActivity.class);
            finish();
            startActivity(goToMain);
        }
        else{

            Log.d("Splash Screen- ", "CurrentUser: " + user.getEmail());
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        // The child doesn't exist
                        Log.d("Splash Screen- ", "User Does not Exists In Customer User Table " + user.getEmail());
                        Intent goToHomeContractor= new Intent(SplashScreen.this, MainPageContractor.class);
                        finish();
                        startActivity(goToHomeContractor);
                    }
                    else{
                        Log.d("Splash Screen- ", "User Exists In Customer User Table " + user.getEmail());
                        Intent goToHomeCustomer = new Intent(SplashScreen.this, MainPageCustomer.class);
                        finish();
                        startActivity(goToHomeCustomer);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }









    }
}