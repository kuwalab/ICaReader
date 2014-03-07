package net.kuwalab.android.icareader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * カード情報を表示するFragment
 * 
 * @author kuwalab
 * 
 */
public class HistoryFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.history_fragment, container, false);
	}

	public void viewList(ArrayList<ICaHistory> icaHistoryList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String nowRestMoney = "0";

		for (int i = 0; i < icaHistoryList.size(); i++) {
			ICaHistory icaHistory = icaHistoryList.get(i);
			list.add(icaHistoryToMap(icaHistory));

			if (i == 0) {
				nowRestMoney = icaHistory.getDispRestMoney();
			}
		}

		TextView messageText = (TextView) getActivity().findViewById(
				R.id.messageText);
		messageText.setText(R.string.ica_rest);
		TextView nowRestMoneyText = (TextView) getActivity().findViewById(
				R.id.nowRestMoneyText);
		nowRestMoneyText.setText(nowRestMoney);

		LinearLayout firstStepLayout = (LinearLayout) getActivity()
				.findViewById(R.id.firstStepLayout);
		firstStepLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		messageText.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		ListView historyListView = (ListView) getActivity().findViewById(
				R.id.listView);
		historyListView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		SimpleAdapter adapter = new ICaAdapter(getActivity(), list,
				R.layout.list, new String[] { "restMoney", "addMoney",
						"useMoney", "date", "rideTime", "dropTime" },
				new int[] { R.id.restMoney, R.id.addMoney, R.id.useMoney,
						R.id.date, R.id.rideTime, R.id.dropTime });
		historyListView.setAdapter(adapter);

		SharedPreferences pref = getActivity().getSharedPreferences(
				ICaService.PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString(
				ICaService.PREFERENCES_CONF_DATE,
				DateFormat.format(getString(R.string.ica_widget_date),
						Calendar.getInstance()).toString());
		edit.putString(ICaService.PREFERENCES_REST_MONEY,
				getString(R.string.yen) + nowRestMoney);
		edit.commit();

		Intent intent = new Intent();
		intent.setAction(ICaService.ACTION);
		getActivity().sendBroadcast(intent);
	}

	private Map<String, String> icaHistoryToMap(ICaHistory icaHistory) {
		Map<String, String> map = new HashMap<String, String>();

		map.put("restMoney", icaHistory.getDispRestMoney());
		if (icaHistory.isUse()) {
			map.put("useMoney", String.valueOf(icaHistory.getDispUseMoney()));
			map.put("addMoney", "");
		} else {
			map.put("useMoney", "");
			map.put("addMoney", String.valueOf(icaHistory.getDispAddMoney()));
		}
		map.put("date", getViewDate(icaHistory.getDate()));
		map.put("rideTime", getViewTime(icaHistory.getRideTime()));
		map.put("dropTime", getViewTime(icaHistory.getDropTime()));

		return map;
	}

	private String getViewDate(int[] date) {
		if (date == null) {
			return getString(R.string.ica_invalid_date);
		}
		return getString(R.string.ica_date, date[0], date[1], date[2]);
	}

	private String getViewTime(int[] time) {
		if (time == null) {
			return getString(R.string.ica_invalid_time);
		}
		return getString(R.string.ica_time, time[0], time[1]);
	}
}
