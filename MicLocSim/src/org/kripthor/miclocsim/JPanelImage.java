package org.kripthor.miclocsim;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JPanelImage extends JPanel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public BufferedImage	image;
	int						x, y;

	public JPanelImage() {
		super();
	}	
	
	public JPanelImage(BufferedImage image, int x, int y) {
		super();
		this.image = image;
		this.x = x;
		this.y = y;
	}


	public synchronized void setImage(BufferedImage image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}
	
	public static BufferedImage loadImage(String ref) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(ref));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) g.drawImage(image, x, y, null);
	}
}