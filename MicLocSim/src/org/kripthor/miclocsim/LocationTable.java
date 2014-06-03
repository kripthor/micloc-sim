package org.kripthor.miclocsim;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class LocationTable {

	public static int						LOCMETHOD_AVGALLERRORS	= 1;
	public static int						LOCMETHOD_2MICCROSSOVER	= 2;
	public static int						LOCMETHOD_TRIANGLECROSSOVER	= 3;

	public ArrayList<LocationTableEntry>	table;
	public ArrayList<MicInfo>				mics;
	public double							sampleFrequency;
	public double							minDiff;
	public int								scale;
	public int								x, y, sizex, sizey;
	public int possibleLocs = 0;
	
	public LocationTable(double sampleFrequency, int scale) {
		table = new ArrayList<LocationTableEntry>();
		this.sampleFrequency = sampleFrequency;
		minDiff = 1.0 / sampleFrequency;
		this.scale = scale;
	}

	
	//CALCULATE A GRID OF SIZEX*SIZEY SIZE WITH LOCATIONS AND TIMINGS FOR EACH POINT IN RELATIONSHIP TO MICS
	//SEE LocationTableEntry.calc()
	public void generateTable(ArrayList<MicInfo> mics, double soundSpeed, int x, int y, int sizex, int sizey) {
		this.mics = mics;
		this.x = x;
		this.y = y;
		this.sizex = sizex;
		this.sizey = sizey;

		for (int k = x; k < x + sizex; k++) {
			for (int j = y; j < y + sizey; j++) {
				LocationTableEntry lte = new LocationTableEntry(k * scale, j * scale);
				lte.calc(mics, soundSpeed);
				table.add(lte);
			}
		}
	}
	
	
	//GENERATE AN HEATMAP FOR ALL POINTS, GIVEN AN ORIGIN OF A SOUND EVENT AND A LOCATION METHOD
	//THE MORE COLOR ON THE POINT, THE CLOSER TO THE REAL LOCATION IT IS
	//WHITE COLOR MEANS THAT FOR THIS SAMPLE RATE IT IS NO LONGER POSSIBLE TO DIFFERENTIATE
	//A REAL LOCATION, IE, NOT ENOUGH PRECISION
	public BufferedImage heatMap(LocationTableEntry origin, int locMethod) {
		possibleLocs = 0;
		BufferedImage image = new BufferedImage(sizex + 1, sizey + 1, BufferedImage.TYPE_4BYTE_ABGR);
		image.createGraphics();
		Graphics2D g2d = (Graphics2D) image.getGraphics();

		double r, g, b, a;
		LocationTableEntry best = null;
		double bestAlpha = 99999999;
		

		if (locMethod == LOCMETHOD_AVGALLERRORS) {
			for (LocationTableEntry lte : table) {
				// DO HEAT MAP
				g = r = b = a = 0;
				a = avgAllErrors(lte, origin);

				if (a < bestAlpha) {
					bestAlpha = a;
					best = lte;
				}

				if (a < minDiff) {
					g = 255;
					r = 255;
					b = 255;
					possibleLocs++;
				}

				b = (minDiff / a * 256) * 3;
				a *= 100;
				setRGB(image, lte.p.x / scale + sizex / 2, -lte.p.y / scale + sizey / 2 - 1, (int) Math.round(r), (int) Math.round(g), (int) Math.round(b), (int) Math.round(a));
			}
		}

		if (locMethod == LOCMETHOD_2MICCROSSOVER || locMethod == LOCMETHOD_TRIANGLECROSSOVER) {
			ArrayList<ArrayList> micPosCombinations = null;
			if (locMethod == LOCMETHOD_2MICCROSSOVER) micPosCombinations = getDualCombinations();
			if (locMethod == LOCMETHOD_TRIANGLECROSSOVER) micPosCombinations = getTriangleCombinations();

			for (LocationTableEntry lte : table) {
				int c = 0;
				double aa = 0;
				int allPossibleLocs = 0;
				g = r = b = a = 0;

				for (ArrayList micposi : micPosCombinations) {

					// DO HEAT MAP
					a = avgErrors(lte, origin, micposi);
					if (a < bestAlpha) {
						bestAlpha = a;
						best = lte;
					}
					if (a < minDiff) {
						allPossibleLocs++;
					}
					c++;
					aa += a;
				}

				if (allPossibleLocs == micPosCombinations.size()) {
					r = 255;
					g = 255;
					b = 255;
					possibleLocs++;
				}

				a = aa * 1.0 / c;
				if (locMethod == LOCMETHOD_2MICCROSSOVER) g = (minDiff / a * 256) * 3;
				if (locMethod == LOCMETHOD_TRIANGLECROSSOVER) r = (minDiff / a * 256) * 3;
				a *= 100;
				setRGB(image, lte.p.x / scale + sizex / 2, -lte.p.y / scale + sizey / 2 - 1, (int) Math.round(r), (int) Math.round(g), (int) Math.round(b), (int) Math.round(a));
			}
		}

		for (MicInfo mic1 : mics) {
			g2d.setColor(Color.RED);
			g2d.fillOval(mic1.location.x / scale + sizex / 2, -mic1.location.y / scale + sizey / 2 - 1, 5, 5);
		}
		g2d.setColor(Color.MAGENTA);
		g2d.fillOval(origin.p.x / scale + sizex / 2 - 4, -origin.p.y / scale + sizey / 2 - 1 - 4, 8, 8);

		g2d.setColor(Color.ORANGE);
		g2d.fillOval(best.p.x / scale + sizex / 2 - 2, -best.p.y / scale + sizey / 2 - 1 - 2, 4, 4);

		g2d.setColor(Color.YELLOW);
		g2d.setFont(new Font("Courier", Font.PLAIN, 14));
		g2d.drawString("Real Distance Mic1: " + Math.round(origin.p.distance(mics.get(0).location) / 10) + " cm", 10, 30);
		g2d.drawString("Best Distance Mic1: " + Math.round(best.p.distance(mics.get(0).location) / 10) + " cm", 10, 50);

		// alpha diff best
		double alpha = 0;
		for (int i = 0; i < best.tdMics.size(); i++) {
			alpha += Math.abs(best.tdMics.get(i) - origin.tdMics.get(i));
		}

		g2d.drawString("Best Alpha: " + alpha, 10, 70);
		g2d.drawString("Possible locs: " + possibleLocs, 10, 90);

		g2d.setColor(Color.YELLOW);
		g2d.setFont(new Font("Courier", Font.PLAIN, 10));
		g2d.drawString("Scale 1px = "+ scale +" mm", 10, sizey-10);
	
		g2d.setColor(Color.ORANGE);
		for (int k = 0; k < sizex; k+=10) {
			g2d.drawLine(k, sizey, k, sizey-3);
			if (k % 100 == 0) {
				g2d.drawLine(k, sizey-3, k, sizey-6);
			}
		}
		
		for (int k = 0; k < sizex; k+=10) {
			g2d.drawLine(sizex, k, sizex-3, k);
				if (k % 100 == 0) {
					g2d.drawLine(sizex-3, k, sizex-6, k);
			}
		}

		return image;
	}

	
	
	//GET AN ARRAY OF ALL POSSIBLE TRIPLETS OF MICS FOR THIS MIC CONFIGURATION
	//FOR EXAMPLE: IF THERE IS 3 MICS IN THE SETUP, THERE WILL ONLY BE ONE COMBINATION
	//IF THERE IS 4 MICS IN THE SETUP, WE CAN HAVE 4 POSSIBLE 'TRIANGLES' FOR THE 4 VERTICES
	//(LAZY CODING FOLLOWS)
	private ArrayList<ArrayList> getTriangleCombinations() {
		ArrayList<ArrayList> micPosCombinations = new ArrayList<ArrayList>();
		ArrayList<Integer> micPos;

		if (mics.size() == 3) {
			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(2);
			micPosCombinations.add(micPos);
		}

		if (mics.size() == 4) {
			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(2);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(3);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(2);
			micPos.add(3);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(1);
			micPos.add(2);
			micPos.add(3);
			micPosCombinations.add(micPos);
		}
		
		if (mics.size() == 5) {
			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(2);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(3);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(4);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(1);
			micPos.add(2);
			micPos.add(3);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(1);
			micPos.add(2);
			micPos.add(4);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(2);
			micPos.add(3);
			micPos.add(4);
			micPosCombinations.add(micPos);
		}
		if (mics.size() == 6) {
			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(2);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(3);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(4);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(0);
			micPos.add(1);
			micPos.add(5);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(1);
			micPos.add(2);
			micPos.add(3);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(1);
			micPos.add(2);
			micPos.add(4);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(1);
			micPos.add(2);
			micPos.add(5);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(2);
			micPos.add(3);
			micPos.add(4);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(2);
			micPos.add(3);
			micPos.add(5);
			micPosCombinations.add(micPos);

			micPos = new ArrayList<Integer>();
			micPos.add(3);
			micPos.add(4);
			micPos.add(5);
			micPosCombinations.add(micPos);

		}

		return micPosCombinations;
	}

	
	//GET AN ARRAY OF ALL POSSIBLE PAIRS OF MICS FOR THIS MIC CONFIGURATION
	//FOR EXAMPLE: IF THERE IS 3 MICS IN THE SETUP, THERE WILL BE 3 POSSIBLE COMBINATIONS
	//IE, (MIC1,MIC2), (MIC1,MIC3), (MIC2,MIC3)
	private ArrayList<ArrayList> getDualCombinations() {
		ArrayList<ArrayList> micPosCombinations = new ArrayList<ArrayList>();
		ArrayList<Integer> micPos;

		for (int k = 0; k < mics.size(); k++) {
			for (int j = k+1; j < mics.size(); j++) {
				micPos = new ArrayList<Integer>();
				micPos.add(k);
				micPos.add(j);
				micPosCombinations.add(micPos);
			}
		}
		return micPosCombinations;
	}

	
	//SUM AND AVERAGE THE DIFERENCE OF ALL DTOA BETWEEN TWO POINTS
	private double avgAllErrors(LocationTableEntry lte, LocationTableEntry origin) {
		double a = 0;
		for (int i = 0; i < lte.tdMics.size(); i++) {
			a += Math.abs(lte.tdMics.get(i) - origin.tdMics.get(i));
		}
		a /= lte.tdMics.size();
		return a;
	}

	//SUM AND AVERAGE THE DIFERENCE OF DTOA BETWEEN TWO POINTS, GIVEN A LIST OF MIC POSITIONS TO AVERAGE
	private double avgErrors(LocationTableEntry lte, LocationTableEntry origin, ArrayList<Integer> micPos) {
		double a = 0;
		for (int i = 0; i < micPos.size(); i++) {
			a += (Math.abs(lte.tdMics.get(micPos.get(i)) - origin.tdMics.get(micPos.get(i))));
		}
		a /= micPos.size();
		return a;
	}

	private void setRGB(BufferedImage image, int x, int y, int R, int G, int B, int alpha) {
		try {
			R = Math.max(0, Math.min(255, R));
			G = Math.max(0, Math.min(255, G));
			B = Math.max(0, Math.min(255, B));
			alpha = Math.max(0, Math.min(255, alpha));
			int RGB = (255 - alpha << 24) + (R << 16) + (G << 8) + B;
			image.setRGB(x, y, RGB);
		} catch (Exception e) {
		}
	}

}
