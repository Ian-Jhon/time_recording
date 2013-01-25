package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yvelabs.satellitemenu.SatelliteItemModel;
import com.yvelabs.satellitemenu.SatelliteMenu;
import com.yvelabs.satellitemenu.SettingPara;

public class BottomFragment extends Fragment {
	
	private SatelliteMenu leftSatelliteMenu;
	private SatelliteMenu rightSatelliteMenu;

	public static BottomFragment newInstance(int num) {
		BottomFragment f = new BottomFragment();

		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int mNum = getArguments() != null ? getArguments().getInt("num") : 0;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.button_fragment, container,
				false);
		
		try {
			leftSatelliteMenu = (SatelliteMenu) v.findViewById(R.id.left_satellite);
			List<SatelliteItemModel> leftRatllites = new ArrayList<SatelliteItemModel>();
	        leftRatllites.add(new SatelliteItemModel(1, R.drawable.satellite_shutdown));
	        leftRatllites.add(new SatelliteItemModel(2, R.drawable.satellite_setting));
	        leftRatllites.add(new SatelliteItemModel(3, R.drawable.satellite_diary));
	        leftRatllites.add(new SatelliteItemModel(4, R.drawable.satellite_report));
	        leftRatllites.add(new SatelliteItemModel(5, R.drawable.satellite_recorder));
			
			SettingPara settingPara = new SettingPara(0, 90, 200, R.drawable.satellite_planet_menu, leftRatllites);
			leftSatelliteMenu.setting(settingPara);
			leftSatelliteMenu.setOnSatelliteClickedListener(new SatelliteMenu.OnSatelliteClickedListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(v.getContext(), "left satelite id:" + ((SatelliteItemModel)v.getTag()).getId(), Toast.LENGTH_SHORT ).show();
				}
			});
			
			rightSatelliteMenu = (SatelliteMenu) v.findViewById(R.id.right_satellite);
			List<SatelliteItemModel> rightRatllites = new ArrayList<SatelliteItemModel>();
			rightRatllites.add(new SatelliteItemModel(1, R.drawable.satellite_shutdown));
			rightRatllites.add(new SatelliteItemModel(2, R.drawable.satellite_setting));
			rightRatllites.add(new SatelliteItemModel(4, R.drawable.satellite_diary));
			rightRatllites.add(new SatelliteItemModel(6, R.drawable.satellite_recorder));
//			rightRatllites.add(new SatelliteItemModel(7, R.drawable.satellite_3));
			SettingPara rightSettingPara = new SettingPara(90, 180, 200, R.drawable.satellite_planet_menu, rightRatllites);
			rightSatelliteMenu.setting(rightSettingPara);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
}
