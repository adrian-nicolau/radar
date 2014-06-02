package ro.pub.cs.radar.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Environment;
import android.util.JsonReader;

public class Parser {

	private String fileName;
	private JsonReader reader;
	public static ArrayList<PointData> points;

	public Parser(String fileName) {
		this.fileName = fileName;
		points = new ArrayList<PointData>();
		this.setupIO();
	}

	public void setupIO() {
		File logFile = new File(Environment.getExternalStorageDirectory()
				.toString(), fileName);
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(logFile));
			this.reader = new JsonReader(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void execute() throws IOException {
		reader.beginObject();

		while (reader.hasNext()) {
			reader.nextName(); // skip "pointx"
			reader.beginObject();

			reader.nextName(); // skip "x"
			int x = reader.nextInt();
			reader.nextName(); // skip "y"
			int y = reader.nextInt();

			ArrayList<HashMap<String, Integer>> samples = new ArrayList<HashMap<String, Integer>>();

			while (reader.hasNext()) {
				reader.nextName(); // skip "samplex"

				HashMap<String, Integer> sample = new HashMap<String, Integer>();

				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();

					reader.nextName();
					String bssid = reader.nextString();
					reader.nextName();
					Integer rssi = reader.nextInt();

					sample.put(bssid, rssi);
					reader.endObject();
				}
				reader.endArray();
				samples.add(sample);
			}
			points.add(new PointData(x, y, samples));
			reader.endObject();
		}
		reader.endObject();
	}

}
