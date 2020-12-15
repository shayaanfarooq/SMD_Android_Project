package com.shayaanfarooq.architech.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shayaanfarooq.architech.R;
import com.shayaanfarooq.architech.model.Favourite;
import com.shayaanfarooq.architech.model.FloorPlan;
import com.shayaanfarooq.architech.model.FloorPlanRecyclerObject;

import java.util.ArrayList;

public class FloorPlanDetails extends AppCompatActivity {

    TextView imageCounter;
    RelativeLayout page;
    FloorPlanRecyclerObject floorPlan;
    TextView title, size, bedrooms, bathrooms, cars, owner;
    Button addToFavourites,findContractor;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    ViewPager viewPager;
    FloorPlanDetailsViewPagerAdapter vpAdapter;
    String uid;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan_details);
        Intent intent= getIntent();
        floorPlan = intent.getParcelableExtra("FLOORPLAN");

        mAuth = FirebaseAuth.getInstance();//getting authentication manager instance

        addToFavourites=findViewById(R.id.add_to_favourites_floorplan_detail);
        findContractor=findViewById(R.id.find_contractor_floorplan_detail);

        imageCounter= findViewById(R.id.imageCounter);
        title= findViewById(R.id.titleFloorPlanDetail);
        size= findViewById(R.id.sizeFloorPlanDetail);
        bedrooms= findViewById(R.id.bedroomsFloorPlanDetail);
        bathrooms= findViewById(R.id.bathroomsFloorPlanDetail);
        cars= findViewById(R.id.carCapacityFloorPlanDetail);
        owner= findViewById(R.id.nameOfOwnerFloorPlanDetail);
        progressBar= findViewById(R.id.progressBarAddToFavs);


        String floorPlanId=floorPlan.getId();
        uid = mAuth.getCurrentUser().getUid();




        title.setText(floorPlan.getTitle());
        size.setText(String.format("Size: %s", floorPlan.getSize()));
        bedrooms.setText(String.format("Bedrooms: %s", floorPlan.getBedrooms()));
        bathrooms.setText(String.format("Bathrooms: %s", floorPlan.getBathrooms()));
        cars.setText(String.format("Max Cars Capacity: %s", floorPlan.getNoOfCars()));
        owner.setText(String.format("by %s", floorPlan.getOwnerName()));

        viewPager= findViewById(R.id.viewPagerFloorPlanDetail);
        vpAdapter= new FloorPlanDetailsViewPagerAdapter(this, floorPlan.getPaths());
        viewPager.setAdapter(vpAdapter);

        imageCounter.setText(String.format("%d/%d", 1, vpAdapter.getCount()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(FloorPlanDetails.this, "Image number " + position, Toast.LENGTH_SHORT).show();
                imageCounter.setText(String.format("%d/%d", position + 1, vpAdapter.getCount()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String key = FirebaseDatabase.getInstance().getReference().child("Favourites").push().getKey();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                        .child("Favourites");
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Boolean favPresentCheck = false;
                            for(DataSnapshot s: snapshot.getChildren()){
                                Favourite tempFav = s.getValue(Favourite.class);
                                if(tempFav.getFloorPlanId().equals(floorPlanId)
                                && tempFav.getUserId().equals(uid)){
                                    favPresentCheck = true;
                                }
                            }
                            if(favPresentCheck){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FloorPlanDetails.this,"Floorplan already added to favourites"
                                , Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Favourite newFavourite=new Favourite(uid,floorPlanId);
                                database.child(key).setValue(newFavourite).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(FloorPlanDetails.this,"Floorplan added to favourites",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FloorPlanDetails.this,"Floorplan could no be added to favourites!",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
//                Favourite newFavourite=new Favourite(uid,floorPlanId);
//                database.child(key).setValue(newFavourite).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(FloorPlanDetails.this,"Floorplan added to favourites",Toast.LENGTH_SHORT);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(FloorPlanDetails.this,"Floorplan could no be added to favourites!",Toast.LENGTH_SHORT);
//                    }
//                });

            }
        });


        findContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToContractorsPage = new Intent(FloorPlanDetails.this, ContractorsPageCustomer.class);
                goToContractorsPage.putExtra("FLOORPLAN",floorPlan);
                startActivity(goToContractorsPage);
            }
        });

    }
}
