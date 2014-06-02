package ro.pub.cs.radar.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ro.pub.cs.radar.R;
import ro.pub.cs.radar.data.AveragePointData;
import ro.pub.cs.radar.data.Collector;
import ro.pub.cs.radar.data.Parser;
import ro.pub.cs.radar.data.PointData;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class WhereAmIActivity extends Activity {

	private HashMap<String, Integer> onlineData;
	private ArrayList<AveragePointData> offlineData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Get data from current location
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

	public void setOnlineData(HashMap<String, Integer> onlineData) {
		this.onlineData = onlineData;
		Log.v("POS", this.onlineData.toString());

		// TODO estimate position
		setOfflineData();
		Log.v("EUCLID", onlineData.toString());
		for (AveragePointData apd : offlineData) {
			Log.v("EUCLID", apd.getData().toString());
			Log.v("EUCLID", String.valueOf(euclideanDistance(apd.getData())));
		}
		// TODO draw point on map
	}

	private void setOfflineData() {
		offlineData = new ArrayList<AveragePointData>();
		for (int i = 0; i < Parser.points.size(); i++) {
			PointData pd = Parser.points.get(i);
			offlineData.add(new AveragePointData(pd.getX(), pd.getY(), pd.getAverage()));
		}
	}

	private double euclideanDistance(HashMap<String, Double> offlineData) {
		double distance = 0;
		for (Map.Entry<String, Double> off : offlineData.entrySet()) {
			for (Map.Entry<String, Integer> on : onlineData.entrySet()) {
				if (off.getKey().equals(on.getKey())) {
					distance += Math.pow(on.getValue() - off.getValue(), 2);
				}
			}
		}
		return Math.sqrt(distance);
	}

}
