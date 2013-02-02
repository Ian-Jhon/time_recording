package com.yvelabs.timerecording;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.NotificationUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

public class RecordActivity extends FragmentActivity {
	
	public static final int NOTIFICATION_ID_RECORDER_MYRECORDER = 111;

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	
	private Recorder recorderFragment;
	private RecordHistoryFragment recordHistoryFragment;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new NotificationUtils().removeNotification(this, NOTIFICATION_ID_RECORDER_MYRECORDER);
		setContentView(R.layout.activity_main);

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
			 mPrevTextView.setTextSize(12);
			 
			 mNextText.setAccessible(true); 
			 TextView mNextTextView = (TextView) mNextText.get(mPagerTitleStrip);
			 mNextTextView.setTextSize(12);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("POSICTION", Integer.valueOf(position));
			
			if (position == 0) {
				recorderFragment = Recorder.newInstance(map);
				return recorderFragment;
			} else if (position == 2) {
				recordHistoryFragment = RecordHistoryFragment.newInstance(map);
				return recordHistoryFragment;
			}else {
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
	
	public void refreshHistoryList (EventRecordModel parameter) {
		recordHistoryFragment.refreshHistoryList(parameter);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行
		
		if (item.getItemId() == 301) {
			recordHistoryFragment.deleteSelectedRecord(selectedPosition);
		}
		return super.onContextItemSelected(item);
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
	
	@Override
	protected void onDestroy() {
		new NotificationUtils().notifyNotification(this, this.getClass(), null,
				R.drawable.ic_launcher,
				getResources().getString(R.string.app_name),
				recorderFragment.getEventsStatusMsg(),
				NOTIFICATION_ID_RECORDER_MYRECORDER);
		super.onDestroy();
	}

}
