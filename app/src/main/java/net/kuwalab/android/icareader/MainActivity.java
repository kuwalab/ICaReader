package net.kuwalab.android.icareader;

import android.app.Dialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * カードを読み取るメインクラス
 *
 * @author kuwalab
 */
public class MainActivity extends AppCompatActivity {
    private HistoryFragment historyFragment;
    private static final int LISENCE_DIALOG_ID = 0;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_lisence) {
            FragmentManager manager = getSupportFragmentManager();
            LisenceDialogFragment d = new LisenceDialogFragment();
            d.show(manager, "dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    public static class LisenceDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.webview_lisence);
            dialog.setTitle("オープンソースライセンス");
            dialog.setCancelable(true);
            WebView webView = (WebView) dialog.findViewById(R.id.webViewLisence);

            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.loadUrl("file:///android_asset/lisence.html");
            return dialog;
        }
    }
}
