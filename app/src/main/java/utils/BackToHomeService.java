package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class BackToHomeService extends IntentService {

	String userId;
	SharedPreferences prefs;
	Editor edit;

	public BackToHomeService() {
		super("BackToHomeService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		userId = prefs.getString(Globals.userId, null);
		edit = prefs.edit();

		new UpdateToHomeAsyncTask().execute(userId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public class UpdateToHomeAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		String from, to;

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

			// date
			DateFormat dateFormat = new SimpleDateFormat(Globals.dateTimeFormat);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			Date yestardayDate = new Date();
			Date todayDate = new Date();
			Date tomorrowDate = new Date();
			Date todayDateNine = new Date();

			String todayString = dateFormat.format(today);

			Calendar c = Calendar.getInstance();
			String todayNineString = sdf.format(c.getTime()) + " 09:00:00";

			c.add(Calendar.DATE, -1);
			String yestardayString = sdf.format(c.getTime()) + " 09:00:00";

			c.add(Calendar.DATE, 2);

			String tomorrowdayString = sdf.format(c.getTime()) + " 09:00:00";

			try {
				yestardayDate = dateFormat.parse(yestardayString);
				todayDate = dateFormat.parse(todayString);
				tomorrowDate = dateFormat.parse(tomorrowdayString);
				todayDateNine = dateFormat.parse(todayNineString);
			} catch (ParseException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			if (todayDate.after(todayDateNine)) {

				// from today 9Am to tomorrow
				from = todayNineString;
				to = tomorrowdayString;
			} else {
				// from yestarday 9Am to today 9AM
				from = yestardayString;
				to = todayNineString;
			}

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/updateBackToHome.php?id="
						+ URLEncoder.encode(user_id, "UTF-8") + "&from="
						+ URLEncoder.encode(from, "UTF-8") + "&to="
						+ URLEncoder.encode(to, "UTF-8")

				;

				Log.e("url here :", sendString);
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
				Log.e("fdfd", jsonString);

				ResultParser resultParser = new ResultParser();
				Result result = resultParser.getParsedResults(jsonString);

				if (result.getError().equals("sucess")) {

					Log.e("error",
							"**********************************************");

					edit.putString(Globals.plan, Globals.onHome);
					edit.commit();

				} else {

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
