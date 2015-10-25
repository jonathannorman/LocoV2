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

public class DrinkFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	ImageView imgSmallDrink, imgLargeDrink;

	// ImageView imgDrinkLeftBox, imgDrinkCenterBox,
	// imgDrinkRightBox;

	Button btnAddDrink;
	SeekBar seekBar;

	TextView percentageDrink;
	int progress;
	String bottleSize = "4";
	String percent = "40.0";
	ProgressBar progressAddDrink;
	SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.drink_fragment, container,
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
		seekBar = (SeekBar) getActivity().findViewById(R.id.seek_bar_drink);
		imgSmallDrink = (ImageView) getActivity().findViewById(
				R.id.img_small_drink);
		imgLargeDrink = (ImageView) getActivity().findViewById(
				R.id.img_large_drink);
		progressAddDrink = (ProgressBar) getActivity().findViewById(
				R.id.progress_add_drink);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// imgDrinkLeftBox = (ImageView) getActivity().findViewById(
		// R.id.img_drink_left_box);
		//
		// imgDrinkCenterBox = (ImageView) getActivity().findViewById(
		// R.id.img_drink_center_box);
		//
		// imgDrinkRightBox = (ImageView) getActivity().findViewById(
		// R.id.img_drink_right_box);
		percentageDrink = (TextView) getActivity().findViewById(
				R.id.txt_percent_drink);

		btnAddDrink = (Button) getActivity().findViewById(R.id.btn_add_drink);
		seekBar.setOnSeekBarChangeListener(this);
		imgSmallDrink.setOnClickListener(this);
		imgLargeDrink.setOnClickListener(this);
		// imgDrinkLeftBox.setOnClickListener(this);
		// imgDrinkCenterBox.setOnClickListener(this);
		// imgDrinkRightBox.setOnClickListener(this);
		btnAddDrink.setOnClickListener(this);

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

		case R.id.img_small_drink:
			// Toast.makeText(getActivity().getApplicationContext(), "small",
			// Toast.LENGTH_SHORT).show();

			imgSmallDrink.setBackgroundResource(R.drawable.small_drink_clicked);
			imgLargeDrink.setBackgroundResource(R.drawable.large_drink);
			bottleSize = "4";
			break;

		case R.id.img_large_drink:

			// Toast.makeText(getActivity().getApplicationContext(), "large",
			// Toast.LENGTH_SHORT).show();

			imgLargeDrink.setBackgroundResource(R.drawable.large_drink_clicked);
			imgSmallDrink.setBackgroundResource(R.drawable.small_drink);
			bottleSize = "6";
			break;

		case R.id.btn_add_drink:

			// Toast.makeText(getActivity().getApplicationContext(), "large",
			// Toast.LENGTH_SHORT).show();
			new AddDrinkAsyncTask().execute(bottleSize, percent);

			break;

		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

		float decimalProgress = (float) (progress * 0.1);
		percent = Float.toString(decimalProgress);

		percentageDrink.setText(decimalProgress + "%");

		// if (decimalProgress < 40.0) {
		//
		// imgDrinkLeftBox.setBackgroundResource(R.drawable.box_clicked);
		// imgDrinkCenterBox.setBackgroundResource(R.drawable.box);
		// imgDrinkRightBox.setBackgroundResource(R.drawable.box);
		//
		// } else if (decimalProgress == 40.0) {
		//
		// imgDrinkLeftBox.setBackgroundResource(R.drawable.box);
		// imgDrinkCenterBox.setBackgroundResource(R.drawable.box_clicked);
		// imgDrinkRightBox.setBackgroundResource(R.drawable.box);
		//
		// } else {
		//
		// imgDrinkLeftBox.setBackgroundResource(R.drawable.box);
		// imgDrinkCenterBox.setBackgroundResource(R.drawable.box);
		// imgDrinkRightBox.setBackgroundResource(R.drawable.box_clicked);
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

	public class AddDrinkAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;

		MyApplication myApplication = (MyApplication) getActivity()
				.getApplication();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressAddDrink.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();
			String bottlesize = params[0];
			String alcoolPercentage = params[1];
			String boozeType = "3"; // for wine
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
			progressAddDrink.setVisibility(View.INVISIBLE);

			ResultParser resultParser = new ResultParser();
			Result result = resultParser.getParsedResults(jsonString);

			if (result.getError().contains("sucess")) {

				Toast toast = Toast.makeText(getActivity(), "Drink added",
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
