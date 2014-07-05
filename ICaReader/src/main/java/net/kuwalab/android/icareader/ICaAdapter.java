package net.kuwalab.android.icareader;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ICaAdapter extends SimpleAdapter {
    public ICaAdapter(Context context, List<? extends Map<String, ?>> data,
                      int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        convertView = layoutInflater.inflate(R.layout.list, parent, false);
        ListView listView = (ListView) parent;

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) listView
                .getItemAtPosition(position);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
        dateTextView.setText((String) data.get("date"));

        TextView addMoneyTextView = (TextView) convertView
                .findViewById(R.id.addMoney);
        addMoneyTextView.setText((String) data.get("addMoney"));
        TextView useMoneyTextView = (TextView) convertView
                .findViewById(R.id.useMoney);
        useMoneyTextView.setText((String) data.get("useMoney"));

        TextView restMoneyTextView = (TextView) convertView
                .findViewById(R.id.restMoney);
        restMoneyTextView.setText((String) data.get("restMoney"));

        TextView beginTimeTextView = (TextView) convertView
                .findViewById(R.id.rideTime);
        beginTimeTextView.setText((String) data.get("rideTime"));

        TextView endTimeTextView = (TextView) convertView
                .findViewById(R.id.dropTime);
        endTimeTextView.setText((String) data.get("dropTime"));

        return convertView;
    }
}
