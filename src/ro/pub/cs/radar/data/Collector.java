package ro.pub.cs.radar.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import ro.pub.cs.radar.gui.MapActivity;
import ro.pub.cs.radar.gui.MapView;
import ro.pub.cs.radar.gui.WhereAmIActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.JsonWriter;
import android.widget.Toast;

/***
 * Thread class which collects Wi-Fi information from the current location.
 * 
 * @author Adrian Nicolau
 * 
 */
public class Collector extends Thread {

	private Activity parent;
	private static WifiManager manager;
	private BroadcastReceiver receiver;
	private List<ScanResult> results;
	private Point point;

	private int noSamples = 5;
	private int currentSample = 1;

	private static final JsonWriter writer = MapActivity.writer;
	private static int instance = 0;

	private boolean offline;

	/***
	 * Constructor for the online phase.
	 * 
	 * @param parent
	 *            the Activity object from which this is called
	 */
	public Collector(Activity parent) {
		this.offline = false;
		this.parent = parent;

		manager = (WifiManager) parent.getSystemService(Context.WIFI_SERVICE);
		this.receiver = new OnlineReceiver();
		parent.registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	/***
	 * Constructor for the offline phase.
	 * 
	 * @param parent
	 *            the Activity object from which this is called
	 * @param point
	 *            coordinates of the current location
	 */
	public Collector(Activity parent, Point point) {
		Collector.instance++;
		this.offline = true;
		this.parent = parent;
		this.point = point;

		manager = (WifiManager) parent.getSystemService(Context.WIFI_SERVICE);
		this.receiver = new OfflineReceiver();
		parent.registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	@Override
	public void run() {
		if (offline) {
			try {
				this.setupIO();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Start scan for available APs
		manager.startScan();
	}

	private void setupIO() throws IOException {
		writer.name("point" + Collector.instance);
		writer.beginObject();
		writer.name("x").value(point.x);
		writer.name("y").value(point.y);
	}

	class OfflineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			results = manager.getScanResults();

			try {

				writer.name("sample" + currentSample);

				writer.beginArray();
				for (ScanResult result : results) {
					writer.beginObject();
					writer.name("bssid").value(result.BSSID);
					writer.name("rssi").value(result.level);
					writer.endObject();
				}
				writer.endArray();

				parent.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(parent.getApplicationContext(),
								"Done " + currentSample + "/" + noSamples, Toast.LENGTH_SHORT)
								.show();
					}
				});

				currentSample++;
				if (currentSample == noSamples + 1) {
					writer.endObject();
					parent.unregisterReceiver(this);
					MapView.setBusy(false);
				} else {
					manager.startScan();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class OnlineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			results = manager.getScanResults();
			HashMap<String, Integer> onlineData = new HashMap<String, Integer>();
			for (int i = 0; i < results.size(); i++) {
				onlineData.put(results.get(i).BSSID, results.get(i).level);
			}
			((WhereAmIActivity) parent).setOnlineData(onlineData);
			parent.unregisterReceiver(this);
		}

	}
}
