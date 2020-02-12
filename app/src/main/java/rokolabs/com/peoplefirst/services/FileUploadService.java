package rokolabs.com.peoplefirst.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import javax.inject.Inject;

import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.api.PeopleFirstService;
import rokolabs.com.peoplefirst.di.ComponentManager;
import rokolabs.com.peoplefirst.di.component.DaggerServiceComponent;
import rokolabs.com.peoplefirst.repository.HarassmentRepository;

public class FileUploadService extends Service {

	private static final String TAG = FileUploadService.class.getSimpleName();

	@Inject
	public PeopleFirstService peopleFirstService;
	@Inject
	public HarassmentRepository repository;

	private TransferUtility transferUtility;
	private LinkedList<File> filesToUpload = new LinkedList<>();
	private TransferObserver currentObserver;

	private NotificationManager notificationManager;
	private String notificationChannelId = "rokolabs.com.peoplefirst.services.FileUploadService";
	private int toUploadAmount = 0;
	private int uploadedAmount = 0;
	private int bytesTotal = 0;
	private int bytesCurrent = 0;
	private int bytesCurrentFile = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("BLABLA", "service created");
		DaggerServiceComponent.builder()
				.appComponent(ComponentManager.getInstance().getAppComponent())
				.build()
				.inject(this);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) return super.onStartCommand(intent, flags, startId);

		if ("STOP_UPLOAD".equals(intent.getAction())) {
			onStopClicked();
		} else {
			startTransferServiceIfNotStarted();
			AWSMobileClient.getInstance().initialize(this, new Callback<UserStateDetails>() {
				@Override
				public void onResult(UserStateDetails userStateDetails) {
					Log.d(TAG, "AWSMobileClient initialized. User State is " + userStateDetails.getUserState());
					if (intent == null) return;

					transferUtility = TransferUtility.builder()
							.context(getApplicationContext())
							.awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
							.s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
							.build();

					ArrayList<File> files = (ArrayList<File>) intent.getSerializableExtra("files");
					if (files != null) {
						filesToUpload.addAll(files);
						toUploadAmount += files.size();
					}
					for (File file : filesToUpload) {
						bytesTotal += file.length();
					}
					if (currentObserver == null) {
						initUploadNextFile();
					}
				}

				@Override
				public void onError(Exception e) {
					Log.e(TAG, "AWSMobileClient initialization error.", e);
				}
			});
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void onStopClicked() {
		currentObserver = null;
		filesToUpload = new LinkedList<>();
		repository.urlSenders = new ArrayList<>();
		repository.currentReport.getValue().attachments.clear();
		transferUtility.cancelAllWithType(TransferType.ANY);
		stopSelf();
		stopService(new Intent(getApplicationContext(), TransferService.class));
		notificationManager.notify(123123, createNotification("Upload has been cancelled", 100, false));
		Intent intent = new Intent();
		intent.setAction("UPLOADING_UPDATE");
		intent.putExtra("uploadingInfo", "Upload has been cancelled");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void startTransferServiceIfNotStarted() {
		ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (TransferService.class.getName().equals(service.service.getClassName())) {
				return;
			}
		}

		if (Build.VERSION.SDK_INT >= 26) {
			if (notificationManager.getNotificationChannel(notificationChannelId) == null) {
				NotificationChannel channel = new NotificationChannel(notificationChannelId, "File upload", NotificationManager.IMPORTANCE_LOW);
				notificationManager.createNotificationChannel(channel);
			}

			Intent tsIntent = new Intent(getApplicationContext(), TransferService.class);
			tsIntent.putExtra(TransferService.INTENT_KEY_NOTIFICATION, createNotification("", 0, true));
			tsIntent.putExtra(TransferService.INTENT_KEY_NOTIFICATION_ID, 123123);
			tsIntent.putExtra(TransferService.INTENT_KEY_REMOVE_NOTIFICATION, true);
			getApplicationContext().startForegroundService(tsIntent);
		} else {
			getApplicationContext().startService(new Intent(getApplicationContext(), TransferService.class));
		}
	}

	private void initUploadNextFile() {
		File file = filesToUpload.peekFirst();
		if (file == null) {
			stopService(new Intent(getApplicationContext(), TransferService.class));
			notificationManager.notify(123123, createNotification(uploadedAmount + " of " + toUploadAmount + " files uploaded (100%)", 100, false));
			return;
		}
		String key = UUID.randomUUID().toString() + "-" + file.getName();
		String folderName = "user-" + repository.me.getValue().id;
		String bucketName = String.valueOf(repository.me.getValue().company.storage_bucket);
		String url = "https://s3.amazonaws.com/" + bucketName + "/" + folderName + "/" + key;

		currentObserver = transferUtility.upload(bucketName, folderName + "/" + key, file, CannedAccessControlList.PublicRead);
		currentObserver.setTransferListener(new L(this));
		repository.urlSenders.add(new HarassmentRepository.UrlSender(repository, file, bucketName, folderName, key, currentObserver.getId(), url));
		Log.d("BLABLA", "started observer " + currentObserver.getId());
	}

	private void updateInfo() {
		float percent = 0;

		if (bytesTotal != 0) {
			float current = bytesCurrent;
			float total = bytesTotal;
			percent = current / total * 100;
		}

		String info = uploadedAmount + " of " + toUploadAmount + " files uploaded (" + (int) percent + "%)";
		Log.d(TAG, info);

		notificationManager.notify(123123, createNotification(info, (int) percent, true));
		Intent intent = new Intent();
		intent.setAction("UPLOADING_UPDATE");
		intent.putExtra("uploadingInfo", info);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private Notification createNotification(String text, int percent, boolean showStop) {
		Intent intent = new Intent(this, FileUploadService.class);
		intent.setAction("STOP_UPLOAD");

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
		builder.setContentTitle("People First")
				.setContentText(text)
				.setSmallIcon(R.mipmap.group_366)
				.setPriority(NotificationCompat.PRIORITY_LOW);
		if (showStop) {
			builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", PendingIntent.getService(this, 0, intent, 0));
		}

		if (percent < 100) {
			builder.setProgress(100, percent, false);
		}

		return builder.build();
	}

	static class L implements TransferListener {

		private WeakReference<FileUploadService> fileUploadService;

		public L(FileUploadService fileUploadService) {
			this.fileUploadService = new WeakReference<>(fileUploadService);
		}

		@Override
		public void onStateChanged(int id, TransferState state) {
			Log.d("BLABLA", "observer " + id + " state " + state.toString());
			if (state == TransferState.COMPLETED) {
				for (HarassmentRepository.UrlSender sender : fileUploadService.get().repository.urlSenders) {
					if (id == sender.observerId) {
						sender.setUploadFinished();
						sender.trySendingUrl();
					}
				}

				fileUploadService.get().uploadedAmount++;
				fileUploadService.get().bytesCurrentFile = 0;
				fileUploadService.get().currentObserver = null;
				fileUploadService.get().filesToUpload.removeFirst();

				fileUploadService.get().updateInfo();
				fileUploadService.get().initUploadNextFile();
			}
			if (state == TransferState.FAILED) {
				for (HarassmentRepository.UrlSender sender : fileUploadService.get().repository.urlSenders) {
					if (id == sender.observerId) {
						fileUploadService.get().bytesCurrent -= fileUploadService.get().currentObserver.getBytesTransferred();
						fileUploadService.get().currentObserver = fileUploadService.get().transferUtility.upload(sender.bucketName, sender.folderName + "/" + sender.key, sender.file, CannedAccessControlList.PublicRead);
						fileUploadService.get().currentObserver.setTransferListener(new L(fileUploadService.get()));
						sender.observerId = fileUploadService.get().currentObserver.getId();
					}
				}
			}
		}

		@Override
		public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
			fileUploadService.get().bytesCurrent += bytesCurrent - fileUploadService.get().bytesCurrentFile;
			fileUploadService.get().bytesCurrentFile = (int) bytesCurrent;
			fileUploadService.get().updateInfo();
		}

		@Override
		public void onError(int id, Exception ex) {
			ex.printStackTrace();
		}
	}
}