package se.loco.app;

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
import java.util.LinkedList;
import java.util.List;

import model.OutPersonDrink;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import utils.BacCalulation;
import utils.ConnectUtils;
import utils.Globals;
import utils.OutPersonDrinkParser;
import adapters.DrinkListAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Session;

public class MyDrinkListActivity extends Activity {

	ProgressBar progressMyDrinkList;
	String friendId;
	SharedPreferences prefs;
	TextView txtNoBeveragesAdded, tvBac;

	List<OutPersonDrink> drinkList = new LinkedList<OutPersonDrink>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_drink_list_activity);

		init();



		// new OutListDrinkAsyncTask().execute(friendId);

		new MyDrinkListAsyncTask().execute(prefs
				.getString(Globals.userId, null));

	}

	private void init() {
		// TODO Auto-generated method stub

		progressMyDrinkList = (ProgressBar) findViewById(R.id.progress_my_drink_list);
		txtNoBeveragesAdded = (TextView) findViewById(R.id.txt_no_beverages_added);
		tvBac = (TextView) findViewById(R.id.tv_bac);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

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

	public class MyDrinkListAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		String from, to;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMyDrinkList.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";
			String id = params[0];
			// date

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
				sendString = "http://afuriqa.com/loco/myDrinkList.php?id="
						+ URLEncoder.encode(id, "UTF-8") + "&from_date="
						+ URLEncoder.encode(from, "UTF-8") + "&to_date="
						+ URLEncoder.encode(to, "UTF-8") + "&current_date="
						+ URLEncoder.encode(todayString, "UTF-8");
				Log.e("today date ", sendString);
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



			// Log.i("**********************************************",
			// jsonString);
			// jsonString=jsonString.trim();
			// jsonString = jsonString.replace("\\", "");

			Log.e("", jsonString);
			try {

				OutPersonDrinkParser outDrinkParser = new OutPersonDrinkParser();
				drinkList = outDrinkParser.getOutPersonDrinkList(jsonString);
				BacCalulation bacCalulation = new BacCalulation(jsonString);
				// for (OutPersonDrink out : drinkList) {
				// Log.e("error",
				// "booze " + out.getBoozeType() + ", "
				// + out.getQuantity() + ",time "
				// + out.getTime());
				// }

				ListView listOfDrinks = (ListView) findViewById(android.R.id.list);

				DrinkListAdapter adapter = new DrinkListAdapter(
						MyDrinkListActivity.this, R.layout.single_drink,
						drinkList);

				if (adapter.getCount() != 0) {
					tvBac.setText("Promille: " + drinkList.get(0).getBac() + " ‰ ");
					listOfDrinks.setAdapter(adapter);
					setListViewHeightBasedOnChildren(listOfDrinks);

				} else {
					listOfDrinks.setVisibility(View.GONE);
					txtNoBeveragesAdded.setVisibility(View.VISIBLE);

				}

				// Log.i("*****************************************************",
				// jsonString);

				progressMyDrinkList.setVisibility(View.INVISIBLE);

			} catch (Exception e) {
				// Log.e("This is the error man ", e.toString());
				progressMyDrinkList.setVisibility(View.INVISIBLE);
			}

		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

}