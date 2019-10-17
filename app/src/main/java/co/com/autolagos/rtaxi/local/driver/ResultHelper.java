/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.com.autolagos.rtaxi.local.driver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import co.com.autolagos.rtaxi.local.driver.Activity.HomeActivity;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;

/**
 * Class to process location results.
 */
public class ResultHelper {

    private List<Location> mLocations;
    private NotificationManager mNotificationManager;

    ResultHelper(List<Location> locations) {
        mLocations = locations;

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(Constants.PRIMARY_CHANNEL,
                    AppBase.getInstance().getApplicationContext().getString(R.string.default_channel), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getNotificationManager().createNotificationChannel(channel);
        }
    }

    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     */
    private String getLocationResultTitle() {
        String numLocationsReported = AppBase.getInstance().getApplicationContext().getResources().getQuantityString(
                R.plurals.num_locations_reported, mLocations.size(), mLocations.size());
        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(new Date());
    }

    private String getLocationResultText() {
        if (mLocations.isEmpty()) {
            return AppBase.getInstance().getApplicationContext().getString(R.string.unknown_location);
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : mLocations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }


    /**
     * Get the notification mNotificationManager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) AppBase.getInstance().getApplicationContext().getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    /**
     * Displays a notification with the location results.
     */
    void showNotification() {
        Intent notificationIntent = new Intent(AppBase.getInstance().getApplicationContext(), HomeActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(AppBase.getInstance().getApplicationContext());

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(HomeActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(AppBase.getInstance().getApplicationContext(),
                    Constants.PRIMARY_CHANNEL)
                    .setContentTitle(getLocationResultTitle())
                    .setContentText(getLocationResultText())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(notificationPendingIntent);
            getNotificationManager().notify(0, notificationBuilder.build());

        } else {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppBase.getInstance().getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                    .setContentTitle(getLocationResultTitle()) // title for notification
                    .setContentText(getLocationResultText())// message for notification
                    //.setSound(alarmSound) // set alarm sound for notification
                    .setAutoCancel(true); // clear notification after click
            Intent intent = new Intent(AppBase.getInstance().getApplicationContext(), HomeActivity.class);
            PendingIntent pi = PendingIntent.getActivity(AppBase.getInstance().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            //   g.notify(0, mBuilder.build());
            getNotificationManager().notify(0, mBuilder.build());


        }

    }


    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    void saveResults() {
        Preferences.saveResults(getLocationResultTitle() + "\n" +
                getLocationResultText());
    }
}
