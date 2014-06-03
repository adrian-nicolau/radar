package ro.pub.cs.radar.positioning;

import java.util.HashMap;
import java.util.Map;

public class Common {

	public static double euclideanDistance(HashMap<String, Double> a, HashMap<String, Integer> b) {
		double distance = 0;
		for (Map.Entry<String, Double> e1 : a.entrySet()) {
			for (Map.Entry<String, Integer> e2 : b.entrySet()) {
				if (e1.getKey().equals(e2.getKey())) {
					distance += Math.pow(e1.getValue() - e2.getValue(), 2);
				}
			}
		}
		return Math.sqrt(distance);
	}

}
