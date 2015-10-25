package fragments;

import se.loco.app.R;
import utils.Globals;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BuddysFragment extends Fragment implements OnClickListener {

	String click;

	Button btnOut, btnRequest, btnAll;
	SharedPreferences prefs;
	Editor edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.buddys_fragment, container,
				false);

		return rootView;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// beer button

		btnOut = (Button) getActivity().findViewById(R.id.btn_out);
		btnRequest = (Button) getActivity().findViewById(R.id.btn_request);
		btnAll = (Button) getActivity().findViewById(R.id.btn_all);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		edit = prefs.edit();
		edit.putString(Globals.onStart, Globals.onStartNo);
		edit.commit();

		btnOut.setOnClickListener(this);
		btnRequest.setOnClickListener(this);
		btnAll.setOnClickListener(this);

		// TODO Auto-generated method stub

		if (savedInstanceState == null) {
			click = "out";
			btnOut.setBackgroundResource(R.drawable.beer_shape_clicked);
			btnRequest.setBackgroundResource(R.drawable.wine_background);
			btnAll.setBackgroundResource(R.drawable.drink_shape);

			FragmentTransaction transactionOut = getChildFragmentManager()
					.beginTransaction();

			Fragment outFragment = new OutFragment();

			transactionOut.replace(R.id.frag_holder_buddys, outFragment);

			transactionOut.commit();

		} else {

			if (savedInstanceState.getString(Globals.buddysClicked) == "out") {

				click = "out";

				btnOut.setBackgroundResource(R.drawable.beer_shape_clicked);
				btnRequest.setBackgroundResource(R.drawable.wine_background);
				btnAll.setBackgroundResource(R.drawable.drink_shape);

				FragmentTransaction transactionOut = getChildFragmentManager()
						.beginTransaction();

				Fragment outFragment = new OutFragment();

				transactionOut.replace(R.id.frag_holder_buddys, outFragment);

				transactionOut.commit();

			} else if (savedInstanceState.getString(Globals.buddysClicked) == "request") {

				click = "request";
				btnOut.setBackgroundResource(R.drawable.beer_shape);
				btnRequest
						.setBackgroundResource(R.drawable.wine_background_clicked);
				btnAll.setBackgroundResource(R.drawable.drink_shape);

				FragmentTransaction transactionRequest = getChildFragmentManager()
						.beginTransaction();

				Fragment requestFragment = new RequestFragment();

				transactionRequest.replace(R.id.frag_holder_buddys,
						requestFragment);

				transactionRequest.commit();

			} else {
				click = "all";
				btnOut.setBackgroundResource(R.drawable.beer_shape);
				btnRequest.setBackgroundResource(R.drawable.wine_background);
				btnAll.setBackgroundResource(R.drawable.drink_shape_clicked);

				FragmentTransaction transactionAll = getChildFragmentManager()
						.beginTransaction();

				Fragment allFragment = new AllFragment();

				transactionAll.replace(R.id.frag_holder_buddys, allFragment);

				transactionAll.commit();

			}

		}

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
		outState.putString(Globals.buddysClicked, click);

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

		switch (v.getId()) {
		case R.id.btn_out:

			// Toast.makeText(getActivity().getApplicationContext(),
			// "Hello",
			// Toast.LENGTH_LONG).show();

			click = "out";
			btnOut.setBackgroundResource(R.drawable.beer_shape_clicked);
			btnRequest.setBackgroundResource(R.drawable.wine_background);
			btnAll.setBackgroundResource(R.drawable.drink_shape);

			FragmentTransaction transactionOut = getChildFragmentManager()
					.beginTransaction();
			Fragment outFragment = new OutFragment();
			transactionOut.replace(R.id.frag_holder_buddys, outFragment);
			transactionOut.commit();

			break;

		case R.id.btn_request:

			click = "request";
			btnOut.setBackgroundResource(R.drawable.beer_shape);
			btnRequest
					.setBackgroundResource(R.drawable.wine_background_clicked);
			btnAll.setBackgroundResource(R.drawable.drink_shape);

			FragmentTransaction transactionRequest = getChildFragmentManager()
					.beginTransaction();
			Fragment requestFragment = new RequestFragment();
			transactionRequest
					.replace(R.id.frag_holder_buddys, requestFragment);
			transactionRequest.commit();

			break;

		case R.id.btn_all:

			click = "all";
			btnOut.setBackgroundResource(R.drawable.beer_shape);
			btnRequest.setBackgroundResource(R.drawable.wine_background);
			btnAll.setBackgroundResource(R.drawable.drink_shape_clicked);

			FragmentTransaction transactionAll = getChildFragmentManager()
					.beginTransaction();
			Fragment allFragment = new AllFragment();
			transactionAll.replace(R.id.frag_holder_buddys, allFragment);
			transactionAll.commit();

			break;
		}

	}

}
