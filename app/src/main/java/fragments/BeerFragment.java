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

public class BeerFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	ImageView imgSmallBeer, imgLargBeer;
	// ImageView imgBeerLeftBox, imgBeerCenterBox, imgBeerRightBox;
	SeekBar seekBar;
	TextView percentageBeer;
	int progress;

	String bottleSize = "33";
	String percent = "5.0";
	Button btnAddBeer;
	SharedPreferences prefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.beer_fragment, container,
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
		imgSmallBeer = (ImageView) getActivity().findViewById(
				R.id.img_small_beer);
		imgLargBeer = (ImageView) getActivity().findViewById(
				R.id.img_large_beer);
		seekBar = (SeekBar) getActivity().findViewById(R.id.seek_bar_beer);
		percentageBeer = (TextView) getActivity().findViewById(
				R.id.txt_percent_beer);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		//
		// imgBeerLeftBox = (ImageView) getActivity().findViewById(
		// R.id.img_beer_left_box);
		//
		// imgBeerCenterBox = (ImageView) getActivity().findViewById(
		// R.id.img_beer_center_box);
		//
		// imgBeerRightBox = (ImageView) getActivity().findViewById(
		// R.id.img_beer_right_box);

		btnAddBeer = (Button) getActivity().findViewById(R.id.btn_add_beer);

		seekBar.setOnSeekBarChangeListener(this);
		imgSmallBeer.setOnClickListener(this);
		imgLargBeer.setOnClickListener(this);
		btnAddBeer.setOnClickListener(this);
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

		case R.id.img_small_beer:
			// Toast.makeText(getActivity().getApplicationContext(), "small",
			// Toast.LENGTH_SHORT).show();

			imgSmallBeer.setBackgroundResource(R.drawable.small_beer_clicked);
			imgLargBeer.setBackgroundResource(R.drawable.large_beer);

			bottleSize = "33";
			break;

		case R.id.img_large_beer:

			// Toast.makeText(getActivity().getApplicationContext(), "large",
			// Toast.LENGTH_SHORT).show();

			imgLargBeer.setBackgroundResource(R.drawable.large_beer_clicked);
			imgSmallBeer.setBackgroundResource(R.drawable.small_beer);
			bottleSize = "50";
			break;

		case R.id.btn_add_beer:

			// Toast.makeText(getActivity().getApplicationContext(), "large",
			// Toast.LENGTH_SHORT).show();

			// if (bottleSize != null && !bottleSize.isEmpty()) {
			new AddBeerAsyncTask().execute(bottleSize, percent);
			// } else {
			//
			// Toast toast = Toast.makeText(getActivity(),
			// "please choose bootle size", Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.CENTER, 0, 0);
			// toast.show();
			// }
			break;

		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

		float decimalProgress = (float) progress / 10;
		percent = Float.toString(decimalProgress);

		percentageBeer.setText(decimalProgress + "%");

		// if (decimalProgress < 5.0) {
		//
		// imgBeerLeftBox.setBackgroundResource(R.drawable.box_clicked);
		// imgBeerCenterBox.setBackgroundResource(R.drawable.box);
		// imgBeerRightBox.setBackgroundResource(R.drawable.box);
		//
		// } else if (decimalProgress == 5.0) {
		//
		// imgBeerLeftBox.setBackgroundResource(R.drawable.box);
		// imgBeerCenterBox.setBackgroundResource(R.drawable.box_clicked);
		// imgBeerRightBox.setBackgroundResource(R.drawable.box);
		//
		// } else {
		//
		// imgBeerLeftBox.setBackgroundResource(R.drawable.box);
		// imgBeerCenterBox.setBackgroundResource(R.drawable.box);
		// imgBeerRightBox.setBackgroundResource(R.drawable.box_clicked);
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

	public class AddBeerAsyncTask extends AsyncTask<String, Void, String> {

		String sendString;
		ProgressBar progressAddBeer;

		MyApplication myApplication = (MyApplication) getActivity()
				.getApplication();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressAddBeer = (ProgressBar) getActivity().findViewById(
					R.id.progress_add_beer);
			progressAddBeer.setVisibility(View.VISIBLE);
		}

		@Override
		// protected String doInBackground(Void... arg0) {
		protected String doInBackground(String... params) {

			// String id = myApplication.getId();
			String bottlesize = params[0];
			String alcoolPercentage = params[1];
			String boozeType = "1"; // for beer
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
			progressAddBeer.setVisibility(View.INVISIBLE);

			ResultParser resultParser = new ResultParser();
			Result result = resultParser.getParsedResults(jsonString);

			if (result.getError().contains("sucess")) {

				Toast toast = Toast.makeText(getActivity(), "Beer added",
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
