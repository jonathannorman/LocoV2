package fragments;

import se.loco.app.R;
import utils.Globals;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class BoozeFragment extends Fragment implements OnClickListener {

	Button btnBeer, btnDrink, btnWine;
	String clicked;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.booze_fragment, container,
				false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// beer button

		btnBeer = (Button) getActivity().findViewById(R.id.btn_beer);
		btnDrink = (Button) getActivity().findViewById(R.id.btn_drink);
		btnWine = (Button) getActivity().findViewById(R.id.btn_wine);
		btnBeer.setOnClickListener(this);
		btnDrink.setOnClickListener(this);
		btnWine.setOnClickListener(this);

		if (savedInstanceState == null) {
			clicked = "beer";
			btnBeer.setBackgroundResource(R.drawable.beer_shape_clicked);

			Fragment bearFragment = new BeerFragment();
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			transaction.add(R.id.frag_holder, bearFragment).commit();

		} else {

			if (savedInstanceState.getString(Globals.boozeClicked) == "beer") {

				clicked = "beer";
				btnBeer.setBackgroundResource(R.drawable.beer_shape_clicked);

				Fragment bearFragment = new BeerFragment();
				FragmentTransaction transaction = getChildFragmentManager()
						.beginTransaction();
				transaction.add(R.id.frag_holder, bearFragment).commit();

			} else if (savedInstanceState.getString(Globals.boozeClicked) == "wine") {

				clicked = "wine";
				btnBeer.setBackgroundResource(R.drawable.beer_shape);
				btnWine.setBackgroundResource(R.drawable.wine_background_clicked);
				btnDrink.setBackgroundResource(R.drawable.drink_shape);

				FragmentTransaction transactionWine = getChildFragmentManager()
						.beginTransaction();
				Fragment wineFragment = new WineFragment();
				transactionWine.replace(R.id.frag_holder, wineFragment);
				transactionWine.commit();

			} else {

				clicked = "drink";
				btnBeer.setBackgroundResource(R.drawable.beer_shape);
				btnWine.setBackgroundResource(R.drawable.wine_background);
				btnDrink.setBackgroundResource(R.drawable.drink_shape_clicked);

				FragmentTransaction transactionDrink = getChildFragmentManager()
						.beginTransaction();
				Fragment drinkFragment = new DrinkFragment();
				transactionDrink.replace(R.id.frag_holder, drinkFragment);
				transactionDrink.commit();

			}

		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_beer:
			// updateDetail();
			// change button background Resources
			clicked = "beer";
			btnBeer.setBackgroundResource(R.drawable.beer_shape_clicked);
			btnWine.setBackgroundResource(R.drawable.wine_background);
			btnDrink.setBackgroundResource(R.drawable.drink_shape);

			FragmentTransaction transactionBeer = getChildFragmentManager()
					.beginTransaction();
			Fragment bearFragment = new BeerFragment();
			transactionBeer.replace(R.id.frag_holder, bearFragment);
			transactionBeer.commit();
			break;

		case R.id.btn_wine:
			clicked = "wine";
			btnBeer.setBackgroundResource(R.drawable.beer_shape);
			btnWine.setBackgroundResource(R.drawable.wine_background_clicked);
			btnDrink.setBackgroundResource(R.drawable.drink_shape);

			FragmentTransaction transactionWine = getChildFragmentManager()
					.beginTransaction();
			Fragment wineFragment = new WineFragment();
			transactionWine.replace(R.id.frag_holder, wineFragment);
			transactionWine.commit();
			break;

		case R.id.btn_drink:
			clicked = "drink";
			btnBeer.setBackgroundResource(R.drawable.beer_shape);
			btnWine.setBackgroundResource(R.drawable.wine_background);
			btnDrink.setBackgroundResource(R.drawable.drink_shape_clicked);

			FragmentTransaction transactionDrink = getChildFragmentManager()
					.beginTransaction();
			Fragment drinkFragment = new DrinkFragment();
			transactionDrink.replace(R.id.frag_holder, drinkFragment);
			transactionDrink.commit();
			break;

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
		outState.putString(Globals.boozeClicked, clicked);

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

}
