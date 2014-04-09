package ro.pub.cs.radar;

import java.util.ArrayList;
import java.util.HashMap;

public class PointData {

	public int x;
	public int y;
	public ArrayList<HashMap<String, Integer>> samples;
	
	public PointData(int x, int y, ArrayList<HashMap<String, Integer>> samples) {
		this.x = x;
		this.y = y;
		this.samples = samples;
	}
}
