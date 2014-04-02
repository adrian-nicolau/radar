package ro.pub.cs.radar;

import java.io.IOException;
import java.util.List;

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

public class Collector extends Thread {

	private Activity parent;
	private WifiManager manager;
	private BroadcastReceiver receiver;
	private List<ScanResult> results;
	private Point point;

	private int noSamples = 5;
	private int currentSample = 1;

	private static final JsonWriter writer = MapActivity.writer;
	private static int instance = 0;

	public Collector(Activity parent, Point point) {
		Collector.instance++;

		this.parent = parent;
		this.point = point;

		this.manager = (WifiManager) parent
				.getSystemService(Context.WIFI_SERVICE);
		this.receiver = new ScanResultsReceiver();
		parent.registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	@Override
	public void run() {
		// Start scan for available APs
		try {
			this.setupIO();
		} catch (IOException e) {
			e.printStackTrace();
		}
		manager.startScan();
	}

	public void setupIO() throws IOException {
		writer.name("point" + Collector.instance);
		writer.beginObject();
		writer.name("x").value(point.x);
		writer.name("y").value(point.y);
	}

	class ScanResultsReceiver extends BroadcastReceiver {

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
								"Done " + currentSample + "/" + noSamples,
								Toast.LENGTH_SHORT).show();
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
}
