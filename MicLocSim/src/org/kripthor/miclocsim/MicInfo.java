package org.kripthor.miclocsim;
import java.awt.Point;


public class MicInfo {
	
	public Point location;
	public int sample;
	public int maxValue;
	
	public String toString() {
		return  "["+location.x+","+location.y+"] S: "+sample+ " V: "+maxValue;
	}

}
