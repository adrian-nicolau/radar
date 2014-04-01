package ro.pub.cs.radar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.JsonWriter;
import android.widget.Toast;

public class Collector extends Thread {

	private Activity parent;
	private WifiManager manager;
	private BroadcastReceiver receiver;
	private List<ScanResult> results;
	private Point point;

	private static final String file = "test";
	private static final SimpleDateFormat s = new SimpleDateFormat(
			"ddMMyyyyhhmmss", Locale.US);
	private static final String timestamp = s.format(new Date());
	private static final String fileName = file + timestamp + ".json";

	private int noSamples = 5;
	private int currentSample = 1;

	private JsonWriter writer;

	public Collector() {
	}

	public Collector(Activity parent, Point point) {
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

		File logFile = new File(Environment.getExternalStorageDirectory()
				.toString(), fileName);
		BufferedWriter out = null;
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		out = new BufferedWriter(new FileWriter(logFile, true));
		writer = new JsonWriter(out);
		writer.setIndent("  ");
		writer.beginObject();
	}

	class ScanResultsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			results = manager.getScanResults();

			try {

				writer.name("x").value(point.x);
				writer.name("y").value(point.y);
				writer.name("APs");

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
					writer.close();
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
