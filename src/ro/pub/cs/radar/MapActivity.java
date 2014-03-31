package ro.pub.cs.radar;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.app.Activity;

public class MapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		FrameLayout fl = (FrameLayout) findViewById(R.id.mapLayout);
		View map = new MapView(this, getApplicationContext());
		fl.addView(map);
	}

}
