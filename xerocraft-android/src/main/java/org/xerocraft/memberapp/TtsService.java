package org.xerocraft.memberapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Owns the text-to-speech synthesizer.  Putting this in a foreground service
 * helps prevent utterances from being interrupted if the main activity is
 * destroyed when it goes into the background.
 *
 * @author Kevin Krumwiede
 */
public class TtsService extends Service {
	private static final String TAG = TtsService.class.getSimpleName();
	private static final int NOTIFICATION_ID = 489579823; // arbitrary
	private static TtsService gInstance;
	private static String gPendingUtterance;
	private TextToSpeech mTts;
	private boolean mTtsReady;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		gInstance = this;
		mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {			
			@Override
			public void onInit(int status) {
				if(status == TextToSpeech.SUCCESS) {
					Log.d(TAG, "init success");
					mTtsReady = true;
					if(gPendingUtterance != null) {
						speak(gPendingUtterance);
						gPendingUtterance = null;
					}
				}
				else {
					Log.d(TAG, "init failed");
					mTtsReady = false;
				}
			}
		});
		Log.d(TAG, "service created");		

		/* Request foreground status. */
		final Intent intent = new Intent(this, MainActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentTitle(getText(R.string.tts_service))
				.setWhen(System.currentTimeMillis())
				.setContentIntent(pendingIntent);
		final Notification notification;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			notification = builder.build();
		}
		else {
			notification = builder.getNotification();
		}
		this.startForeground(NOTIFICATION_ID, notification);
		
		
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
		stopSelf();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mTts.shutdown();
		gInstance = null;
		Log.d(TAG, "service destroyed");
	}

	/**
	 * Speaks some text.  This method can save up to one utterance if the
	 * service is not running or the synthesizer is not ready.  If this method
	 * is called again before everything is ready, the pending utterance will
	 * be overwritten by the new one.
	 * 
	 * @param utterance the text to speak
	 */
	@SuppressWarnings("deprecation") // speak changed in API 21
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void speak(final String utterance) {
		if(utterance == null || utterance.isEmpty()) {
			return;
		}
		else if(gInstance != null && gInstance.mTtsReady) {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				gInstance.mTts.speak(utterance, TextToSpeech.QUEUE_ADD, null, null);
			}
			else {
				gInstance.mTts.speak(utterance, TextToSpeech.QUEUE_ADD, null);
			}
		}
		else {
			gPendingUtterance = utterance;
		}
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}
	
}
