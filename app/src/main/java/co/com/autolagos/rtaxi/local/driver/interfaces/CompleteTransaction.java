package co.com.autolagos.rtaxi.local.driver.interfaces;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface CompleteTransaction {

    void onComplete(@Nullable DatabaseError var1, boolean var2, @Nullable DataSnapshot var3);

}