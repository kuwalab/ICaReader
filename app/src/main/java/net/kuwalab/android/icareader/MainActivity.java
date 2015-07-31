package net.kuwalab.android.icareader;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_lisence) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.webview_lisence);
            dialog.setTitle("オープンソースライセンス");
            dialog.setCancelable(true);
            WebView webView = (WebView)dialog.findViewById(R.id.webView);

            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    dialog.show();
                }
            });
            webView.loadUrl("file:///android_asset/lisence.html");
        }

        return super.onOptionsItemSelected(item);
    }
}
