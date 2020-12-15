package com.shayaanfarooq.architech.customer;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shayaanfarooq.architech.R;
import com.shayaanfarooq.architech.contractor.MainPageContractor;
import com.shayaanfarooq.architech.contractor.SignupContractor;
import com.shayaanfarooq.architech.model.Customer;
import com.shayaanfarooq.architech.model.FloorPlan;
import com.shayaanfarooq.architech.model.Image;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddFloorPlan extends AppCompatActivity {
    Customer myProfile;
    Button addFloorplanButton;
    EditText title,length,width,bedroom,bathrooms,carsCapacity;
    ImageButton imageButton;
    ViewPager viewPager;
    TextView selectImageText;
    ProgressBar progressBar;
    AddFloorPlanViewPagerAdapter vpAdapter;
    ArrayList<Uri> imageList;
    ArrayList<String> imagePaths;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_floor_plan);

        myProfile = getIntent().getParcelableExtra("MYPROFILE");

        imageButton=findViewById(R.id.image_addfloorplan);
        title=findViewById(R.id.title_addfloorplan);
        length=findViewById(R.id.length_addfloorplan);
        width=findViewById(R.id.width_addfloorplan);
        bedroom=findViewById(R.id.bedrooms_addfloorplan);
        bathrooms=findViewById(R.id.bathrooms_addfloorplan);
        carsCapacity=findViewById(R.id.car_capacity_addfloorplan);
        addFloorplanButton=findViewById(R.id.addFloorPlanButton);
        viewPager= findViewById(R.id.viewPagerAddFloorPlan);
        selectImageText= findViewById(R.id.selectImageText);
        progressBar= findViewById(R.id.progressBarAddFloorPlan);

        mAuth=FirebaseAuth.getInstance();

        imageList=new ArrayList<>();
        imagePaths=new ArrayList<>();

        vpAdapter= new AddFloorPlanViewPagerAdapter(this, imageList);
        viewPager.setAdapter(vpAdapter);



        selectImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(AddFloorPlan.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddFloorPlan.this,
                                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                        100);
                    return;
                }
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        addFloorplanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String titleString= title.getText().toString().trim();
                String widthString= width.getText().toString().trim();
                String lengthString= length.getText().toString().trim();
                String carsCapacityString= carsCapacity.getText().toString().trim();
                String bathroomsString= bathrooms.getText().toString().trim();
                String bedroomString= bedroom.getText().toString().trim();




                if (imageList.size() == 0) {
                    Toast.makeText(AddFloorPlan.this, "Add at least 1 image", Toast.LENGTH_SHORT).show();
                }
                else if (titleString.isEmpty())
                {
                    title.setError("Set title");
                    title.requestFocus();
                }
                else if (lengthString.isEmpty())
                {
                    length.setError("Enter length");
                    length.requestFocus();
                }
                else if (widthString.isEmpty())
                {
                    width.setError("Enter width");
                    width.requestFocus();
                }
                else if (bedroomString.isEmpty())
                {
                    bedroom.setError("Enter no. of bedrooms");
                    bedroom.requestFocus();
                }
                else if (bathroomsString.isEmpty())
                {
                    bathrooms.setError("Enter number of bathrooms");
                    bathrooms.requestFocus();
                }
                else if (carsCapacityString.isEmpty())
                {
                    carsCapacity.setError("Add number of cars");
                    carsCapacity.requestFocus();
                }
                else{
                    hideKeyboard(AddFloorPlan.this);
                    progressBar.setVisibility(View.VISIBLE);
                    String key = FirebaseDatabase.getInstance().getReference().child("Floorplans").push().getKey();
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                            .child("Floorplans")
                            .child(key);

                    FloorPlan newFloorplan = new FloorPlan(
                            title.getText().toString().trim(),
                            mAuth.getCurrentUser().getUid(),
                            width.getText().toString().trim(),
                            length.getText().toString().trim(),
                            carsCapacity.getText().toString().trim(),
                            bathrooms.getText().toString().trim(),
                            bedroom.getText().toString().trim(),
                            key,
                            myProfile.getName()


                    );
                    database.setValue(newFloorplan).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            for (int i = 0; i < imageList.size(); i++) {
                                StorageReference st = FirebaseStorage.getInstance().getReference();
                                st = st.child("Images").child(key + Integer.toString(i));
                                int finalI = i;
                                st.putFile(imageList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String dp = uri.toString();
                                                imagePaths.add(dp);
                                                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference()
                                                        .child("Images")
                                                        .child(key + Integer.toString(finalI));
                                                database2.setValue(new Image(
                                                        dp,
                                                        key
                                                ));
                                                Intent intent= new Intent(AddFloorPlan.this, MainPageCustomer.class);
                                                //startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddFloorPlan.this, "Image(s) uploading failed", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddFloorPlan.this, "Image uploading failed", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                                ;
                            }
                        }
                    });


                }

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 1 && resultCode == RESULT_OK)){
            imageButton.setVisibility(View.GONE);
            ImageView imageView=findViewById(R.id.image_addfloorplan);
            ClipData clipdata= data.getClipData();
            if(clipdata !=null){
                //for multiple images
                imageList.clear();
                for(int i=0;i<clipdata.getItemCount();i++){
                    imageList.add(clipdata.getItemAt(i).getUri());

                }

            }
            else{
                //for single image
                Uri image=data.getData();
                imageList.clear();
                imageList.add(image);

            }

            vpAdapter.setImageUrls(imageList);
            vpAdapter.notifyDataSetChanged();

        }
    }



    void hideKeyboard(Context c)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(c);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
