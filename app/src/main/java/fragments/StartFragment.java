package fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import se.loco.app.R;
import se.loco.app.SearchFriendsActivity;
import utils.AlarmManagerBroadcastReceiver;
import utils.ConnectUtils;
import utils.GPSTracker;
import utils.Globals;
import utils.ResultParser;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class StartFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener, OnCheckedChangeListener {

	ToggleButton toggleBtnHomeHarty;
	String userId;
	String onParty;
	String percent = "1";
	SeekBar seekBar;
	ImageView imgAddBuddys;
	boolean saveFragment;
	SharedPreferences prefs;
	Editor edit;

	LocationManager lm;
	String provider;
	Location l;

	double lng;
	double lat;
	GPSTracker gps;
	// TextView txtHome;

	ViewGroup viewGroup;
	private AlarmManagerBroadcastReceiver alarm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.start_fragment, container,
				false);
		// Log.e("error", "onCreateView");

		return rootView;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {

			saveFragment = true;

		}

		// Log.e("error", "onCreate");

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

		// if (saveFragment) {

		userId = prefs.getString(Globals.userId, null);

		// create class object
		gps = new GPSTracker(getActivity());

		if (isChecked) {

			if (gps.canGetLocation()) {

				if (prefs.getString(Globals.onStart, null).equals(
						Globals.onStartYes)) {

					onParty = "0";
					if (alarm != null) {

						alarm.SetAlarm(getActivity());

					}

					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();
					
					Log.e("latitude", String.valueOf(latitude));
					Log.e("longitude", String.valueOf(longitude));
					

					new UpdateStatusAsyncTask()
							.execute(userId, onParty, String.valueOf(latitude),
									String.valueOf(longitude));
					edit.putString(Globals.plan, Globals.onParty);
					edit.commit();

				}

			} else {
				gps.showSettingsAlert();
			}

		} else {
			if (prefs.getString(Globals.onStart, null).equals(
					Globals.onStartYes)) {
				onParty = "1";
				alarm.CancelAlarm(getActivity().getApplicationContext());
				new UpdateStatusAsyncTask().execute(userId, onParty,
						String.valueOf("0.0"), String.valueOf("0.0"));
				edit.putString(Globals.plan, Globals.onHome);
				edit.commit();
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		// Log.e("error", "onSaveInstanceState");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initialize();
		// Log.e("error", "onActivityCreated");
	}

	private void initialize() {
		// TODO Auto-generated method stub
		toggleBtnHomeHarty = (ToggleButton) getActivity().findViewById(
				R.id.toggle_btn_home_party);
		// viewGroup = (ViewGroup) getActivity().findViewById(
		// R.id.layout_start_fragment);
		toggleBtnHomeHarty.setOnCheckedChangeListener(this);
		seekBar = (SeekBar) getActivity().findViewById(R.id.sbPartyLevel);
		seekBar.setOnSeekBarChangeListener(this);
		imgAddBuddys = (ImageView) getActivity().findViewById(
				R.id.img_add_buddys);
		// txtHome = (TextView) getActivity().findViewById(R.id.txt_home);
		imgAddBuddys.setOnClickListener(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		edit = prefs.edit();

		Typeface custom_font = Typeface.createFromAsset(getActivity()
				.getAssets(), "Rajdhani-SemiBold.ttf");
		alarm = new AlarmManagerBroadcastReceiver();
		// viewGroup.setTypeface(custom_font);

		if (prefs.getString(Globals.plan, null) != null
				&& prefs.getString(Globals.plan, null).equals(Globals.onHome)) {
			toggleBtnHomeHarty.setChecked(false);
		}

		if (prefs.getString(Globals.plan, null) != null
				&& prefs.getString(Globals.plan, null).equals(Globals.onParty)) {
			toggleBtnHomeHarty.setChecked(true);
		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		percent = Integer.toString(progress);

		if (progress < 1)
			seekBar.setProgress(1);

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		userId = prefs.getString(Globals.userId, null);
		new LevelAsyncTask().execute(userId, percent);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.img_add_buddys:

			// Toast toast = Toast.makeText(getActivity(),
			// "SearchFriendsActivity", Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.CENTER, 0, 0);
			// toast.show();

			Intent addBuddyIntent = new Intent(getActivity(),
					SearchFriendsActivity.class);
			startActivity(addBuddyIntent);
			break;
		}

	}

	public class UpdateStatusAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		ProgressBar progressStart;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressStart = (ProgressBar) getActivity().findViewById(
					R.id.progressBar);
			progressStart.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";
			String user_id = params[0];
			String on_party = params[1];
			String latitude = params[2];
			String longitude = params[3];

			// date

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(Globals.dateTimeFormat);
			String date = sdf.format(c.getTime());

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/changeStatus.php?id="
						+ URLEncoder.encode(user_id, "UTF-8") + "&on_party="
						+ URLEncoder.encode(on_party, "UTF-8") + "&date="
						+ URLEncoder.encode(date, "UTF-8") + "&latitude="
						+ URLEncoder.encode(latitude, "UTF-8") + "&longitude="
						+ URLEncoder.encode(longitude, "UTF-8");
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
			progressStart.setVisibility(View.INVISIBLE);

			try {

				ResultParser resultParser = new ResultParser();
				Result result = resultParser.getParsedResults(jsonString);

				if (result.getError().contains("sucess")
						&& result.getMessage().contains("Home")) {

					Toast toast = Toast.makeText(getActivity(), "Home",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

				} else if (result.getError().contains("sucess")
						&& result.getMessage().contains("Party")) {

					Toast toast = Toast.makeText(getActivity(), "Party",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

				}

				else if (result.getError().contains("failed")) {

					Toast toast = Toast.makeText(getActivity(),
							"Error retry !!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

				} else {

				}

			} catch (Exception e) {

			}

		}
	}

	public class LevelAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		ProgressBar progressLevel;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressLevel = (ProgressBar) getActivity().findViewById(
					R.id.progressBarLevel);
			progressLevel.setVisibility(View.VISIBLE);
			progressLevel.setClickable(false);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";
			String user_id = params[0];
			String percent = params[1];

			// date

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(Globals.dateTimeFormat);
			String date = sdf.format(c.getTime());

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/partyLevel.php?id="
						+ URLEncoder.encode(user_id, "UTF-8") + "&party_level="
						+ URLEncoder.encode(percent, "UTF-8") + "&date="
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
			progressLevel.setVisibility(View.GONE);

			ResultParser resultParser = new ResultParser();
			Result result = resultParser.getParsedResults(jsonString);

			if (result.getError().contains("sucess")) {

				Toast toast = Toast.makeText(getActivity(),
						result.getMessage(), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else {
				Toast toast = Toast.makeText(getActivity(), "Error retry !!",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			}

		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		edit.putString(Globals.onStart, Globals.onStartYes);
		edit.commit();
		// Log.e("error", "onResume");

	}

}
