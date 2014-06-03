package org.kripthor.miclocsim;
import java.awt.Point;
import java.util.ArrayList;


public class LocationTableEntry {
	
	//THE POINT IN SPACE
	Point p;
	
	//DISTANCE TO EACH MIC
	public ArrayList<Double> dMics;
	//TOA (Time of Arrival) TO EACH MIC
	public ArrayList<Double> tMics;
	//Delta TOA (Time difference between Time of Arrival) TO EACH MIC
	public ArrayList<Double> tdMics;
	
	
	public LocationTableEntry(int x, int y) {
		p = new Point(x,y);
	}

	
	public String toString() {
		return "["+p.x+","+p.y+"]";
	}

	
	//GIVEN A LIST OF MIC LOCATIONS, CALCULATE THE DISTANCES, TOA AND DTOA TO ALL MICS FROM THIS LOCATION
	public void calc(ArrayList<MicInfo> mics, double soundSpeed) {
		dMics = new ArrayList<Double>();
		tMics = new ArrayList<Double>();
		tdMics = new ArrayList<Double>();
		
		//CALCULATE DISTANCES AND TOA
		for (int k = 0; k < mics.size();k++) {
			double dmic = (p.distance(mics.get(k).location));
			double tmic = dmic/soundSpeed;
			dMics.add(dmic);
			tMics.add(tmic);
		}
		
		//CALCULATE ALL DTOA BETWEEN ALL MICS FOR THIS LOCATION, FOR ALL MIC COMBINATIONS
		//IE: MIC2TOA-MIC1TOA, MIC3TOA-MIC1TOA, MIC3TOA-MIC2TOA,....
		for (int k = 0; k < tMics.size()-1;k++) {
			for (int kk = k+1; kk < tMics.size();kk++) {
				tdMics.add(tMics.get(kk)-tMics.get(k));
			}
		}
	}
}
