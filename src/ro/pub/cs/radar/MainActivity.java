package ro.pub.cs.radar;

import java.util.ArrayList;
import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView lv;
	private ArrayAdapter<String> adapter;

	private WifiManager mainWifi;
	private WifiReceiver receiverWifi;
	private List<ScanResult> wifiList;
	private List<String> SSIDs = new ArrayList<String>();
	private List<String> attributes = new ArrayList<String>();

	private Handler mHandler;
	private int mInterval = 5000; // 5 seconds

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lv = (ListView) findViewById(R.id.listView);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, SSIDs);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				AlertDialog.Builder adb = new AlertDialog.Builder(
						MainActivity.this);
				adb.setTitle("Network Info");
				adb.setMessage(attributes.get(position));
				adb.setPositiveButton("Ok", null);
				adb.show();

			}

		});

		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		receiverWifi = new WifiReceiver();
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mainWifi.startScan();

		// refresh information
		mHandler = new Handler();
		startRepeatingTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Refresh");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		mainWifi.startScan();
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(receiverWifi);
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}

	Runnable mStatusChecker = new Runnable() {
		@Override
		public void run() {
			mainWifi.startScan();
			mHandler.postDelayed(mStatusChecker, mInterval);
		}
	};

	void startRepeatingTask() {
		mStatusChecker.run();
	}

	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			// Dismiss all previous data
			SSIDs.clear();
			attributes.clear();

			wifiList = mainWifi.getScanResults();
			for (int i = 0; i < wifiList.size(); i++) {
				SSIDs.add(wifiList.get(i).SSID);
				attributes.add(wifiList.get(i).toString().replace(", ", "\n"));
			}
			adapter.notifyDataSetChanged();
		}
	}

}
