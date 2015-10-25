package utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class BackToHomeAlarmManagerBroadcastReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();

		Intent i = new Intent();
		i.setClass(context, BackToHomeService.class);
		context.startService(i);

		// Release the lock
		wl.release();

	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, BackToHomeAlarmManagerBroadcastReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.FALSE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		// After after 30 seconds
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 1 * 10, pi);
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, BackToHomeAlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	public void setOnetimeTimer(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, BackToHomeAlarmManagerBroadcastReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.TRUE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
	}

}
