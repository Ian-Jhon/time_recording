package com.yvelabs.timerecording;

import java.lang.reflect.Field;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConfigActivity extends FragmentActivity {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.config_activity);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.config_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mPagerTitleStrip = (PagerTitleStrip) this.findViewById(R.id.config_pager_title_strip);
		
		try {
			Field mPrevText = mPagerTitleStrip.getClass().getDeclaredField("mPrevText");
			Field mCurrText = mPagerTitleStrip.getClass().getDeclaredField("mCurrText");
			Field mNextText = mPagerTitleStrip.getClass().getDeclaredField("mNextText");
			
			 mCurrText.setAccessible(true); 
			 TextView mCurrTextView = (TextView) mCurrText.get(mPagerTitleStrip);
			 TypefaceUtils.setTypeface(mCurrTextView, TypefaceUtils.MOBY_MONOSPACE);
			 
			 mPrevText.setAccessible(true); 
			 TextView mPrevTextView = (TextView) mPrevText.get(mPagerTitleStrip);
			 mPrevTextView.setTextSize(12);
			 
			 mNextText.setAccessible(true); 
			 TextView mNextTextView = (TextView) mNextText.get(mPagerTitleStrip);
			 mNextTextView.setTextSize(12);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			
			/*if (position == 1) {
				return 
			} else {*/
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.configure_the_events);
			case 1:
				return getString(R.string.configure_the_categories);
			case 2:
				return getString(R.string.settings);
			}
			return null;
		}
		
	}
	
	public static class DummySectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}
		

}
