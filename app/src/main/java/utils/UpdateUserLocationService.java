package utils;

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

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class UpdateUserLocationService extends Service {

	double lng;
	double lat;
	LocationManager lm;
	Location l;
	String provider;
	String userId;
	SharedPreferences prefs;
	GPSTracker gps;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		userId = prefs.getString(Globals.userId, null);

		gps = new GPSTracker(
				UpdateUserLocationService.this.getApplicationContext());

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			// \n is for new line
			new UpdateLocationAsyncTask().execute(userId,
					String.valueOf(latitude), String.valueOf(longitude));

			//

		} else {

//			gps.showSettingsAlert();
		}

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public class UpdateLocationAsyncTask extends
			AsyncTask<String, Void, String> {

		String sendString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

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
			String latitude = params[1];
			String longitude = params[2];

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/updateLocation.php?id="
						+ URLEncoder.encode(user_id, "UTF-8") + "&latitude="
						+ URLEncoder.encode(latitude, "UTF-8") + "&longitude="
						+ URLEncoder.encode(longitude, "UTF-8")

				;

				Log.e("", sendString);
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

			try {

				ResultParser resultParser = new ResultParser();
				Result result = resultParser.getParsedResults(jsonString);

				if (result.getError().contains("sucess")
						&& result.getMessage().contains("Home")) {

					// Toast toast = Toast
					// .makeText(UpdateUserLocationService.this
					// .getApplicationContext(), "Home",
					// Toast.LENGTH_LONG);
					// toast.setGravity(Gravity.CENTER, 0, 0);
					// toast.show();

				} else if (result.getError().contains("sucess")
						&& result.getMessage().contains("Party")) {

					// Toast toast =
					// Toast.makeText(UpdateUserLocationService.this
					// .getApplicationContext(), "Party",
					// Toast.LENGTH_LONG);
					// toast.setGravity(Gravity.CENTER, 0, 0);
					// toast.show();

				}

				else if (result.getError().contains("failed")) {

					// Toast toast =
					// Toast.makeText(UpdateUserLocationService.this
					// .getApplicationContext(), "Error retry !!",
					// Toast.LENGTH_LONG);
					// toast.setGravity(Gravity.CENTER, 0, 0);
					// toast.show();

				} else {

				}

			} catch (Exception e) {

			}

		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
