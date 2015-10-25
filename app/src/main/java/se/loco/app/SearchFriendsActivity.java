package se.loco.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import model.Result;
import model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.ConnectUtils;
import utils.Globals;
import utils.ResultParser;
import utils.UserListParser;
import adapters.UserListAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class SearchFriendsActivity extends Activity implements
		OnItemClickListener {

	final Session session = Session.getActiveSession();
	List<User> userList = new LinkedList<User>();
	List<User> listToShow = new LinkedList<User>();
	String showN;
	Editor edit;

	SharedPreferences prefs;
	ProgressBar progressSearchFriends;
	TextView txtNoFriend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_friends_activity);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		edit = prefs.edit();

		progressSearchFriends = (ProgressBar) findViewById(R.id.progress_search_friends);
		txtNoFriend = (TextView) findViewById(R.id.txt_no_friend);

		// get user id

		// end get user id

		if (isLoggedIn()) {
			loadFriends();
		} else {
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
		}

	}

	@SuppressWarnings("deprecation")
	private void loadFriendsSecondTime() {
		// TODO Auto-generated method stub

		new CheckUnfriendsAsyncTask().execute(toJSon(userList));

	}

	@SuppressWarnings("deprecation")
	private void loadFriends() {
		// TODO Auto-generated method stub

		progressSearchFriends.setVisibility(View.VISIBLE);

		Request.executeMyFriendsRequestAsync(session,
				new Request.GraphUserListCallback() {

					@Override
					public void onCompleted(List<GraphUser> friends,
							Response response) {
						// TODO Auto-generated method stub

						for (GraphUser friend : friends) {
							User user = new User(friend.getId(), friend
									.getName(), prefs.getString(Globals.userId,
									null));
							userList.add(user);

						}

						new CheckUnfriendsAsyncTask().execute(toJSon(userList));

					}
				});

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

	public static String toJSon(List<User> users) {
		try {
			// Here we convert Java Object to JSON
			JSONObject jsonObj = new JSONObject();
			JSONArray jsonArr = new JSONArray();

			for (User user : users) {
				jsonObj = new JSONObject();

				jsonObj.put("name", user.getName());
				jsonObj.put("id", user.getId());
				jsonObj.put("user_id", user.getUserId());

				jsonArr.put(jsonObj);

			}

			return jsonArr.toString();

		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		return null;

	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();

		return result;

	}

	public String POST(String jsonString) {

		// 1. create HttpClient
		String a = null;

		String path = "";

		path = "http://afuriqa.com/loco/unFriendList.php";

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																				// Limit
		HttpResponse response;
		JSONObject json = new JSONObject();
		try {
			HttpPost post = new HttpPost(path);
			json.put("service", "GOOGLE");
			Log.i("jason Object", jsonString);
			post.setHeader("json", jsonString);
			StringEntity se = new StringEntity(jsonString);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);
			/* Checking response */
			if (response != null) {
				InputStream in = response.getEntity().getContent(); // Get the
																	// data in
																	// the
																	// entity
				a = convertStreamToString(in);
				Log.i("Read from Server", a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public class CheckUnfriendsAsyncTask extends
			AsyncTask<String, Void, String> {

		String result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";

			String sendString = null;

			// date

			try {
				sendString = "http://afuriqa.com/loco/unFriendList.php?json_data="
						+ URLEncoder.encode(params[0], "UTF-8");
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

		}

		@Override
		protected void onPostExecute(String userList) {
			super.onPostExecute(userList);
			Log.e("*****", userList);
			try {
				UserListParser userListParser = new UserListParser();
				listToShow = userListParser.getUserList(userList);

				ListView listOfDepartment = (ListView) findViewById(android.R.id.list);

				if (listOfDepartment != null) {
					UserListAdapter adapter = new UserListAdapter(
							SearchFriendsActivity.this, R.layout.single_user,
							listToShow);

					if (adapter.getCount() != 0) {
						listOfDepartment.setAdapter(adapter);
						listOfDepartment
								.setOnItemClickListener(SearchFriendsActivity.this);
					} else {
						listOfDepartment.setVisibility(View.GONE);
						txtNoFriend.setVisibility(View.VISIBLE);
					}

					progressSearchFriends.setVisibility(View.INVISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("", e.toString());
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		TextView textViewItem = ((TextView) view
				.findViewById(R.id.txt_friend_name));
		String frienId = textViewItem.getTag().toString();
		String friendName = textViewItem.getText().toString();
		String name = listToShow.get(position).getName();

		// Toast toast = Toast.makeText(
		// getApplicationContext(),
		// "name:" + friendName + ", Id: " + frienId + ", userId"
		// + prefs.getString(Globals.userId, null),
		// Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();

		new AddFriendAsyncTask().execute(prefs.getString(Globals.userId, null),
				frienId, name);

	}

	public class AddFriendAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		String userId, friendId, friendName;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressSearchFriends.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();
			userId = params[0];
			friendId = params[1];
			friendName = params[2]; // for beer
			// String date = params[2];

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";

			// date

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/addFriend.php?user_id="
						+ URLEncoder.encode(userId, "UTF-8") + "&friend_id="
						+ URLEncoder.encode(friendId, "UTF-8") + "&friendName="
						+ URLEncoder.encode(friendName, "UTF-8")

				;
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
				// REMOVE DIALOG

				ResultParser resultParser = new ResultParser();
				Result result = resultParser.getParsedResults(jsonString);

				if (result.getError().contains("sucess")) {

					// Toast toast = Toast.makeText(getApplicationContext(),
					// friendName + " added", Toast.LENGTH_LONG);
					// toast.setGravity(Gravity.CENTER, 0, 0);
					// toast.show();

					loadFriendsSecondTime();

				} else if (result.getError().contains("failed")) {

					Toast toast = Toast.makeText(getApplicationContext(),
							"Error retry !!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();

				} else {

				}

			} catch (Exception e) {

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

	public boolean isLoggedIn() {
		Session session = Session.getActiveSession();
		return (session != null && session.isOpened());
	}

}