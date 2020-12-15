package com.shayaanfarooq.architech.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shayaanfarooq.architech.R;
import com.shayaanfarooq.architech.model.Customer;
import com.shayaanfarooq.architech.model.Favourite;
import com.shayaanfarooq.architech.model.FloorPlan;
import com.shayaanfarooq.architech.model.FloorPlanRecyclerObject;
import com.shayaanfarooq.architech.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainPageCustomer extends AppCompatActivity {

    BottomNavigationView bottomNav;
    TextView title;

    Customer myProfile;


    //ArrayList<FloorPlanRecyclerObject> listOfFloorPlans;


    ArrayList<FloorPlan> listOfFloorPlansFromDB;
    ArrayList<Image> listOfImages;
    ArrayList<FloorPlanRecyclerObject> listOfFloorPlans;


    String uid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_customer);



        title= findViewById(R.id.titleMainPageCustomer);
        bottomNav= findViewById(R.id.bottomNavbarCustomer);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);







        mAuth = FirebaseAuth.getInstance();//getting authentication manager instance
        uid = mAuth.getCurrentUser().getUid();//get firebase user id of current user
        Log.i("USERID",uid);



        //populate data
        populateMyProfile(); // does what it says on the tin
        populateData(); // populates floor plans and images, then on success initializes first fragment only

        Fragment initialFragment;
        if(getIntent().getStringExtra("FRAGMENT")!=null){
            if(getIntent().getStringExtra("FRAGMENT").equals("meetingcancelledbycontractor")){
                bottomNav.setSelectedItemId(R.id.meetingNavIcon);
                title.setText("Meetings"); //initially
                initialFragment=new MeetingCustomerFragment();
            }
            else{

                title.setText("Home"); //initially
                initialFragment=new HomeCustomerFragment();
            }
        }
        else{

            title.setText("Home"); //initially
            initialFragment=new HomeCustomerFragment();
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerCustomer,initialFragment).commitAllowingStateLoss();




    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.homeNavIcon);
        title.setText("Home");
    }

    private void populateMyProfile() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//        create listener that will listen for any changes in current user table
        ValueEventListener myProfileListener;
        myProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myProfile = snapshot.getValue(Customer.class);
//                    myProfile = new Customer(myProfileContact);
                    /*if (!myProfile.getImageUrl().isEmpty()) {
                        Picasso.get().load(myProfile.getImageUrl()).into(searchBarProfileIcon);//to set image from firebase storage with picasso
                    }*/

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
//        add listener to that database
        database.addValueEventListener(myProfileListener);
    }

    private void populateData() {

        listOfFloorPlansFromDB=new ArrayList<>();
        listOfImages=new ArrayList<Image>();
        listOfFloorPlans=new ArrayList<>();

        DatabaseReference database;

        database = FirebaseDatabase.getInstance().getReference();
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    listOfFloorPlansFromDB.clear();
//                    listOfImages.clear();
//                    listOfFloorPlans.clear();
//                    for (DataSnapshot s: snapshot.child("Floorplans").getChildren()){
//                        FloorPlan x=s.getValue(FloorPlan.class);
//                        listOfFloorPlansFromDB.add(x);
//
//                    }
//                    for (DataSnapshot s: snapshot.child("Images").getChildren()){
//                        Image temp=s.getValue(Image.class);
//                        Log.d("pathhh",temp.getFloorPlanId());
//                        listOfImages.add(temp);
//                    }
//
//                    /*for (DataSnapshot s: snapshot.child("Favourites").getChildren()){
//                        Favourite tempFavourite=s.getValue(Image.class);
//                        listOfFavourites.add(tempFavourite);
//                    }
//*/
//
//                    for(int i = 0; i< listOfFloorPlansFromDB.size(); i++){
//                        ArrayList<String> temp=new ArrayList<>();
//                        for(int j=0;j<listOfImages.size();j++){
//                            if(listOfFloorPlansFromDB.get(i).getId().equals(listOfImages.get(j).getFloorPlanId()) ){
//                                temp.add(listOfImages.get(j).getName());
//
//                            }
//                        }
//
//                        FloorPlanRecyclerObject tempObject=new FloorPlanRecyclerObject(
//                                listOfFloorPlansFromDB.get(i).getTitle(),
//                                listOfFloorPlansFromDB.get(i).getOwner(),
//                                listOfFloorPlansFromDB.get(i).getWidth(),
//                                listOfFloorPlansFromDB.get(i).getLength(),
//                                listOfFloorPlansFromDB.get(i).getNoOfCars(),
//                                listOfFloorPlansFromDB.get(i).getBathrooms(),
//                                listOfFloorPlansFromDB.get(i).getBedrooms(),
//                                listOfFloorPlansFromDB.get(i).getId(),
//                                temp
//
//                        );
//                        listOfFloorPlans.add(tempObject);
//
//                        //AT THIS POINT WE HAVE ALL FLOOR PLANS SO WE ARE INITIALIZING FRAGMENT
////                        Bundle bundleForInitial = new Bundle();
////                        bundleForInitial.putParcelableArrayList("FLOORPLANS", listOfFloorPlans);
////                        Fragment initialFragment= new HomeCustomerFragment();
////                        initialFragment.setArguments(bundleForInitial);
////                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerCustomer,initialFragment).commitAllowingStateLoss();
//
//
//                    }
//
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }


    //we are creating a nav listener here that is passed to the nav bar above
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener= new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment= new HomeCustomerFragment();

            switch (item.getItemId()){
                case R.id.homeNavIcon:
//                    Bundle bundleForHome = new Bundle();
//                    bundleForHome.putParcelable("MYPROFILE", myProfile);
//                    Bundle bundleForHome = new Bundle();
//                    bundleForHome.putParcelableArrayList("FLOORPLANS", listOfFloorPlans);
                    selectedFragment= new HomeCustomerFragment();
//                    selectedFragment.setArguments(bundleForHome);

                    title.setText("Home");
                    break;

                case R.id.favouriteNavIcon:
                    Bundle bundleForFav = new Bundle();
                    bundleForFav.putParcelable("MYPROFILE", myProfile);
//                    bundleForFav.putParcelableArrayList("FLOORPLANS", new ArrayList<>(listOfFloorPlans));
                    selectedFragment= new FavCustomerFragment();
                    selectedFragment.setArguments(bundleForFav);

                    title.setText("Favourites");
                    break;

                case R.id.meetingNavIcon:
                    Bundle bundleForMeeting = new Bundle();
                    bundleForMeeting.putParcelable("MYPROFILE", myProfile);
                    selectedFragment= new MeetingCustomerFragment();
                    selectedFragment.setArguments(bundleForMeeting);

                    title.setText("Meetings");
                    break;

                case R.id.profileNavIcon:
                    Bundle bundleForProfile = new Bundle();
                    bundleForProfile.putParcelable("MYPROFILE", myProfile);
//                    bundleForProfile.putParcelableArrayList("FLOORPLANS", listOfFloorPlans);
                    selectedFragment= new ProfileCustomerFragment();
                    selectedFragment.setArguments(bundleForProfile);
                    title.setText("Profile");
                    break;

                case R.id.addNavIcon:
                    Intent intent= new Intent(MainPageCustomer.this, AddFloorPlan.class);
                    intent.putExtra("MYPROFILE",myProfile);
                    startActivity(intent);
                    break;
            }


            //this line is used to replace the fragment into the framelayout in the xml with the selected fragment
            if(selectedFragment!=null)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerCustomer,selectedFragment).commit();
            return true;
        }

    };
}
