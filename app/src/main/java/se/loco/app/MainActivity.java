package se.loco.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import model.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;

import utils.BackToHomeAlarmManagerBroadcastReceiver;
import utils.ConnectUtils;
import utils.Globals;
import utils.MyApplication;
import utils.ResultParser;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

public class MainActivity extends FragmentActivity {

	private LoginButton loginBtn;

	private TextView userName;

	String test;
	private UiLifecycleHelper uiHelper;

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");

	private static String message = "Sample status posted from android app";

	ProgressDialog pd;

	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;

	String id, name;

	SharedPreferences prefs;
	Editor edit;

	Session session = Session.getActiveSession();
	private BackToHomeAlarmManagerBroadcastReceiver backToHomeAlarm;

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {

				// Intent i = new Intent(getApplicationContext(),
				// TabsActivity.class);
				// startActivity(i);

				Log.d("FacebookSampleActivity", "Facebook session opened");
			} else if (state.isClosed()) {

				Log.d("FacebookSampleActivity", "Facebook session closed");
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		edit = prefs.edit();
		edit.putString(Globals.onStart, Globals.onStartNo);
		edit.commit();

		if (prefs.getString(Globals.userId, null) != null
				&& !prefs.getString(Globals.userId, null).isEmpty()
				&& session != null && session.isOpened()) {

			Log.e("error ", "*****here******passed*****");

			Intent intent = new Intent(getApplicationContext(),
					TabsActivity.class);
			startActivity(intent);

		} else {

			Log.e("error ", "*****be******passed*****");

			uiHelper = new UiLifecycleHelper(this, statusCallback);
			uiHelper.onCreate(savedInstanceState);

			setContentView(R.layout.activity_main);
			ActionBar bar = getActionBar();
			// pd = new ProgressDialog(getApplicationContext());
			bar.hide();

			userName = (TextView) findViewById(R.id.user_name);
			loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
			loginBtn.setReadPermissions("user_friends");
			loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
				@Override
				public void onUserInfoFetched(GraphUser user) {
					if (user != null) {

						name = user.getName();
						id = user.getId();
						edit.putString(Globals.userId, id);
						// edit.putString(Globals.gender, Globals.male);
						// String weigh = prefs.getString(Globals.weight, null);
						// String gender = prefs.getString(Globals.gender,
						// null);
						// if (weigh != null && !weigh.isEmpty()) {
						// edit.putString(Globals.weight, weigh);
						// } else {
						// edit.putString(Globals.weight, "1");
						// }
						//
						// if (gender != null && !gender.isEmpty()) {
						// edit.putString(Globals.gender, gender);
						// } else {
						// edit.putString(Globals.gender, Globals.male);
						// }

						edit.commit();

						backToHomeAlarm = new BackToHomeAlarmManagerBroadcastReceiver();

						if (backToHomeAlarm != null) {

							backToHomeAlarm.SetAlarm(getApplicationContext());

						}

						// Toast.makeText(
						// getApplicationContext(),
						// "name: " + user.getName() + " ,Id :" + user.getId(),
						// Toast.LENGTH_LONG).show();
						// run async task here

						new UserVerificationAsyncTask().execute(id, name);

						// Intent i = new Intent(getApplicationContext(),
						// TabsActivity.class);
						// startActivity(i);

					} else {
						// userName.setText("You are not logged");
					}
				}
			});
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Session.getActiveSession().onActivityResult(this, requestCode,
		// resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}

	public class UserVerificationAsyncTask extends
			AsyncTask<String, Void, String> {

		String sendString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// pd.setCancelable(false);
			// pd.setIndeterminate(true);
			// pd.show();
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			String id = params[0];
			String name = params[1];

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED ";

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(Globals.dateTimeFormat);
			String date = sdf.format(c.getTime());

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/checkUsers.php?id="
						+ URLEncoder.encode(id, "UTF-8") + "&name="
						+ URLEncoder.encode(name, "UTF-8") + "&date="
						+ URLEncoder.encode(date, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			HttpGet httpGet = new HttpGet(sendString);

			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					result = builder.toString();

				} else {

				}
			} catch (ConnectTimeoutException e) {

				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;

			//

		}

		@Override
		protected void onPostExecute(String jsonString) {
			super.onPostExecute(jsonString);
			// REMOVE DIALOG
			// if (pd != null) {
			// pd.dismiss();
			// // b.setEnabled(true);
			// }
			try {
				ResultParser resultParser = new ResultParser();
				Result result = resultParser.getParsedResults(jsonString);

				if (result.getError().contains("sucess")) {

					MyApplication myApplication = (MyApplication) getApplication();
					myApplication.setId(id);
					myApplication.setName(name);

					Intent i = new Intent(getApplicationContext(),
							TabsActivity.class);
					startActivity(i);

				} else if (result.getError().contains("failed")) {

					Toast.makeText(
							getApplicationContext(),
							"result: " + result.getError() + " ,Message :"
									+ result.getMessage(), Toast.LENGTH_LONG)
							.show();

				} else {

				}
			} catch (Exception e) {

			}

		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}