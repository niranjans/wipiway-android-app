package com.wipiway.wipiway_app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuListFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MenuAdapter adapter = new MenuAdapter(getActivity());
		
		adapter.add(new SampleItem("Home", android.R.drawable.ic_menu_search));
		adapter.add(new SampleItem("Help", android.R.drawable.ic_menu_search));
		adapter.add(new SampleItem("Settings", android.R.drawable.ic_menu_search));

		adapter.add(new SampleItem("Feedback", android.R.drawable.ic_menu_search));
		adapter.add(new SampleItem("Rate this App", android.R.drawable.ic_menu_search));


		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Activity newContent = null;
		
		Intent i;
		switch (position) {
		case 0:
			i = new Intent(v.getContext(), PasscodeActivity.class);
        	i.putExtra(WipiwayUtils.INTENT_EXTRA_KEY_PASSCODE_FLOW, WipiwayUtils.INTENT_EXTRA_VALUE_NEW_PASSCODE_FLOW);
        	startActivity(i);
			break;
		case 1:
			break;
		case 2:
			i = new Intent(v.getContext(), SettingsActivity.class);
        	startActivity(i);
			break;
		case 3:
			break;
		case 4:
			break;
		}

	}
	

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class MenuAdapter extends ArrayAdapter<SampleItem> {

		public MenuAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
	
}
