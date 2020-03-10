package com.example.filmgan2.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.filmgan2.ApiClient;
import com.example.filmgan2.BuildConfig;
import com.example.filmgan2.MainActivity;
import com.example.filmgan2.R;
import com.example.filmgan2.application.film.DetailFilm;
import com.example.filmgan2.application.film.model.filmData;
import com.example.filmgan2.application.film.model.filmResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TYPE_REPEATING_NEW_FILM = "New Film";
    public static final String TYPE_REPEATING_REMINDER = "Reminder";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private static final String CHANNEL_ID_REMINDER = "Channel_reminder";
    private static final String CHANNEL_ID_NEW_FILM = "Channel_new_film";
    private static final String CHANNEL_NAME_REMINDER = "AlarmManager Reminder";
    private static final String CHANNEL_NAME_NEW_FILM = "AlarmManager New Film";

    private static final String KEY_GROUP_REMINDER = "group_reminder";
    private static final String KEY_GROUP_NEW_FILM = "group_new_film";

    // Siapkan 2 id untuk 2 macam alarm, onetime dan repeating
    private final static int ID_REPEATING_NEW_FILM = 100;
    private final static int ID_REPEATING_REMINDER = 201;
    private ArrayList<filmData> listFilm = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        String title = type.equalsIgnoreCase(TYPE_REPEATING_REMINDER) ? TYPE_REPEATING_REMINDER : TYPE_REPEATING_NEW_FILM;
        int notifId = type.equalsIgnoreCase(TYPE_REPEATING_REMINDER) ? ID_REPEATING_REMINDER : ID_REPEATING_NEW_FILM;
        String Channel_id = type.equalsIgnoreCase(TYPE_REPEATING_REMINDER) ? CHANNEL_ID_REMINDER : CHANNEL_ID_NEW_FILM;
        String Channel_name = type.equalsIgnoreCase(TYPE_REPEATING_REMINDER) ? CHANNEL_NAME_REMINDER : CHANNEL_NAME_NEW_FILM;

        if (title.equals(TYPE_REPEATING_NEW_FILM)) {
            setFilm(context, Locale.getDefault().getLanguage(), Channel_id, Channel_name);
        }

        if (title.equals(TYPE_REPEATING_REMINDER)) {
            showNotificationReminder(context, title, message, notifId, Channel_id, Channel_name);
        }
    }

    public void setFilm(final Context context, final String language, final String Channel_id, final String Channel_name) {
        GetNewFilm apiClient = ApiClient.getRetrofitInstance().create(GetNewFilm.class);
        Call<filmResponse> call = apiClient.getNewFilm(BuildConfig.API_KEY, getCurrentDate(), getCurrentDate(), language.equals("in") ? "id" : language);

        call.enqueue(new Callback<filmResponse>() {
            @Override
            public void onResponse(Call<filmResponse> call, Response<filmResponse> response) {
                listFilm.addAll(response.body().getResults());
                int idNotification = 0;
                for (int i = 0; i < 6; i++) {
                    filmData data = listFilm.get(i);
                    idNotification++;
                    showNotification(context, data.getJudulfilm(), data, idNotification, Channel_id, Channel_name);
                }
            }

            @Override
            public void onFailure(Call<filmResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

    private void showNotificationReminder(Context context, String title, String message, int notifId, String channel_id, String channel_name) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setGroup(KEY_GROUP_REMINDER)
                .setColor(ContextCompat.getColor(context, R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id,
                    channel_name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(channel_id);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    private void showNotification(Context context, String title, filmData filmData, int notifId, String channel_id, String channel_name) {
        String message = filmData.getJudulfilm() + "release today";
        Log.d("SETTINGS", message);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, DetailFilm.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra(DetailFilm.EXTRA_NAME, filmData);

//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, notifId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent = TaskStackBuilder.create(context)
                .addParentStack(DetailFilm.class)
                .addNextIntent(notificationIntent)
                .getPendingIntent(notifId,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setGroup(KEY_GROUP_NEW_FILM)
                .setContentIntent(resultPendingIntent)
                .setColor(ContextCompat.getColor(context, R.color.transparent))
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id,
                    channel_name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(channel_id);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void setRepeatingALarm(Context context, String type, String message, int hour) {
        Log.d("SETTINGS", "SET REPEATING : " + message);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, type.equalsIgnoreCase(TYPE_REPEATING_REMINDER) ? ID_REPEATING_REMINDER : ID_REPEATING_NEW_FILM, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("ALARMKU", String.valueOf(alarmManager != null));
        }
    }

    public void cancelAlarm(Context context, String type) {
        Log.d("SETTINGS", "CANCEL ALARM : " + type);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_REPEATING_REMINDER) ? ID_REPEATING_REMINDER : ID_REPEATING_NEW_FILM;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d("SETTINGS", "CANCEL ALARM : " + String.valueOf(alarmManager));
        }
    }
}
