package ro.pub.cs.radar.positioning;

import java.util.HashMap;
import java.util.Map;

public class Common {

	public static double euclideanDistance(HashMap<String, Double> off, HashMap<String, Integer> on) {
		double distance = 0;
		boolean found = false;
		for (Map.Entry<String, Integer> e1 : on.entrySet()) {
			for (Map.Entry<String, Double> e2 : off.entrySet()) {
				if (e1.getKey().equals(e2.getKey())) {
					distance += Math.pow(((double) (e1.getValue())) - e2.getValue(), 2);
					found = true;
				}
			}
			if (!found) {
				distance += 42; // the answer to everything
				found = false;
			}
		}
		return Math.sqrt(distance);
	}

}
