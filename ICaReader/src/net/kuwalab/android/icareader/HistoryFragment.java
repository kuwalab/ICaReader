package net.kuwalab.android.icareader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kazzz.felica.FeliCaTag;
import net.kazzz.felica.command.ReadResponse;
import net.kazzz.felica.lib.FeliCaLib.ServiceCode;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * カード情報を表示するFragment
 * 
 * @author kuwalab
 * 
 */
public class HistoryFragment extends Fragment {
	private ArrayList<ICaHistory> icaHistoryList;
	protected static final String PREFERENCES_NAME = "ICA_DATA";
	protected static final String PREFERENCES_CONF_DATE = "conf_date";
	protected static final String PREFERENCES_REST_MONEY = "rest_money";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.history_fragment, container, false);
	}

	private void viewList(ArrayList<ICaHistory> icaHistoryList) {
		List<Map<String, String>> list = icaHistoryListToMap(icaHistoryList);
		String nowRestMoney = icaHistoryList.get(0).getDispRestMoney();

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
				PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString(
				PREFERENCES_CONF_DATE,
				DateFormat.format(getString(R.string.ica_widget_date),
						Calendar.getInstance()).toString());
		edit.putString(PREFERENCES_REST_MONEY, getString(R.string.yen)
				+ nowRestMoney);
		edit.commit();

		// ウィジェット
		Resources res = getActivity().getResources();

		AppWidgetManager awm = AppWidgetManager.getInstance(getActivity());
		RemoteViews remoteViews = new RemoteViews(getActivity()
				.getPackageName(), R.layout.widget_main);
		remoteViews.setTextViewText(
				R.id.confDate,
				pref.getString(PREFERENCES_CONF_DATE,
						res.getString(R.string.ica_widget_unconfirmed)));
		remoteViews.setTextViewText(
				R.id.widetRest,
				pref.getString(PREFERENCES_REST_MONEY,
						res.getString(R.string.ica_widget_default_rest_money)));
		awm.updateAppWidget(new ComponentName(getActivity(), ICaWidget.class),
				remoteViews);

	}

	private List<Map<String, String>> icaHistoryListToMap(
			List<ICaHistory> icaHistoryList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < icaHistoryList.size(); i++) {
			ICaHistory icaHistory = icaHistoryList.get(i);
			list.add(icaHistoryToMap(icaHistory));
		}

		return list;
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

	/**
	 * 描画時にデータがあれば、そのデータを表示
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (icaHistoryList != null && icaHistoryList.size() > 0) {
			viewList(icaHistoryList);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("test", icaHistoryList);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			icaHistoryList = savedInstanceState.getParcelableArrayList("test");
		}
	}

	public void read(Parcelable nfcTag) {
		try {
			FeliCaTag f = new FeliCaTag(nfcTag);

			// polling は IDm、PMmを取得するのに必要
			f.polling(0x80EF);

			// サービスコード読み取り
			ServiceCode sc = new ServiceCode(0x898F);
			byte addr = 0;
			ReadResponse result = f.readWithoutEncryption(sc, addr);
			if (result == null) {
				Toast.makeText(getActivity(), R.string.ica_not_ica,
						Toast.LENGTH_LONG).show();
				return;
			}

			icaHistoryList = new ArrayList<ICaHistory>();
			while (result != null && result.getStatusFlag1() == 0) {
				icaHistoryList.add(new ICaHistory(result.getBlockData()));

				addr++;
				result = f.readWithoutEncryption(sc, addr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), R.string.ica_read_error,
					Toast.LENGTH_LONG).show();
		}
	}
}
