package com.shayaanfarooq.architech;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.shayaanfarooq.architech.contractor.MainPageContractor;
import com.shayaanfarooq.architech.contractor.SignupContractor;
import com.shayaanfarooq.architech.customer.HomeCustomerFragment;
import com.shayaanfarooq.architech.customer.MainPageCustomer;

import org.json.JSONException;
import org.json.JSONObject;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.initWithContext(this);

        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenedResult result) {



                try {
                    String check=result.getNotification().getAdditionalData().getString("action");
                    if(check.equals("meetingcancelledbycontractor")){
                        Intent intent=new Intent(getApplicationContext(), MainPageCustomer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("FRAGMENT","meetingcancelledbycontractor");
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Intent intent=new Intent(getApplicationContext(), SignupContractor.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }
        });
    }
}
