package com.yvelabs.timerecording;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yvelabs.satellitemenu.DefaultAnimation2;
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
			// left satellite menu
			leftSatelliteMenu = (SatelliteMenu) v.findViewById(R.id.left_satellite);
			List<SatelliteItemModel> leftRatllites = new ArrayList<SatelliteItemModel>();
	        leftRatllites.add(new SatelliteItemModel(1, R.drawable.satellite_shutdown));
	        leftRatllites.add(new SatelliteItemModel(2, R.drawable.satellite_setting));
	        leftRatllites.add(new SatelliteItemModel(3, R.drawable.satellite_diary));
//	        leftRatllites.add(new SatelliteItemModel(4, R.drawable.satellite_report));
	        leftRatllites.add(new SatelliteItemModel(5, R.drawable.satellite_recorder));
			
			SettingPara settingPara = new SettingPara(0, 90, 200, R.drawable.satellite_planet_menu, leftRatllites);
			//设置动画
			DefaultAnimation2 anim = new DefaultAnimation2();
			anim.setSatelliteStartOffsetMin(50);
			anim.setSatelliteStartOffsetMax(150);
			settingPara.setMenuAnimation(anim);
			//增大宽高
			settingPara.setCustomerRadiusAdjust(50);
			leftSatelliteMenu.setting(settingPara);
			
			//satellite listener
			leftSatelliteMenu.setOnSatelliteClickedListener(new SatelliteMenu.OnSatelliteClickedListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(v.getContext(), "left satelite id:" + ((SatelliteItemModel)v.getTag()).getId(), Toast.LENGTH_SHORT ).show();
				}
			});
			
			// right satellite menu
			rightSatelliteMenu = (SatelliteMenu) v.findViewById(R.id.right_satellite);
			
			// satellite
			List<SatelliteItemModel> rightRatllites = new ArrayList<SatelliteItemModel>();
			rightRatllites.add(new SatelliteItemModel(1, R.drawable.satellite_recorder));
			rightRatllites.add(new SatelliteItemModel(2, R.drawable.satellite_diary));
			rightRatllites.add(new SatelliteItemModel(3, R.drawable.satellite_setting));
			rightRatllites.add(new SatelliteItemModel(4, R.drawable.satellite_shutdown));
			
			//parameter
			SettingPara rightSettingPara = new SettingPara(90, 180, 200, R.drawable.satellite_planet_menu, rightRatllites);
			rightSettingPara.setCustomerRadiusAdjust(50);
			
			//设置动画
			DefaultAnimation2 rightAnimation = new DefaultAnimation2();
			rightAnimation.setSatelliteStartOffsetMin(50);
			rightAnimation.setSatelliteStartOffsetMax(150);
			rightSettingPara.setMenuAnimation(rightAnimation);
			
			rightSatelliteMenu.setting(rightSettingPara);
			
			//satellite listener
			rightSatelliteMenu.setOnSatelliteClickedListener(new SatelliteMenu.OnSatelliteClickedListener() {
				@Override
				public void onClick(View v) {
					int satelliteId = ((SatelliteItemModel)v.getTag()).getId();
					if (satelliteId == 1) {
						Intent intent = new Intent(getActivity(), RecordActivity.class);
						getActivity().startActivity(intent);
						getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
					} else if (satelliteId == 3) {
						Intent intent = new Intent(getActivity(), ConfigActivity.class);
						getActivity().startActivity(intent);
						getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
					}
					
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
}
