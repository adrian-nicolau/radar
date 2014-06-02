package ro.pub.cs.radar.data;

import java.util.HashMap;

public class AveragePointData {

	private int x;
	private int y;
	private HashMap<String, Float> data;
	
	public AveragePointData(int x, int y, HashMap<String, Float> averageData) {
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

	public HashMap<String, Float> getData() {
		return data;
	}

	public void setData(HashMap<String, Float> data) {
		this.data = data;
	}
}
