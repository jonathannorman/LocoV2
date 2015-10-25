package se.loco.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import model.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import com.facebook.Session;

import utils.ConnectUtils;
import utils.Globals;
import utils.ResultParser;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

public class Settings extends Activity {

	SharedPreferences prefs;
	EditText tvWeight;
	Editor edit;
	RadioButton radioMale, radioFemale;
	String genderInt;
	String genderType;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		edit = prefs.edit();

		tvWeight = (EditText) findViewById(R.id.weight);
		radioMale = (RadioButton) findViewById(R.id.radio_male);
		radioFemale = (RadioButton) findViewById(R.id.radio_female);

		if (prefs.getString(Globals.gender, null) != null
				&& !prefs.getString(Globals.gender, null).isEmpty()) {

			if (prefs.getString(Globals.gender, null).equals(Globals.male)) {
				radioMale.setChecked(true);
				genderType = Globals.male;
				genderInt = "1";
			}

			if (prefs.getString(Globals.gender, null).equals(Globals.female)) {
				radioFemale.setChecked(true);
				genderType = Globals.female;
				genderInt = "0";
			}

		}

		if (prefs.getString(Globals.weight, null) != null
				&& !prefs.getString(Globals.weight, null).isEmpty()) {
			tvWeight.setText(prefs.getString(Globals.weight, null));
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
	}

	public void onSaveSettingsClick(View view) {

		if (tvWeight.getText().toString() == null
				|| tvWeight.getText().toString().isEmpty()) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Enter Weight", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		} else if (prefs.getString(Globals.gender, null) == null
				&& genderInt == null) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Select Gender", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		} else {
			new AddSettingsAsyncTask().execute(genderInt);
		}

	}

	public void onGenderClick(View v) {

		switch (v.getId()) {

		case R.id.radio_male:
			genderInt = "1";
			break;

		case R.id.radio_female:
			genderInt = "0";
			break;

		}

	}

	public void layoutClick(View v) {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public class AddSettingsAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		ProgressBar progressAddSettings;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressAddSettings = (ProgressBar) findViewById(R.id.progress_add_settings);
			progressAddSettings.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();
			String gender = params[0];

			if (Integer.valueOf(gender) == 1) {
				// male
				genderType = Globals.male;
			} else {
				// female
				genderType = Globals.female;
			}

			String result = "FAILED";

			// date

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/addSettings.php?id="
						+ URLEncoder.encode(
								prefs.getString(Globals.userId, null), "UTF-8")
						+ "&weight="
						+ URLEncoder.encode(tvWeight.getText().toString(),
								"UTF-8") + "&gender="
						+ URLEncoder.encode(genderType, "UTF-8");
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
			progressAddSettings.setVisibility(View.INVISIBLE);

			ResultParser resultParser = new ResultParser();
			Result result = resultParser.getParsedResults(jsonString);

			if (result.getError().contains("sucess")) {
				edit.putString(Globals.weight, tvWeight.getText().toString());
				if (genderType.equals(Globals.male)) {
					edit.putString(Globals.gender, Globals.male);
				} else {
					edit.putString(Globals.gender, Globals.female);
				}

				edit.commit();

				Toast toast = Toast.makeText(getApplicationContext(),
						"Settings added", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else if (result.getError().contains("failed")) {

				Toast toast = Toast.makeText(getApplicationContext(),
						"Error retry !!", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else {

			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// action with ID action_settings was selected
		case R.id.menuSettings:
			Intent intent = new Intent(getApplicationContext(), Settings.class);
			startActivity(intent);
			break;

		case R.id.menuMyDrinkList:

			Intent intenti = new Intent(getApplicationContext(),
					MyDrinkListActivity.class);
			startActivity(intenti);
			break;

		case R.id.menuLogout:

			callFacebookLogout(getApplicationContext());

			break;
		default:
			break;
		}

		return true;
	}

	public static void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved

				Intent i = new Intent();
				i.setClass(context, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);

			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			// clear your preferences if saved

			Intent i = new Intent();
			i.setClass(context, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		}

	}

}