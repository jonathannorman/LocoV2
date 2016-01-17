package fragments;

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

import model.OutPerson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import se.loco.app.R;
import se.loco.app.ViewBuddy;
import utils.ConnectUtils;
import utils.Globals;
import utils.MyApplication;
import utils.OutPersonParser;
import adapters.OutListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;

public class OutFragment extends ListFragment {

	final Session session = Session.getActiveSession();
	List<OutPerson> outPersontList = new LinkedList<OutPerson>();
	String showN;

	SharedPreferences prefs;
	Editor edit;
	ProgressBar progressOut;
	TextView txtNoOut;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.out_fragment, container,
				false);

		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initialize();
		new OutListAsyncTask().execute();

	}

	private void initialize() {
		// TODO Auto-generated method stub
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		progressOut = (ProgressBar) getActivity().findViewById(
				R.id.progress_out);
		// txtNoOut = (TextView) getActivity().findViewById(R.id.txt_no_out);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public class OutListAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		String from, to;

		MyApplication myApplication = (MyApplication) getActivity()
				.getApplication();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressOut.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";
			String userId = prefs.getString(Globals.userId, null);

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
				sendString = "http://afuriqa.com/loco/outPersonList.php?id="
						+ URLEncoder.encode(userId, "UTF-8") + "&from_date="
						+ URLEncoder.encode(from, "UTF-8")
						+ "&to_date=" + URLEncoder.encode(to, "UTF-8")
						+ "&current_date="
						+ URLEncoder.encode(todayString, "UTF-8");
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

			Log.e("Error here ..", jsonString);

			OutPersonParser outPersonParser = new OutPersonParser();
			outPersontList = outPersonParser.getOutPersonList(jsonString);
			try {
				ListView listOfOut = (ListView) getActivity().findViewById(
						android.R.id.list);

				OutListAdapter adapter = new OutListAdapter(getActivity(),
						R.layout.single_out, outPersontList);
				listOfOut.setAdapter(adapter);
				// if (adapter.getCount() != 0) {
				listOfOut.setAdapter(adapter);
				// } else {
				// listOfOut.setVisibility(View.GONE);
				//
				// txtNoOut.setVisibility(View.VISIBLE);
				//
				// // Toast.makeText(getActivity(), "No Items Available",
				// // Toast.LENGTH_SHORT).show();
				// }

				setListViewHeightBasedOnChildren(listOfOut);

				// Log.e("************", jsonString);

				// for (OutPerson out : outPersontList) {
				// Log.e("name",
				// out.getNames() + " bac," + out.getBac() + "bac ,"
				// + out.getBac() + " ,latitude "
				// + out.getLatitude() + " , longitude "
				// + out.getLongitude() + " , friendId "
				// + out.getFriendId());
				//
				// }

				progressOut.setVisibility(View.INVISIBLE);
			} catch (Exception e) {
				progressOut.setVisibility(View.INVISIBLE);
				Log.e("This is the error man ", e.toString());
			}

		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		OutPerson out = outPersontList.get(position);

		Intent addBuddyIntent = new Intent(getActivity(), ViewBuddy.class);
		addBuddyIntent.putExtra(Globals.friendID, out.getFriendId());
		addBuddyIntent.putExtra(Globals.names, out.getNames());
		addBuddyIntent.putExtra(Globals.bac, out.getBac());
		addBuddyIntent.putExtra(Globals.latitude, out.getLatitude());
		addBuddyIntent.putExtra(Globals.longitude, out.getLongitude());
		startActivity(addBuddyIntent);

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
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
		listView.setLayoutParams(params);
	}

}
