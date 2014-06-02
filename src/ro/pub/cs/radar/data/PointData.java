package ro.pub.cs.radar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ro.pub.cs.radar.Constants;

public class PointData {

	public int x;
	public int y;
	public ArrayList<HashMap<String, Integer>> samples;
	public HashMap<String, Integer> sum = new HashMap<String, Integer>();
	public HashMap<String, Integer> occurrences = new HashMap<String, Integer>();

	public PointData(int x, int y, ArrayList<HashMap<String, Integer>> samples) {
		this.x = x;
		this.y = y;
		this.samples = samples;
	}

	private void computeSum() {
		for (int i = 0; i < samples.size(); i++) {
			for (Map.Entry<String, Integer> e : samples.get(i).entrySet()) {
				String bssid = e.getKey();
				// add signal strengths
				Integer previousValue = sum.get(bssid);
				if (previousValue == null) {
					previousValue = 0;
				}
				sum.put(bssid, previousValue + e.getValue());

				// count occurrences
				previousValue = occurrences.get(bssid);
				if (previousValue == null) {
					previousValue = 0;
				}
				occurrences.put(bssid, previousValue + 1);
			}
		}
	}

	public HashMap<String, Float> getAverage() {
		HashMap<String, Float> average = new HashMap<String, Float>();

		computeSum();
		for (Map.Entry<String, Integer> e : sum.entrySet()) {
			String bssid = e.getKey();
			if (bssid.startsWith(Constants.FSL)) {
				average.put(bssid,
						((float) e.getValue()) / occurrences.get(bssid));
			}
		}

		return average;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x: " + this.x + "\n");
		sb.append("y: " + this.y + "\n");
		for (int i = 0; i < samples.size(); i++) {
			sb.append("sample" + i + "\n");
			for (Map.Entry<String, Integer> e : samples.get(i).entrySet()) {
				sb.append("\t" + e.getKey() + " -> " + e.getValue() + "\n");
			}
		}
		return sb.toString();
	}
}
