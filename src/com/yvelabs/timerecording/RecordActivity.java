package com.yvelabs.timerecording;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.yvelabs.timerecording.utils.TypefaceUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecordActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	private PagerTitleStrip mPagerTitleStrip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mPagerTitleStrip = (PagerTitleStrip) this
				.findViewById(R.id.pager_title_strip);

		try {
			Field mPrevText = mPagerTitleStrip.getClass().getDeclaredField("mPrevText");
			Field mCurrText = mPagerTitleStrip.getClass().getDeclaredField("mCurrText");
			Field mNextText = mPagerTitleStrip.getClass().getDeclaredField("mNextText");
			
			 mCurrText.setAccessible(true); 
			 TextView mCurrTextView = (TextView) mCurrText.get(mPagerTitleStrip);
			 TypefaceUtils.setTypeface(mCurrTextView, TypefaceUtils.MOBY_MONOSPACE);
			 
			 mPrevText.setAccessible(true); 
			 TextView mPrevTextView = (TextView) mPrevText.get(mPagerTitleStrip);
//			 TypefaceUtils.setTypeface(mPrevTextView, TypefaceUtils.RBNO2_LIGHT_A);
			 mPrevTextView.setTextSize(12);
			 
			 mNextText.setAccessible(true); 
			 TextView mNextTextView = (TextView) mNextText.get(mPagerTitleStrip);
//			 TypefaceUtils.setTypeface(mNextTextView, TypefaceUtils.RBNO2_LIGHT_A);
			 mNextTextView.setTextSize(12);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			if (position == 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("POSICTION", Integer.valueOf(position));
				return Recorder.newInstance(map);
			} else {
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.my_recorder);
			case 1:
				return getString(R.string.add_records);
			case 2:
				return getString(R.string.history);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}

}
