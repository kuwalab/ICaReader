package net.kuwalab.android.icareader;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * サポートライブラリーにPreferenceFragmentがないためActivityで実装
 */
public class ICaPreferenceActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
    }
}
