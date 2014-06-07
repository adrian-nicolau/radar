package ro.pub.cs.radar.data;

import java.util.HashMap;

/***
 * Container for the average data computed for a sampled point.
 * 
 * @author Adrian Nicolau
 *
 */
public class AveragePointData {

	private int x;
	private int y;
	private HashMap<String, Double> data;

	public AveragePointData(int x, int y, HashMap<String, Double> averageData) {
		this.x = x;
		this.y = y;
		this.data = averageData;
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

	public HashMap<String, Double> getData() {
		return data;
	}

	public void setData(HashMap<String, Double> data) {
		this.data = data;
	}
}
