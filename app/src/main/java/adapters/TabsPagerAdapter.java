package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import fragments.BoozeFragment;
import fragments.BuddysFragment;
import fragments.StartFragment;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new StartFragment();
		case 1:
			// Games fragment activity
			return new BoozeFragment();
		case 2:
			// Movies fragment activity
			return new BuddysFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
