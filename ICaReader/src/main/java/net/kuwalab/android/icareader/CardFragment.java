package net.kuwalab.android.icareader;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import net.kazzz.felica.FeliCaTag;
import net.kazzz.felica.command.ReadResponse;
import net.kazzz.felica.lib.FeliCaLib;
import net.kazzz.felica.lib.FeliCaLib.ServiceCode;
import net.kuwalab.android.util.HexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * カード情報を表示するFragment
 *
 * @author kuwalab
 */
public class CardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> a = new HashMap<String, String>();
        a.put("cardDate", "2010/10/01");
        a.put("cardRestMoney", "1000");
        list.add(a);

        ListView cardListView = (ListView) getActivity().findViewById(
                R.id.cardListView);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
                R.layout.card_list, new String[]{"cardDate", "cardRestMoney"},
                new int[]{R.id.cardDate, R.id.cardRestMoney}
        );
        cardListView.setAdapter(adapter);

    }
}
