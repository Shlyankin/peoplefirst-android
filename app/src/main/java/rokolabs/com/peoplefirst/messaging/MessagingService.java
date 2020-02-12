package rokolabs.com.peoplefirst.messaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.SplashActivity;

public class MessagingService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Intent intent = new Intent(this, SplashActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		int notifyID = 1;
		String CHANNEL_ID = "pf_channel";// The id of the channel.
		CharSequence name = "people first";// The user-visible name of the channel.
		int importance = NotificationManager.IMPORTANCE_HIGH;
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			mNotificationManager.createNotificationChannel(mChannel);
		}


		Notification notification = new NotificationCompat.Builder(this, "rokolabs.com.peoplefirst")
				.setContentTitle("People First")
				.setContentText(remoteMessage.getNotification().getBody())
				.setSmallIcon(R.mipmap.group_366)
				.setContentIntent(pendingIntent)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setAutoCancel(true)
				.setChannelId(CHANNEL_ID)
				.build();

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
		notificationManager.notify(0, notification);
	}

	@Override
	public void onNewToken(String s) {
		super.onNewToken(s);
		Log.d("MessagingService", "new token = " + s);
	}
}
