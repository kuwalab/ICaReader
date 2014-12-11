package net.kuwalab.android.icareader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * カードを読み取るメインクラス
 *
 * @author kuwalab
 */
public class MainActivity extends ActionBarActivity {
    private HistoryFragment historyFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

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