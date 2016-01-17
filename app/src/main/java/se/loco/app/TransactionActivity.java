package se.loco.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import utils.AlarmManagerBroadcastReceiver;
import utils.BackToHomeAlarmManagerBroadcastReceiver;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import com.facebook.Session;

public class TransactionActivity extends Activity {

	SharedPreferences prefs;
	EditText tvWeight;

	// private BackToHomeAlarmManagerBroadcastReceiver backToHomeAlarm;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction_activity);
		ActionBar bar = getActionBar();
		bar.hide();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (isLoggedIn()) {
			Intent intent = new Intent(getApplicationContext(),
					TabsActivity.class);
			startActivity(intent);
			Log.e("TabsActivity 1", " TabsActivity1");
		} else {

			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
			Log.e("MainActivity 1", "MainActivity1");

		}

	}

	public boolean isLoggedIn() {
		Session session = Session.getActiveSession();
		return (session != null && session.isOpened());
	}

	public void printHashKey() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"se.loco.app", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("TEMPTAGHASH KEY:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

	}

}