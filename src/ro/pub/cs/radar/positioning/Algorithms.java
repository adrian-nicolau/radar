package ro.pub.cs.radar.positioning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import ro.pub.cs.radar.Constants;
import ro.pub.cs.radar.data.AveragePointData;
import android.graphics.PointF;

/***
 * Class that exposes the positioning algorithms.
 * 
 * @author Adrian Nicolau
 * 
 */
public class Algorithms {

	private HashMap<String, Integer> onlineData;
	private ArrayList<AveragePointData> offlineData;
	private TreeMap<Double, PointF> sortedPointsByDistance;

	public Algorithms(HashMap<String, Integer> onlineData, ArrayList<AveragePointData> offlineData) {
		this.onlineData = onlineData;
		this.offlineData = offlineData;
		this.sortedPointsByDistance = new TreeMap<Double, PointF>();
		this.sortPointsByDistance();
	}

	/***
	 * Sort the offline data with regard to the measured RSSI.
	 */
	private void sortPointsByDistance() {
		double distance;
		for (AveragePointData apd : offlineData) {
			distance = Common.euclideanDistance(apd.getData(), onlineData);
			if (distance != 0) {
				sortedPointsByDistance.put(distance, new PointF(apd.getX(), apd.getY()));
			}
		}
	}

	/***
	 * Nearest Neighbor algorithm. Return the point that minimizes the distance.
	 */
	public PointF NN() {
		try {
			return sortedPointsByDistance.get(sortedPointsByDistance.firstKey());
		} catch (NoSuchElementException nsee) {
			return null;
		}
	}

	/***
	 * k-Nearest Neighbors algorithm. Return the averaged coordinates from the
	 * closest k points.
	 */
	public PointF KNN() {
		int k = Constants.k;
		float sumX = 0, sumY = 0;

		for (int i = 0; i < k; i++) {
			Map.Entry<Double, PointF> e = sortedPointsByDistance.pollFirstEntry();
			if (e == null) {
				return null;
			}
			sumX += e.getValue().x;
			sumY += e.getValue().y;
		}

		return new PointF(sumX / k, sumY / k);
	}

	/***
	 * Weighted k-Nearest Neighbors algorithm. Return the averaged coordinates
	 * from the closest k points, but weigh them first according to the sorting.
	 */
	public PointF WKNN() {
		int k = Constants.k;
		float sumX = 0, sumY = 0;
		float denominator = 0;

		for (int i = 0; i < k; i++) {
			Map.Entry<Double, PointF> e = sortedPointsByDistance.pollFirstEntry();
			if (e == null) {
				return null;
			}
			sumX += e.getValue().x / e.getKey();
			sumY += e.getValue().y / e.getKey();
			denominator += 1.0 / e.getKey();
		}

		return new PointF(sumX / denominator, sumY / denominator);
	}

}
