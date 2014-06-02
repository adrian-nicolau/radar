package ro.pub.cs.radar.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ro.pub.cs.radar.R;
import ro.pub.cs.radar.data.AveragePointData;
import ro.pub.cs.radar.data.Collector;
import ro.pub.cs.radar.data.Parser;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class WhereAmIActivity extends Activity {

	private HashMap<String, Integer> onlineData;
	private ArrayList<AveragePointData> offlineData;

	public HashMap<String, Integer> getCurrentData() {
		return onlineData;
	}

	public void setOnlineData(HashMap<String, Integer> onlineData) {
		this.onlineData = onlineData;
		Log.v("POS", this.onlineData.toString());

		// TODO estimate position
		setOfflineData();
		Log.v("EUCLID", onlineData.toString());
		for (AveragePointData apd : offlineData) {
			Log.v("EUCLID", apd.getData().toString());
			// Log.v("EUCLID",
			// String.valueOf(euclideanDistance(apd.getData())));
		}
		// TODO draw point on map
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Get current location data
		Collector collector = new Collector(this);
		collector.start();

		FrameLayout fl = (FrameLayout) findViewById(R.id.mapLayout);
		View map = new MapView(this, this);
		fl.addView(map);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// TODO
	}

	private void setOfflineData() {
		offlineData = new ArrayList<AveragePointData>();
		for (int i = 0; i < Parser.points.size(); i++) {
			offlineData.add(new AveragePointData(Parser.points.get(i).x,
					Parser.points.get(i).y, Parser.points.get(i).getAverage()));
		}
	}

	private double euclideanDistance(HashMap<String, Float> offlineData) {
		double distance = 0;
		for (Map.Entry<String, Float> off : offlineData.entrySet()) {
			for (Map.Entry<String, Integer> on : onlineData.entrySet()) {
				if (off.getKey().equals(on.getKey())) {
					distance += Math.sqrt(Math.pow(on.getValue(), 2)
							- Math.pow(off.getValue(), 2));
				}
			}
		}
		return distance;
	}

}
