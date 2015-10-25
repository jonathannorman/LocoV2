package fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import model.All;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import se.loco.app.R;
import utils.AllParser;
import utils.ConnectUtils;
import utils.Globals;
import adapters.AllListAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.Session;

public class AllFragment extends ListFragment {

	ProgressBar progressAll;
	final Session session = Session.getActiveSession();
	List<All> allList = new LinkedList<All>();
	String showN;

	SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.all_fragment, container,
				false);

		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initialize();
		new AllListAsyncTask().execute();

	}

	private void initialize() {
		// TODO Auto-generated method stub
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		progressAll = (ProgressBar) getActivity().findViewById(
				R.id.progress_all);

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

	public class AllListAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressAll.setVisibility(View.VISIBLE);
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

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/all.php?id="
						+ URLEncoder.encode(userId, "UTF-8");
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
				AllParser allListParser = new AllParser();
				allList = allListParser.getAllList(jsonString);

				ListView listOfAll = (ListView) getActivity().findViewById(
						android.R.id.list);
				AllListAdapter adapter = new AllListAdapter(getActivity(),
						R.layout.single_all, allList);
				listOfAll.setAdapter(adapter);
				setListViewHeightBasedOnChildren(listOfAll);

				Log.i("*****************************************************",
						jsonString);

				for (All all : allList) {
					Log.i("name", all.getNames() + " id," + all.getId());
				}

				progressAll.setVisibility(View.INVISIBLE);

			} catch (Exception e) {
			}

		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// All all = allList.get(position);
		// Toast toast = Toast.makeText(getActivity(), "name :" + all.getNames()
		// + ", ID " + all.getId(), Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();

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
