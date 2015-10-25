package fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import model.Request;
import model.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import se.loco.app.R;
import utils.ConnectUtils;
import utils.Globals;
import utils.RequestParser;
import utils.ResultParser;
import adapters.RequestListAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;

public class RequestFragment extends ListFragment {

	ProgressBar progressRequest;
	final Session session = Session.getActiveSession();
	List<Request> requestList = new LinkedList<Request>();
	List<Request> newRequestList = new LinkedList<Request>();
	String showN;

	SharedPreferences prefs;
	Editor edit;
	TextView txtNoRequest;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.request_fragment, container,
				false);

		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initialize();
		new RequestListAsyncTask().execute();

	}

	private void initialize() {
		// TODO Auto-generated method stub
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		progressRequest = (ProgressBar) getActivity().findViewById(
				R.id.progress_request);
		txtNoRequest = (TextView) getActivity().findViewById(
				R.id.txt_no_request);

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

	public class RequestListAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressRequest.setVisibility(View.VISIBLE);
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
				sendString = "http://afuriqa.com/loco/request.php?id="
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

			try {

				RequestParser requestListParser = new RequestParser();
				requestList = requestListParser.getRequestList(jsonString);

				ListView listOfRequest = (ListView) getActivity().findViewById(
						android.R.id.list);
				RequestListAdapter adapter = new RequestListAdapter(
						getActivity(), R.layout.single_request, requestList);

				if (adapter.getCount() != 0) {
					listOfRequest.setAdapter(adapter);
				} else {
					listOfRequest.setVisibility(View.GONE);
					txtNoRequest.setVisibility(View.VISIBLE);
				}
				listOfRequest.setAdapter(adapter);

				setListViewHeightBasedOnChildren(listOfRequest);

//				Log.i("*****************************************************",
//						jsonString);

//				for (Request request : requestList) {
//					Log.i("name",
//							request.getNames() + " status,"
//									+ request.getStatus() + "id ,"
//									+ request.getId());
//				}

				progressRequest.setVisibility(View.INVISIBLE);

			} catch (Exception e) {
			}

		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Request request = requestList.get(position);
		// accept friend here
		if (request.getStatus().equals("1")) {
			new AcceptFriendAsyncTask().execute(request.getId());
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

	public class AcceptFriendAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		ProgressBar progressAcceptFriend;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressAcceptFriend = (ProgressBar) getActivity().findViewById(
					R.id.progress_request);
			progressAcceptFriend.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";
			String from_id = params[0];

			// date

			try {
				sendString = "http://afuriqa.com/loco/acceptFriend.php?from_id="
						+ URLEncoder.encode(from_id, "UTF-8")
						+ "&to_id="
						+ URLEncoder.encode(
								prefs.getString(Globals.userId, null), "UTF-8");
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
			progressAcceptFriend.setVisibility(View.INVISIBLE);

			try {

				ResultParser resultParser = new ResultParser();
				Result result = resultParser.getParsedResults(jsonString);

				if (result.getError().contains("sucess")) {

					Toast toast = Toast.makeText(getActivity(),
							result.getMessage(), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

					// remove that item from the list
					new RequestListAsyncTask().execute();

				} else {

					Toast toast = Toast.makeText(getActivity(),
							result.getMessage(), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

				}

			} catch (Exception e) {

			}

		}
	}

}
