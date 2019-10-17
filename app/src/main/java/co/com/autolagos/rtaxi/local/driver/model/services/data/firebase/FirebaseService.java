package co.com.autolagos.rtaxi.local.driver.model.services.data.firebase;

import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.entities.Petition;
import co.com.autolagos.rtaxi.local.driver.model.services.data.local.preferences.driver.DriverPreferences;

public class FirebaseService extends Service {

    private Context context;
    private FirebaseHelper firebaseHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupComponents();
    }

    private void setupComponents() {
        context = this;
        firebaseHelper = new FirebaseHelper(context);
        setupFirebaseHelper();
    }

    private void setupFirebaseHelper() {
        firebaseHelper.listenerChangesPetitions();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Servicio firebase iniciado...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Servicio firebase detenido...");
    }
}
