package net.kuwalab.android.icareader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kazzz.felica.FeliCaTag;
import net.kazzz.felica.command.ReadResponse;
import net.kazzz.felica.lib.FeliCaLib.ServiceCode;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView messageText = (TextView) findViewById(R.id.messageText);
		messageText.setText("ICaを端末にかざしてください");
		messageText.setTextColor(Color.rgb(0xff, 0x80, 0x40));
		messageText.setBackgroundColor(Color.rgb(0xff, 0xd0, 0xd0));

		onNewIntent(getIntent());
	}

	private void viewList(String nowRestMoney, List<Map<String, String>> list) {
		TextView messageText = (TextView) findViewById(R.id.messageText);
		messageText.setText("残高");
		messageText.setTextColor(Color.rgb(0xff, 0x80, 0x40));
		messageText.setBackgroundColor(Color.rgb(0xff, 0xd0, 0xd0));

		TextView nowRestMoneyText = (TextView) findViewById(R.id.nowRestMoneyText);
		nowRestMoneyText.setText(nowRestMoney);
		nowRestMoneyText.setTextColor(Color.rgb(0xff, 0x80, 0x40));
		nowRestMoneyText.setBackgroundColor(Color.rgb(0xff, 0xd0, 0xd0));

		TextView historyText = (TextView) findViewById(R.id.historyText);
		historyText.setText("使用履歴（上から新しい順）");
		historyText.setBackgroundColor(Color.rgb(0x80, 0x80, 0xff));

		ListView historyListView = (ListView) findViewById(R.id.listView);
		SimpleAdapter adapter = new ICaAdapter(this, list, R.layout.list,
				new String[] { "no", "reason", "restMoney", "useMoney", "date",
						"beginTime", "endTime" }, new int[] { R.id.no,
						R.id.reason, R.id.restMoney, R.id.useMoney, R.id.date,
						R.id.beginTime, R.id.endTime });
		historyListView.setAdapter(adapter);
	}

	@Override
	public void onNewIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

			Parcelable nfcTag = intent
					.getParcelableExtra("android.nfc.extra.TAG");
			// FeliCaLib.IDm idm = new FeliCaLib.IDm(
			// intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
			read(nfcTag);
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
				Toast.makeText(getBaseContext(), "ICaでないか、カードが読み込めません。",
						Toast.LENGTH_LONG).show();
				return;
			}

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String nowRestMoney = "0";

			while (result != null && result.getStatusFlag1() == 0) {
				IcaHistory icaHistory = new IcaHistory(result.getBlockData());

				Map<String, String> map = new HashMap<String, String>();
				map.put("no", String.valueOf(addr + 1));
				map.put("reason", icaHistory.getReason());
				map.put("restMoney", icaHistory.getDispRestMoney());
				map.put("useMoney",
						String.valueOf(icaHistory.getDispUseMoney()));
				map.put("date", icaHistory.date);
				map.put("beginTime", icaHistory.beginTime);
				map.put("endTime", icaHistory.endTime);
				list.add(map);

				if (addr == 0) {
					nowRestMoney = icaHistory.getDispRestMoney();
				}
				addr++;
				result = f.readWithoutEncryption(sc, addr);
			}

			viewList(nowRestMoney, list);

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "読み込めませんでした。再度試してください。",
					Toast.LENGTH_LONG).show();
		}
	}
}
