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
import utils.ConnectUtils;
import utils.Globals;
import utils.MyApplication;
import utils.ResultParser;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class WineFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	ImageView imgSmallWine, imgLargeWine;

	// ImageView imgWineLeftBox, imgWineCenterBox,
	// imgWineRightBox;
	Button btnAddWine;
	SeekBar seekBar;

	TextView percentageWine;
	int progress;
	String bottleSize = "20";
	String percent = "12.0";
	ProgressBar progressAddWine;

	SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.wine_fragment, container,
				false);

		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initialize();

	}

	private void initialize() {
		// TODO Auto-generated method stub
		seekBar = (SeekBar) getActivity().findViewById(R.id.seek_bar_wine);
		imgSmallWine = (ImageView) getActivity().findViewById(
				R.id.img_small_wine);
		imgLargeWine = (ImageView) getActivity().findViewById(
				R.id.img_large_wine);

		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// imgWineLeftBox = (ImageView) getActivity().findViewById(
		// R.id.img_wine_left_box);
		//
		// imgWineCenterBox = (ImageView) getActivity().findViewById(
		// R.id.img_wine_center_box);
		//
		// imgWineRightBox = (ImageView) getActivity().findViewById(
		// R.id.img_wine_right_box);

		percentageWine = (TextView) getActivity().findViewById(
				R.id.txt_percent_wine);
		progressAddWine = (ProgressBar) getActivity().findViewById(
				R.id.progress_add_wine);

		btnAddWine = (Button) getActivity().findViewById(R.id.btn_add_wine);
		seekBar.setOnSeekBarChangeListener(this);
		imgSmallWine.setOnClickListener(this);
		imgLargeWine.setOnClickListener(this);
		// imgWineLeftBox.setOnClickListener(this);
		// imgWineCenterBox.setOnClickListener(this);
		// imgWineRightBox.setOnClickListener(this);
		btnAddWine.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.img_small_wine:
			// Toast.makeText(getActivity().getApplicationContext(), "small",
			// Toast.LENGTH_SHORT).show();

			imgSmallWine.setBackgroundResource(R.drawable.small_wine_clicked);
			imgLargeWine.setBackgroundResource(R.drawable.large_wine);

			bottleSize = "20";
			break;

		case R.id.img_large_wine:

			// Toast.makeText(getActivity().getApplicationContext(), "large",
			// Toast.LENGTH_SHORT).show();

			imgLargeWine.setBackgroundResource(R.drawable.large_wine_clicked);
			imgSmallWine.setBackgroundResource(R.drawable.small_wine);
			bottleSize = "30";
			break;

		case R.id.btn_add_wine:

			// Toast.makeText(getActivity().getApplicationContext(), "large",
			// Toast.LENGTH_SHORT).show();

			new AddWineAsyncTask().execute(bottleSize, percent);

			break;

		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

		float decimalProgress = (float) (progress * 0.1);
		percent = Float.toString(decimalProgress);

		percentageWine.setText(decimalProgress + "%");

		// if (decimalProgress < 12.0) {
		//
		// imgWineLeftBox.setBackgroundResource(R.drawable.box_clicked);
		// imgWineCenterBox.setBackgroundResource(R.drawable.box);
		// imgWineRightBox.setBackgroundResource(R.drawable.box);
		//
		// } else if (decimalProgress == 12.0) {
		//
		// imgWineLeftBox.setBackgroundResource(R.drawable.box);
		// imgWineCenterBox.setBackgroundResource(R.drawable.box_clicked);
		// imgWineRightBox.setBackgroundResource(R.drawable.box);
		//
		// } else {
		//
		// imgWineLeftBox.setBackgroundResource(R.drawable.box);
		// imgWineCenterBox.setBackgroundResource(R.drawable.box);
		// imgWineRightBox.setBackgroundResource(R.drawable.box_clicked);
		// }

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public class AddWineAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;

		MyApplication myApplication = (MyApplication) getActivity()
				.getApplication();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressAddWine.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();
			String bottlesize = params[0];
			String alcoolPercentage = params[1];
			String boozeType = "2"; // for wine
			// String date = params[2];

			StringBuilder builder = new StringBuilder();
			ConnectUtils connector = new ConnectUtils();
			HttpClient client = connector.getNewHttpClient();

			String result = "FAILED";

			// date

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(Globals.dateTimeFormat);
			String date = sdf.format(c.getTime());

			// LOAD SERVER PREF

			try {
				sendString = "http://afuriqa.com/loco/booze.php?id="
						+ URLEncoder.encode(
								prefs.getString(Globals.userId, null), "UTF-8")
						+ "&bottle_size="
						+ URLEncoder.encode(bottlesize, "UTF-8")
						+ "&alcool_percentage="
						+ URLEncoder.encode(alcoolPercentage, "UTF-8")
						+ "&date=" + URLEncoder.encode(date, "UTF-8")
						+ "&booze_type="
						+ URLEncoder.encode(boozeType, "UTF-8");
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
			progressAddWine.setVisibility(View.INVISIBLE);

			ResultParser resultParser = new ResultParser();
			Result result = resultParser.getParsedResults(jsonString);

			if (result.getError().contains("sucess")) {

				Toast toast = Toast.makeText(getActivity(), "Wine added",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else if (result.getError().contains("failed")) {

				Toast toast = Toast.makeText(getActivity(), "Error retry !!",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else {

			}

		}
	}

}
