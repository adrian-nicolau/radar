package ro.pub.cs.radar.gui;

import java.util.ArrayList;
import java.util.HashMap;

import ro.pub.cs.radar.R;
import ro.pub.cs.radar.data.AveragePointData;
import ro.pub.cs.radar.data.Collector;
import ro.pub.cs.radar.data.Parser;
import ro.pub.cs.radar.data.PointData;
import ro.pub.cs.radar.positioning.Algorithms;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

public class WhereAmIActivity extends Activity {

	public static String algorithm = "Nearest Neighbor";

	private HashMap<String, Integer> onlineData;
	private ArrayList<AveragePointData> offlineData;
	private MapView mapView;

	private int mInterval = 5000; // 5 seconds by default, can be changed later
	private Handler mHandler;
	private float lastX, lastY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		FrameLayout fl = (FrameLayout) findViewById(R.id.mapLayout);
		this.mapView = new MapView(this, this);
		fl.addView(this.mapView);

		this.mHandler = new Handler();
		// this.startRepeatingTask();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopRepeatingTask();
	}

	// Get data from current location
	public void getCurrentData() {
		Collector collector = new Collector(this);
		collector.start();
		try {
			collector.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Runnable picasso = new Runnable() {
		@Override
		public void run() {
			getCurrentData();
			mHandler.postDelayed(picasso, mInterval);
		}
	};

	private void startRepeatingTask() {
		picasso.run();
	}

	private void stopRepeatingTask() {
		mHandler.removeCallbacks(picasso);
	}

	private void drawPosition() {
		Algorithms a = new Algorithms(onlineData, offlineData);
		PointF position = null;
		if (algorithm.startsWith("N")) {
			Log.v("METHOD", "NN");
			position = a.NN();
		} else if (algorithm.startsWith("k")) {
			Log.v("METHOD", "kNN");
			position = a.KNN();
		} else if (algorithm.startsWith("W")) {
			Log.v("METHOD", "WkNN");
			position = a.WKNN();
		} else if (algorithm.startsWith("E")) {
			Log.v("METHOD", "EWkNN");
			position = a.EWKNN();
		}

		if (position != null) {
			Canvas canvas = new Canvas(this.mapView.getBitmap());
			Paint paint = new Paint();
			paint.setColor(Color.BLUE);
			canvas.drawCircle(position.x, position.y, 13, paint);
			lastX = position.x;
			lastY = position.y;
			mapView.invalidate();
		} else {
			Toast.makeText(getApplicationContext(), "Not enough data..", Toast.LENGTH_SHORT).show();
		}
	}

	public void setOnlineData(HashMap<String, Integer> onlineData) {
		this.onlineData = onlineData;
		Log.v("POS", this.onlineData.toString());

		setOfflineData();
		drawPosition();
	}

	private void setOfflineData() {
		offlineData = new ArrayList<AveragePointData>();
		for (int i = 0; i < Parser.points.size(); i++) {
			PointData pd = Parser.points.get(i);
			offlineData.add(new AveragePointData(pd.getX(), pd.getY(), pd.getAverage()));
		}
	}

}
