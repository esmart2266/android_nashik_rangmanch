package in.gov.nmc.rangmach;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Random;


public class AlarmReceiver extends BroadcastReceiver {
    App app;

    @Override
    public void onReceive(Context context, Intent intent) {
        app = (App) context.getApplicationContext();
        Random r = new Random();
        int randomNumber = r.nextInt(100);
//        Log.e("bundle"," received: "+ intent.getExtras().getString("title", ""));
//        Log.e("intent"," received: "+ intent.getStringExtra("title"));
        sendNotification(intent.getExtras().getString("title"), randomNumber, context);
        //Toast.makeText(context, context.getString(R.string.Alertnotifty) + intent.getStringExtra("title") , Toast.LENGTH_LONG).show();
//        String Title = intent.getStringExtra(context.getString(R.string.titttle));
//        Intent x = new Intent(context, Alert.class);
//        x.putExtra(context.getString(R.string.titttle), Title);
//        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(x);
    }

    private void sendNotification(String message, int m, Context mContext) {
        Intent intent;
        if (app.getPreferences().isLoggedInUser()) {
            intent = new Intent(mContext, MainActivity.class);
        } else {
            intent = new Intent(mContext, LoginActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_ONE_SHOT);
        Notification n;
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.order_beep);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "id_doctor_network");
        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setLargeIcon(largeIcon);
//            notificationBuilder.setSmallIcon(R.mipmap.app_icon_noti);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        notificationBuilder.setVibrate(new long[]{1000, 1000});
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        notificationBuilder.setContentTitle(mContext.getResources().getString(R.string.app_name) + " - Reminder");
        notificationBuilder.setContentIntent(pendingIntent);

        n = notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message)).build();
        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "id_doctor_network";// The id of the channel.
            CharSequence name = "id_doctor_network";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationBuilder.setChannelId(CHANNEL_ID);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
    }
}