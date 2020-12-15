package com.shayaanfarooq.architech.customer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.VirtualLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shayaanfarooq.architech.R;
import com.shayaanfarooq.architech.model.Customer;
import com.shayaanfarooq.architech.model.Favourite;
import com.shayaanfarooq.architech.model.FloorPlan;
import com.shayaanfarooq.architech.model.FloorPlanRecyclerObject;
import com.shayaanfarooq.architech.model.Image;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FavCustomerFragment extends Fragment {


    Customer myProfile;
//    ArrayList<FloorPlanRecyclerObject> listOfFloorPlans;
    ArrayList<Favourite> listOfFavourites;
    ArrayList<FloorPlanRecyclerObject> listOfFavouriteFloorplans;


    ProgressBar progressBar;
    RecyclerView recyclerView;


    FavCustomerFragmentRvAdapter rvAdapter;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myProfile= getArguments().getParcelable("MYPROFILE");
//        listOfFloorPlans = new ArrayList<>();
////        listOfFloorPlans= getArguments().getParcelableArrayList("FLOORPLANS");
//        listOfFloorPlans= new ArrayList<>(listOfFloorPlans);
        listOfFavourites=new ArrayList<>();
        listOfFavouriteFloorplans=new ArrayList<>();
        View view= inflater.inflate(R.layout.fragment_fav_customer,container,false);
        progressBar= view.findViewById(R.id.progressBarFavCustomerFragment);
        //initailly sends null list till firebase operation is done
        buildRecyclerView(view); //does what it says

        //TODO: In this function, retrieve favourites, then using myProfile.getUid() filter the listOfFloorplans
        //TODO: After that in there call rvAdapter.setListOfFloorPlans and notifydataset
        populateListOfFavouriteFloorPlans();


        mAuth=FirebaseAuth.getInstance();

//
//        database = FirebaseDatabase.getInstance().getReference().child("Floorplans");
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    listOfFloorPlans.clear();
//                    listOfImages.clear();
//                    listForRecycler.clear();
//                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Images");
//                    for(DataSnapshot s: snapshot.getChildren()){
//                        FloorPlan newFp = s.getValue(FloorPlan.class);
//                        ArrayList<String> imgPathsString = new ArrayList<>();
//                        Query queryRef = db.orderByChild("floorPlanId").equalTo(newFp.getId());
//                        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(snapshot.exists()){
//                                    for(DataSnapshot s: snapshot.getChildren()){
//                                        imgPathsString.add(s.getValue(Image.class).getName());
//                                    }
//                                    listForRecycler.add(new FloorPlanRecyclerObject(
//                                            newFp.getTitle(),
//                                            newFp.getOwner(),
//                                            newFp.getWidth(),
//                                            newFp.getLength(),
//                                            newFp.getNoOfCars(),
//                                            newFp.getBathrooms(),
//                                            newFp.getBedrooms(),
//                                            newFp.getId(),
//                                            imgPathsString
//                                    ));
//                                    rvAdapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                    rvAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
//        database = FirebaseDatabase.getInstance().getReference();
//
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for (DataSnapshot s: snapshot.child("Favourites").getChildren()){
//                        Favourite x=s.getValue(Favourite.class);
//                        listOfFavourites.add(x);
//                        Log.d("print",x.getFloorPlanId());
//
//                    }
//
//                    for(int i=0;i<listOfFavourites.size();i++){
//                        for(int j=0;j<listOfFloorPlans.size();j++){
//                            if(listOfFavourites.get(i).getUserId().equals(uid)  && listOfFavourites.get(i).getFloorPlanId().equals(listOfFloorPlans.get(j).getId())){
//                                listOfFavouriteFloorplans.add(listOfFloorPlans.get(j));
//                                Log.d("errorcheck",listOfFloorPlans.get(j).getTitle());
//                            }
//
//                        }
//                    }
//                    Log.d("size",Integer.toString(listOfFavouriteFloorplans.size()));
//                    listOfFloorPlans.clear();
//                    listOfFloorPlans.addAll(listOfFavouriteFloorplans);
//                    rvAdapter.setListOfFloorPlans(listOfFloorPlans);
//                    rvAdapter.notifyDataSetChanged();
//
//                }
//                else{
//                    Log.d("error","Error");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        return view;
    }

    private void populateListOfFavouriteFloorPlans() {

        //get and filter

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference().child("Favourites");
        Query query = database.orderByChild("userId").equalTo(myProfile.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                {
                    progressBar.setVisibility(View.GONE);
                }

                for(DataSnapshot s: snapshot.getChildren()){
                    Favourite newFav = s.getValue(Favourite.class);
                    DatabaseReference dbFp = FirebaseDatabase.getInstance().getReference().child("Floorplans");
                    Query queryFp = dbFp.orderByChild("id").equalTo(newFav.getFloorPlanId());
                    queryFp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                listOfFavouriteFloorplans.clear();
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Images");
                                for(DataSnapshot s: snapshot.getChildren()){
                                    FloorPlan newFp = s.getValue(FloorPlan.class);
                                    ArrayList<String> imgPathsString = new ArrayList<>();
                                    Query queryRef = db.orderByChild("floorPlanId").equalTo(newFp.getId());
                                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                for(DataSnapshot s: snapshot.getChildren()){
                                                    imgPathsString.add(s.getValue(Image.class).getName());
                                                }
                                                listOfFavouriteFloorplans.add(new FloorPlanRecyclerObject(
                                                        newFp.getTitle(),
                                                        newFp.getOwner(),
                                                        newFp.getWidth(),
                                                        newFp.getLength(),
                                                        newFp.getNoOfCars(),
                                                        newFp.getBathrooms(),
                                                        newFp.getBedrooms(),
                                                        newFp.getId(),
                                                        imgPathsString,
                                                        newFp.getOwnerName()
                                                ));
                                                rvAdapter.notifyDataSetChanged();
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                progressBar.setVisibility(View.GONE);
                                rvAdapter.notifyDataSetChanged();
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                rvAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        rvAdapter.setListOfFloorPlans(listOfFavouriteFloorplans);
//        rvAdapter.notifyDataSetChanged();
    }

    private void buildRecyclerView(View view) {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerViewFavCustomer);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        rvAdapter=new FavCustomerFragmentRvAdapter(listOfFavouriteFloorplans, getActivity());
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
