package com.yvelabs.timerecording;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.yvelabs.timerecording.ConfigEventFragment.DeleteListener;
import com.yvelabs.timerecording.utils.LogUtils;
import com.yvelabs.timerecording.utils.TypefaceUtils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ConfigActivity extends FragmentActivity {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	
	private ConfigCategoryFragment categoryFragment;
	private ConfigEventFragment eventFragment;
	
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("POSICTION", Integer.valueOf(position));
			
			if (position == 0) {
				eventFragment = ConfigEventFragment.newInstance(map);
				return eventFragment;
			} else if (position == 1) {
				categoryFragment = ConfigCategoryFragment.newInstance(map);
				return categoryFragment;
			} 
			
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.configure_the_events);
			case 1:
				return getString(R.string.configure_the_categories);
			}
			return null;
		}
		
	}
	
	public void refreshCategoryList () {
		categoryFragment.refreshList();
	}
	
	public void refreshEventList () {
		eventFragment.refreshEventList();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;// 获取点击了第几行

		if (item.getItemId() == 1) {
			//event edit
			eventFragment.editEdit(selectedPosition);
		} else if (item.getItemId() == 2) {
			//event delete
			eventFragment.deleteEvent(selectedPosition);
		} else if (item.getItemId() == 3) {
			// category edit
			categoryFragment.editCategory(selectedPosition);
		} else if (item.getItemId() == 4) {
			//category delete
			categoryFragment.deleteCategory(selectedPosition);
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
		

}
