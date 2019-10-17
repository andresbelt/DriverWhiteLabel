package co.com.autolagos.rtaxi.local.driver.model.services.data.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.events.MessageBusInformation;
import co.com.autolagos.rtaxi.local.driver.model.events.StationBus;
import co.com.autolagos.rtaxi.local.driver.model.services.data.local.preferences.driver.DriverPreferences;

public class FirebaseHelper {

    private Context context;
    private DriverPreferences driverPreferences;

    public FirebaseHelper(Context context) {
        this.context = context;
        setupDriverPreferences();
    }

    private void setupDriverPreferences() {
        driverPreferences = new DriverPreferences(context);
    }

    public void listenerChangesPetitions() {
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("PeticionCarrera").child("location");


        assignedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    System.out.println("Esa wea si existe");
                    StationBus.getBus().post(new MessageBusInformation(4, "",dataSnapshot));
                }else{
                    System.out.println("Esa wea no existe");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void listenerChangesPetitions(double latitude, double longitude) {
        Driver driver = driverPreferences.show();
        String driverId = driver.getUserId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child("Drivers")
                .child(driverId)
                .child("location");
        Map<String, Object> updates = new HashMap<>();
        updates.put("lat", latitude);
        updates.put("lng", longitude);
        databaseReference.updateChildren(updates);
    }

}
