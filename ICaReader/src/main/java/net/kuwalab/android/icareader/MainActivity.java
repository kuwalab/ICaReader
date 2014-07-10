package net.kuwalab.android.icareader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * カードを読み取るメインクラス
 *
 * @author kuwalab
 */
public class MainActivity extends FragmentActivity {
    private HistoryFragment historyFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FragmentManager manager = getSupportFragmentManager();
        historyFragment = (HistoryFragment) manager
                .findFragmentById(R.id.historyFragment);

        Intent intent = getIntent();
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            onNewIntent(intent);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        Parcelable nfcTag = intent.getParcelableExtra("android.nfc.extra.TAG");
        historyFragment.read(nfcTag);

        // 呼び出しIntentを取り消す
        setIntent(new Intent());
    }

}