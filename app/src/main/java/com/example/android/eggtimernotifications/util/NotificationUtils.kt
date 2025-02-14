/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID. We use in this always the same id.
private const val NOTIFICATION_ID = 0 // unused?

/** Request code for pending intent. needed to access the PendingIntent for update or cancel. */
private const val REQUEST_CODE = 0

/* flag 'one shot' because the intent will be used only once. */
private const val FLAGS = 0


// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param messageBody, notification text.
 * @param applicationContext, activity context.
 */
fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context
) {
    /** Create content intent for notification, which launches this activity
     *  Done: Step 1.11 */
    val contentIntent =
        Intent(applicationContext, MainActivity::class.java) // Intent calls this class

    // Create PendingIntent Done: Step 1.12
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent, // intent of the activity to be launched
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Loading the image from resources
    //  Done: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )

    /* Add picture and icon to notification. Use specific style. */
    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    // TODO: Step 2.2 add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )

    /** To quick the action, enter notification will disappear after the first step,
     * which is why the intent can only be used once. */

    // Done: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    )
        // Step 1.8 use the new 'breakfast' notification channel

        // Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.cooked_egg) // We call methods of builder!
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        // Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // Step 2.1 add style to builder
        .setStyle(bigPictureStyle)
        .setLargeIcon(eggImage)

        // Step 2.3 add snooze action
        .addAction(
            R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

        // Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // TODO: Step 1.4 call notify
    /* We can directly call notify since you are performing to call
     from an extension function on the same class.*/
    notify(NOTIFICATION_ID, builder.build())
}

// TODO: Step 1.14 Cancel all notifications
fun NotificationManager.cancelNotifications() {
    cancelAll()
}