package ro.pub.cs.radar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.os.Environment;
import android.util.JsonWriter;
import android.view.View;
import android.widget.FrameLayout;
import android.app.Activity;

public class MapActivity extends Activity {

	protected static JsonWriter writer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		try {
			setupIO();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FrameLayout fl = (FrameLayout) findViewById(R.id.mapLayout);
		View map = new MapView(this, this);
		fl.addView(map);
	}

	public void setupIO() throws IOException {

		String file = "test";
		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
		String timestamp = s.format(new Date());
		String fileName = file + timestamp + ".json";

		File logFile = new File(Environment.getExternalStorageDirectory()
				.toString(), fileName);

		if (!logFile.exists()) {
			logFile.createNewFile();
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
		writer = new JsonWriter(out);
		writer.setIndent(" ");
		writer.beginObject();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			writer.endObject();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
