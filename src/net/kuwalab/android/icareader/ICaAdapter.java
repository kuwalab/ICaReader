package net.kuwalab.android.icareader;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ICaAdapter extends SimpleAdapter {
	private LayoutInflater layoutInflater;

	public ICaAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
			String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		layoutInflater = LayoutInflater.from(parent.getContext());

		convertView = layoutInflater.inflate(R.layout.list, parent, false);
		ListView listView = (ListView) parent;

		Map<String, Object> data = (Map<String, Object>) listView.getItemAtPosition(position);

		TextView noTextView = (TextView) convertView.findViewById(R.id.no);
		noTextView.setText((String) data.get("no"));

		TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
		dateTextView.setText((String) data.get("date"));

		TextView reasonTextView = (TextView) convertView.findViewById(R.id.reason);
		reasonTextView.setText((String) data.get("reason"));

		TextView useMoneyTextView = (TextView) convertView.findViewById(R.id.useMoney);
		useMoneyTextView.setText((String) data.get("useMoney"));
		if ("使用".equals(data.get("reason"))) {
			reasonTextView.setTextColor(Color.rgb(0xff, 0x80, 0x80));
			useMoneyTextView.setTextColor(Color.rgb(0xff, 0x80, 0x80));
		} else {
			reasonTextView.setTextColor(Color.rgb(0x80, 0x80, 0xff));
			useMoneyTextView.setTextColor(Color.rgb(0x80, 0x80, 0xff));
		}

		TextView restMoneyTextView = (TextView) convertView.findViewById(R.id.restMoney);
		restMoneyTextView.setText((String) data.get("restMoney"));

		TextView beginTimeTextView = (TextView) convertView.findViewById(R.id.beginTime);
		beginTimeTextView.setText((String) data.get("beginTime"));

		TextView endTimeTextView = (TextView) convertView.findViewById(R.id.endTime);
		endTimeTextView.setText((String) data.get("endTime"));

		return convertView;
	}
}
