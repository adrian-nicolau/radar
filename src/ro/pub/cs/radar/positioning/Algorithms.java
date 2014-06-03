package ro.pub.cs.radar.positioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ro.pub.cs.radar.Constants;
import ro.pub.cs.radar.data.AveragePointData;
import android.graphics.PointF;

public class Algorithms {

	private HashMap<String, Integer> onlineData;
	private ArrayList<AveragePointData> offlineData;

	public Algorithms(HashMap<String, Integer> onlineData, ArrayList<AveragePointData> offlineData) {
		this.onlineData = onlineData;
		this.offlineData = offlineData;
	}

	public PointF NN() {
		PointF result = new PointF();
		double best = Integer.MAX_VALUE;
		double curr;

		for (AveragePointData apd : offlineData) {
			curr = Common.euclideanDistance(apd.getData(), onlineData);
			if (curr < best) {
				best = curr;
				result.set(apd.getX(), apd.getY());
			}
		}

		return result;
	}

	public PointF KNN() {
		int k = Constants.k;
		HashMap<Double, PointF> pointsWithDistance = new HashMap<Double, PointF>();
		double distance;
		float sumX = 0, sumY = 0;

		for (AveragePointData apd : offlineData) {
			distance = Common.euclideanDistance(apd.getData(), onlineData);
			pointsWithDistance.put(distance, new PointF(apd.getX(), apd.getY()));
		}

		List<Double> sortedKeys = new ArrayList<Double>(pointsWithDistance.keySet());
		Collections.sort(sortedKeys);

		for (int i = 0; i < k; i++) {
			sumX += pointsWithDistance.get(sortedKeys.get(i)).x;
			sumY += pointsWithDistance.get(sortedKeys.get(i)).y;
		}

		return new PointF(sumX / k, sumY / k);
	}

}
