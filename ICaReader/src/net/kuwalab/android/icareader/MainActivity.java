package net.kuwalab.android.icareader;

import java.util.ArrayList;

import net.kazzz.felica.FeliCaTag;
import net.kazzz.felica.command.ReadResponse;
import net.kazzz.felica.lib.FeliCaLib.ServiceCode;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * カードを読み取るメインクラス
 * 
 * @author kuwalab
 * 
 */
public class MainActivity extends FragmentActivity {
	private ArrayList<ICaHistory> icaHistoryList;
	private HistoryFragment historyFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView messageText = (TextView) findViewById(R.id.messageText);
		messageText.setText(R.string.ica_first_step);
		FragmentManager manager = getSupportFragmentManager();
		historyFragment = (HistoryFragment) manager
				.findFragmentById(R.id.historyFragment);

		Intent intent = getIntent();
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			onNewIntent(intent);
		}
	}

	/**
	 * 描画時にデータがあれば、そのデータを表示
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (icaHistoryList != null && icaHistoryList.size() > 0) {
			historyFragment.viewList(icaHistoryList);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("test", icaHistoryList);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		icaHistoryList = savedInstanceState.getParcelableArrayList("test");
	}

	@Override
	public void onNewIntent(Intent intent) {
		Parcelable nfcTag = intent.getParcelableExtra("android.nfc.extra.TAG");
		read(nfcTag);

		// 呼び出しIntentを取り消す
		setIntent(new Intent());
	}

	private void read(Parcelable nfcTag) {
		try {
			FeliCaTag f = new FeliCaTag(nfcTag);

			// polling は IDm、PMmを取得するのに必要
			f.polling(0x80EF);

			// サービスコード読み取り
			ServiceCode sc = new ServiceCode(0x898F);
			byte addr = 0;
			ReadResponse result = f.readWithoutEncryption(sc, addr);
			if (result == null) {
				Toast.makeText(getBaseContext(), R.string.ica_not_ica,
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
			Toast.makeText(getBaseContext(), R.string.ica_read_error,
					Toast.LENGTH_LONG).show();
		}
	}

}