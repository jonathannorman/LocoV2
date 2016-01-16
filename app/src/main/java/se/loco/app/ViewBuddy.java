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
import java.util.Locale;

import model.OutPersonDrink;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import utils.ConnectUtils;
import utils.Globals;
import utils.OutPersonDrinkParser;
import adapters.DrinkListAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewBuddy extends FragmentActivity {

	Marker marker;
	//private GoogleMap map;
	double latitude, longitude;
	TextView names, bac, address;
	TextView txtNoBeveragesAdded;
	ProgressBar progressDrinkList;
	String friendId;

	List<OutPersonDrink> drinkList = new LinkedList<OutPersonDrink>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_buddy);

		init();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			names.setText(extras.getString(Globals.names));
			bac.setText(extras.getString(Globals.bac));
			//latitude = Double.valueOf(extras.getString(Globals.latitude));
			//longitude = Double.valueOf(extras.getString(Globals.longitude));
			latitude = 37.6329946;
			longitude = -122.4938344;
			friendId = extras.getString(Globals.friendID);

		}

		initilizeMap();

		// new OutListDrinkAsyncTask().execute(friendId);
		new setLocationNameAsyncTask().execute();

	}

	private void init() {
		// TODO Auto-generated method stub
		names = (TextView) findViewById(R.id.text_name);
		bac = (TextView) findViewById(R.id.text_bac);
		address = (TextView) findViewById(R.id.text_address);
		progressDrinkList = (ProgressBar) findViewById(R.id.progress_drink_list);
		txtNoBeveragesAdded = (TextView) findViewById(R.id.txt_no_beverages_added);

	}

	private void initilizeMap() {
		// TODO Auto-generated method stub
		MapsInitializer.initialize(this);

		final GoogleMap map = ((MapFragment)getFragmentManager().findFragmentById(R.id.fragment_map)).getMap();

		LatLng MyCurrentLocation = new LatLng(37.6329946, -122.4938344);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MyCurrentLocation, 18));

		marker = map.addMarker(new MarkerOptions()
				.position(MyCurrentLocation).draggable(true)
				.visible(true));


	}

	//private void initilizeMapAime() {
		// TODO Auto-generated method stub

		//map = ((MapFragment) getFragmentManager().findFragmentById(
				//R.id.fragment_map)).getMap();

		//LatLng MyCurrentLocation = new LatLng(37.6329946, -122.4938344);

		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(MyCurrentLocation, 18));

		//marker = map.addMarker(new MarkerOptions()
				//.position(new LatLng(latitude, longitude)).draggable(true)
				//.visible(true));

		// map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
		// longitude), 8));

	//}



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

	public class setLocationNameAsyncTask extends
			AsyncTask<String, Void, String> {

		String locationName;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			address.setText("waiting for Location ....");
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			Geocoder geoCoder = new Geocoder(getBaseContext(),
					Locale.getDefault());

			try {

				List<Address> address = geoCoder.getFromLocation(latitude,
						longitude, 1);
				if (address.size() > 0) {
					// locationName = address.get(0).getFeatureName() + ", "
					// + address.get(0).getAdminArea() + ", "
					// + address.get(0).getCountryName();

					locationName = address.get(0).getThoroughfare() + " "
							+ address.get(0).getFeatureName() + ", "
							+ address.get(0).getCountryName();

				} else {
					locationName = " no Location name found...";

				}
			} catch (Exception e) {
				locationName = " no Location name found...";
				e.printStackTrace(); // getFromLocation() may sometimes fail
			}

			return locationName;

			//

		}

		@Override
		protected void onPostExecute(String locationName) {
			super.onPostExecute(locationName);

			address.setText(locationName);

			new OutListDrinkAsyncTask().execute(friendId);

		}
	}

	public class OutListDrinkAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		String from, to;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDrinkList.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";
			String fiendId = params[0];

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
				sendString = "http://afuriqa.com/loco/drinkList.php?friendId="
						+ URLEncoder.encode(fiendId, "UTF-8") + "&from_date="
						+ URLEncoder.encode(from, "UTF-8") + "&to_date="
						+ URLEncoder.encode(to, "UTF-8");
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
			try {
				OutPersonDrinkParser outDrinkParser = new OutPersonDrinkParser();
				drinkList = outDrinkParser.getOutPersonDrinkList(jsonString);
				// for (OutPersonDrink out : drinkList) {
				// Log.e("error",
				// "booze " + out.getBoozeType() + ", "
				// + out.getQuantity() + ",time "
				// + out.getTime());
				// }

				ListView listOfDrinks = (ListView) findViewById(android.R.id.list);

				DrinkListAdapter adapter = new DrinkListAdapter(ViewBuddy.this,
						R.layout.single_drink, drinkList);

				if (adapter.getCount() != 0) {
					listOfDrinks.setAdapter(adapter);
					setListViewHeightBasedOnChildren(listOfDrinks);

				} else {
					listOfDrinks.setVisibility(View.GONE);
					txtNoBeveragesAdded.setVisibility(View.VISIBLE);

				}

				setListViewHeightBasedOnChildren(listOfDrinks);

				// ViewGroup.LayoutParams listViewParams =
				// (ViewGroup.LayoutParams) listOfDrinks
				// .getLayoutParams();
				// listViewParams.height = 400;
				// listOfDrinks.requestLayout();


				progressDrinkList.setVisibility(View.INVISIBLE);

			} catch (Exception e) {
				Log.e("This is the error man ", e.toString());
				progressDrinkList.setVisibility(View.INVISIBLE);
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