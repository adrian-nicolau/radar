package ro.pub.cs.radar.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ro.pub.cs.radar.Constants;

/***
 * Container class for all information fetched about a certain point.
 * 
 * @author Adrian Nicolau
 * 
 */
public class PointData {

	private int x;
	private int y;
	private ArrayList<HashMap<String, Integer>> samples;
	private HashMap<String, Integer> sum = new HashMap<String, Integer>();
	private HashMap<String, Integer> occurrences = new HashMap<String, Integer>();

	public PointData(int x, int y, ArrayList<HashMap<String, Integer>> samples) {
		this.x = x;
		this.y = y;
		this.samples = samples;
	}

	private void getSumWithOccurrences() {
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

	public HashMap<String, Double> getAverage() {
		HashMap<String, Double> average = new HashMap<String, Double>();
		this.getSumWithOccurrences();

		for (Map.Entry<String, Integer> e : sum.entrySet()) {
			String bssid = e.getKey();
			if (bssid.startsWith(Constants.FSL)) {
				average.put(bssid, ((double) e.getValue()) / occurrences.get(bssid));
			}
		}

		return average;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x: " + x + "\n");
		sb.append("y: " + y + "\n");
		for (int i = 0; i < samples.size(); i++) {
			sb.append("sample" + i + "\n");
			for (Map.Entry<String, Integer> e : samples.get(i).entrySet()) {
				sb.append("\t" + e.getKey() + " -> " + e.getValue() + "\n");
			}
		}
		return sb.toString();
	}
}
