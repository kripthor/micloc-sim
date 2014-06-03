package org.kripthor.miclocsim;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class GUI {

	private JFrame				frmMiclocSimulator;
	private JPanelImage		panelImage;
	private JTextField			textField;
	private JTextField			textField_1;
	private JTextField			textField_2;
	private JTextField			textField_3;
	private JTextField			textField_4;
	private JTextField			textField_5;
	private JTextField			textField_6;
	private JTextField			textField_7;
	private JTextField			textField_8;
	private JTextField			textField_9;
	private JTextField			textField_10;
	private JTextField			textField_11;
	private JTextField			textField_12;
	private JTextField			textField_13;
	private JTextField			textField_14;
	private JTextField			textField_15;

	private JButton			btnZoom;
	private JButton			btnZoom_1;
	private JButton			btnNewButton;
	private JButton			btnS;
	private JButton			btnNewButton_1;
	private JButton			btnH;
	private JRadioButton		rdbtnAvgerrors;
	private JRadioButton		rdbtnAvgbest;
	private JRadioButton		rdbtnTri;
	private JButton			btnBenchmark;

	//GLOBAL VARS FOR SIMULATION
	private ArrayList<MicInfo>	mics;
	private double				soundSpeed	= 339.389;	// ~339 m/s
	private double				samplesms	= 100;		// 100 kHz
	private int				scale		= 1;
	private Point				soundEvent;
	private LocationTable		loctable;
	private boolean			canSimulate	= false;
	private int				locMethod	= 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmMiclocSimulator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMiclocSimulator = new JFrame();
		frmMiclocSimulator.setTitle("MicLoc Simulator (v0.1 alpha)");
		frmMiclocSimulator.setBounds(100, 100, 745, 595);
		frmMiclocSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMiclocSimulator.getContentPane().setLayout(new MigLayout("", "[200px:200px:200px][520px:520px:520px]", "[520px:520px:520px][]"));

		JPanel panel = new JPanel();
		panel.setBorder(null);
		frmMiclocSimulator.getContentPane().add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[grow]", "[][][][][][][][][][][][][][][][][][][][][][][][][]"));

		JLabel lblSoundSpeed = new JLabel("Sound Speed(m/s):");
		panel.add(lblSoundSpeed, "cell 0 0");

		textField = new JTextField();
		textField.setToolTipText("Define the speed of sound in meters/second");
		textField.setText("340.00");
		panel.add(textField, "cell 0 1,growx");
		textField.setColumns(10);

		JLabel lblSampleSpeedsms = new JLabel("Samples/ms (kHz)");
		panel.add(lblSampleSpeedsms, "cell 0 2");

		textField_1 = new JTextField();
		textField_1.setToolTipText("Define the sample rate from mics");
		textField_1.setText("300");
		panel.add(textField_1, "cell 0 3,growx");
		textField_1.setColumns(10);

		JLabel lblMicPosition = new JLabel("Mic 1 Position X/Y:");
		panel.add(lblMicPosition, "cell 0 4");

		textField_2 = new JTextField();
		textField_2.setToolTipText("Mic position in milimeters");
		textField_2.setText("0");
		panel.add(textField_2, "flowx,cell 0 5,growx");
		textField_2.setColumns(10);

		JLabel label = new JLabel("/");
		panel.add(label, "cell 0 5");

		textField_3 = new JTextField();
		textField_3.setToolTipText("Mic position in milimeters");
		textField_3.setText("-200");
		panel.add(textField_3, "cell 0 5");
		textField_3.setColumns(10);

		JLabel lblMicPosition_1 = new JLabel("Mic 2 Position X/Y:");
		panel.add(lblMicPosition_1, "cell 0 6");

		textField_4 = new JTextField();
		textField_4.setToolTipText("Mic position in milimeters");
		textField_4.setText("-173");
		panel.add(textField_4, "flowx,cell 0 7,growx");
		textField_4.setColumns(10);

		JLabel label_1 = new JLabel("/");
		panel.add(label_1, "cell 0 7");

		textField_5 = new JTextField();
		textField_5.setToolTipText("Mic position in milimeters");
		textField_5.setText("100");
		panel.add(textField_5, "cell 0 7");
		textField_5.setColumns(10);

		JLabel lblMicPosition_2 = new JLabel("Mic 3 Position X/Y:");
		panel.add(lblMicPosition_2, "cell 0 8");

		textField_6 = new JTextField();
		textField_6.setToolTipText("Mic position in milimeters");
		textField_6.setText("173");
		textField_6.setColumns(10);
		panel.add(textField_6, "flowx,cell 0 9,growx");

		JLabel lblMicPosition_3 = new JLabel("Mic 4 Position X/Y:");
		panel.add(lblMicPosition_3, "cell 0 10");

		textField_7 = new JTextField();
		textField_7.setToolTipText("Mic position in milimeters");
		textField_7.setColumns(10);
		panel.add(textField_7, "flowx,cell 0 11,growx");

		JLabel lblMicPosition_4 = new JLabel("Mic 5 Position X/Y:");
		panel.add(lblMicPosition_4, "cell 0 12");

		textField_8 = new JTextField();
		textField_8.setToolTipText("Mic position in milimeters");
		textField_8.setColumns(10);
		panel.add(textField_8, "flowx,cell 0 13,growx");

		JLabel lblMicPosition_5 = new JLabel("Mic 6 Position X/Y:");
		panel.add(lblMicPosition_5, "cell 0 14");

		textField_9 = new JTextField();
		textField_9.setToolTipText("Mic position in milimeters");
		textField_9.setColumns(10);
		panel.add(textField_9, "flowx,cell 0 15,growx");

		JLabel label_2 = new JLabel("/");
		panel.add(label_2, "cell 0 9");

		textField_10 = new JTextField();
		textField_10.setToolTipText("Mic position in milimeters");
		textField_10.setText("100");
		textField_10.setColumns(10);
		panel.add(textField_10, "cell 0 9");

		JLabel label_3 = new JLabel("/");
		panel.add(label_3, "cell 0 11");

		textField_11 = new JTextField();
		textField_11.setToolTipText("Mic position in milimeters");
		textField_11.setColumns(10);
		panel.add(textField_11, "cell 0 11");

		JLabel label_4 = new JLabel("/");
		panel.add(label_4, "cell 0 13");

		textField_12 = new JTextField();
		textField_12.setToolTipText("Mic position in milimeters");
		textField_12.setColumns(10);
		panel.add(textField_12, "cell 0 13");

		JLabel label_5 = new JLabel("/");
		panel.add(label_5, "cell 0 15");

		textField_13 = new JTextField();
		textField_13.setToolTipText("Mic position in milimeters");
		textField_13.setColumns(10);
		panel.add(textField_13, "cell 0 15");

		btnNewButton = new JButton("T");
		btnNewButton.setToolTipText("Predefined triangular mic setup");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFigure(1);
			}
		});
		panel.add(btnNewButton, "flowx,cell 0 16");

		JLabel lblSoundEventXy = new JLabel("Sound event X/Y:");
		panel.add(lblSoundEventXy, "cell 0 19");

		textField_14 = new JTextField();
		textField_14.setToolTipText("Location of a sound event in millimeters");
		textField_14.setText("200");
		panel.add(textField_14, "flowx,cell 0 20,growx");
		textField_14.setColumns(10);

		JLabel label_6 = new JLabel("/");
		panel.add(label_6, "cell 0 20");

		textField_15 = new JTextField();
		textField_15.setToolTipText("Location of a sound event in millimeters");
		textField_15.setText("200");
		panel.add(textField_15, "cell 0 20");
		textField_15.setColumns(10);

		btnS = new JButton("S");
		btnS.setToolTipText("Predefined square mic setup");
		btnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFigure(2);
			}
		});
		panel.add(btnS, "cell 0 16");

		btnNewButton_1 = new JButton("P");
		btnNewButton_1.setToolTipText("Predefined pentagonal mic setup");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFigure(3);
			}
		});
		panel.add(btnNewButton_1, "cell 0 16");

		btnH = new JButton("H");
		btnH.setToolTipText("Predefined hexagonal mic setup");
		btnH.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFigure(4);
			}
		});
		panel.add(btnH, "cell 0 16");

		rdbtnAvgerrors = new JRadioButton("avgE");
		rdbtnAvgerrors.setToolTipText("Sound location method: Average all errors from sound diferences to mics");
		rdbtnAvgerrors.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnAvgerrors.isSelected()) {
					if (rdbtnAvgbest != null)
						rdbtnAvgbest.setSelected(false);
					if (rdbtnTri != null)
						rdbtnTri.setSelected(false);
					if (canSimulate)
						simulate();
				}
			}
		});
		
		rdbtnAvgerrors.setSelected(true);
		panel.add(rdbtnAvgerrors, "flowx,cell 0 17");

		rdbtnAvgbest = new JRadioButton("Dual");
		rdbtnAvgbest.setToolTipText("Sound location method: Average errors by taking pairs of mics and superimpose common areas of possible locations");
		rdbtnAvgbest.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (rdbtnAvgbest.isSelected()) {
					if (rdbtnAvgerrors != null)
						rdbtnAvgerrors.setSelected(false);
					if (rdbtnTri != null)
						rdbtnTri.setSelected(false);
					if (canSimulate)
						simulate();
				}
			}
		});
	
		panel.add(rdbtnAvgbest, "cell 0 17");

		rdbtnTri = new JRadioButton("Tri");
		rdbtnTri.setToolTipText("Sound location method: Average errors by taking triplets of mics and superimpose common areas of possible locations");
		rdbtnTri.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnTri.isSelected()) {
					if (rdbtnAvgerrors != null)
						rdbtnAvgerrors.setSelected(false);
					if (rdbtnAvgbest != null)
						rdbtnAvgbest.setSelected(false);
					if (canSimulate)
						simulate();
				}
			}
		});
		
		panel.add(rdbtnTri, "cell 0 17");

		JButton btnSimulate = new JButton("Simulate");
		btnSimulate.setToolTipText("Simulate for current parameters and generate heatmap");
		btnSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulate();
			}
		});
		panel.add(btnSimulate, "cell 0 22,growx");

		btnBenchmark = new JButton("360º Benchmark");
		btnBenchmark.setToolTipText("Run several simulations in 360º and accumulate errors for benchmarking parameters");
		btnBenchmark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				benchmark();
			}
		});
		panel.add(btnBenchmark, "cell 0 23,growx");

		panelImage = new JPanelImage();
		panelImage.setToolTipText("Click here after simulation to choose the location of a sound event and simulate again");

		panelImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changeSoundEvent(e.getPoint());
			}

		});
		panelImage.setBackground(Color.BLACK);
		panelImage.setBorder(null);
		frmMiclocSimulator.getContentPane().add(panelImage, "cell 1 0,grow");

		btnZoom_1 = new JButton("Zoom -");
		frmMiclocSimulator.getContentPane().add(btnZoom_1, "flowx,cell 1 1,growx");

		btnZoom = new JButton("Zoom +");
		frmMiclocSimulator.getContentPane().add(btnZoom, "cell 1 1,growx");
		btnZoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoom(-5);
			}
		});
		btnZoom_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoom(5);
			}
		});
	}

	
	
	//DO SEVERAL SIMULATIONS AND ACCUMULATE POSSIBLE LOCATIONS (IE. POSSIBLE MEASUREMENT ERRORS)
	protected void benchmark() {
		parseInputs();
		if (!canSimulate) {
			// TODO error reporting
			return;
		}

		int x, y;
		double r = 200;
		double t;

		int totalLocs = 0;
		int totalSims = 0;

		for (t = 0; t < 2.0 * Math.PI; t += Math.PI / 45) {
			x = (int) Math.round(r * Math.cos(t) * scale);
			y = (int) Math.round(r * Math.sin(t) * scale);

			textField_14.setText(Integer.toString(x));
			textField_15.setText(Integer.toString(y));
			LocationTableEntry lte = new LocationTableEntry(x, y);
			lte.calc(mics, soundSpeed);
			BufferedImage image = loctable.heatMap(lte, locMethod);
			totalSims++;
			totalLocs += loctable.possibleLocs;

			Graphics2D g2d = (Graphics2D) image.getGraphics();
			g2d.setColor(Color.YELLOW);
			g2d.setFont(new Font("Courier", Font.PLAIN, 14));
			g2d.drawString("Total Locs Benchmark: " + totalLocs, 10, 110);
			g2d.drawString("Avg Locs Benchmark: " + (totalLocs / totalSims), 10, 130);

			panelImage.setImage(image, 0, 0);
			this.panelImage.update(this.panelImage.getGraphics());
			this.textField_14.update(this.textField_14.getGraphics());
			this.textField_15.update(this.textField_15.getGraphics());
		}

	}

	
	//DO A SIMULATION AND SHOW HEATMAP
	protected void simulate() {
		parseInputs();
		if (!canSimulate) {
			// TODO error reporting
			return;
		}
		loctable = new LocationTable(samplesms, scale);
		int sizex = panelImage.getWidth() / 2;
		int sizey = panelImage.getHeight() / 2;
		loctable.generateTable(mics, soundSpeed, -sizex, -sizey, panelImage.getWidth(), panelImage.getHeight());

		LocationTableEntry lte = new LocationTableEntry(soundEvent.x, soundEvent.y);
		lte.calc(mics, soundSpeed);

		BufferedImage image = loctable.heatMap(lte, locMethod);

		panelImage.setImage(image, 0, 0);
		this.panelImage.update(this.panelImage.getGraphics());

	}

	
	private void parseInputs() {
		mics = new ArrayList<MicInfo>();
		try {
			// parse sound speed
			soundSpeed = Double.parseDouble(textField.getText());
			// parse samplesms
			samplesms = Double.parseDouble(textField_1.getText());
			// parse mics
			try {
				MicInfo m = new MicInfo();
				m.location = new Point(Integer.parseInt(textField_2.getText()), Integer.parseInt(textField_3.getText()));
				mics.add(m);
			} catch (Exception e) {

			}
			try {
				MicInfo m = new MicInfo();
				m.location = new Point(Integer.parseInt(textField_4.getText()), Integer.parseInt(textField_5.getText()));
				mics.add(m);
			} catch (Exception e) {

			}
			try {
				MicInfo m = new MicInfo();
				m.location = new Point(Integer.parseInt(textField_6.getText()), Integer.parseInt(textField_10.getText()));
				mics.add(m);
			} catch (Exception e) {

			}
			try {
				MicInfo m = new MicInfo();
				m.location = new Point(Integer.parseInt(textField_7.getText()), Integer.parseInt(textField_11.getText()));
				mics.add(m);
			} catch (Exception e) {
			}
			try {
				MicInfo m = new MicInfo();
				m.location = new Point(Integer.parseInt(textField_8.getText()), Integer.parseInt(textField_12.getText()));
				mics.add(m);
			} catch (Exception e) {

			}
			try {
				MicInfo m = new MicInfo();
				m.location = new Point(Integer.parseInt(textField_9.getText()), Integer.parseInt(textField_13.getText()));
				mics.add(m);
			} catch (Exception e) {

			}
			
			locMethod = LocationTable.LOCMETHOD_AVGALLERRORS;
			if (rdbtnAvgbest.isSelected())
				locMethod = LocationTable.LOCMETHOD_2MICCROSSOVER;
			if (rdbtnTri.isSelected())
				locMethod = LocationTable.LOCMETHOD_TRIANGLECROSSOVER;

			// parse sound source event
			soundEvent = new Point(Integer.parseInt(textField_14.getText()), Integer.parseInt(textField_15.getText()));
			canSimulate = true;
		} catch (Exception e) {
			// TODO error reporting
		}
	}

	protected void changeSoundEvent(Point point) {
		parseInputs();
		if (!canSimulate) {
			// TODO error reporting
			return;
		}
		int sizex = panelImage.getWidth() / 2;
		int sizey = panelImage.getHeight() / 2;
		int pointx = (-sizex + point.x) * scale;
		int pointy = (sizey - point.y) * scale;
		textField_14.setText(Integer.toString(pointx));
		textField_15.setText(Integer.toString(pointy));
		LocationTableEntry lte = new LocationTableEntry(pointx, pointy);
		lte.calc(mics, soundSpeed);
		BufferedImage image = loctable.heatMap(lte, locMethod);

		panelImage.setImage(image, 0, 0);
		panelImage.updateUI();
	}

	protected void zoom(int i) {
		parseInputs();
		if (!canSimulate) {
			// TODO error reporting
			return;
		}
		
		if (i > 0) {
			if (scale == 1)
				scale = 5;
			else
				scale += 5;
		}
		if (i < 0)
			scale -= 5;
		if (scale < 1)
			scale = 1;
		simulate();
	}

	protected void setFigure(int i) {
		textField_2.setText("");
		textField_3.setText("");
		textField_4.setText("");
		textField_5.setText("");
		textField_6.setText("");
		textField_10.setText("");
		textField_7.setText("");
		textField_11.setText("");
		textField_8.setText("");
		textField_12.setText("");
		textField_9.setText("");
		textField_13.setText("");
		switch (i) {
			case 1:
				textField_2.setText("0");
				textField_3.setText("-200");
				textField_4.setText("-173");
				textField_5.setText("100");
				textField_6.setText("173");
				textField_10.setText("100");
				break;
			case 2:
				textField_2.setText("141");
				textField_3.setText("-141");
				textField_4.setText("-141");
				textField_5.setText("-141");
				textField_6.setText("-141");
				textField_10.setText("141");
				textField_7.setText("141");
				textField_11.setText("141");
				break;
			case 3:
				textField_2.setText("0");
				textField_3.setText("-200");
				textField_4.setText("-190");
				textField_5.setText("-62");
				textField_6.setText("-118");
				textField_10.setText("162");
				textField_7.setText("118");
				textField_11.setText("162");
				textField_8.setText("190");
				textField_12.setText("-62");
				break;
			case 4:
				textField_2.setText("100");
				textField_3.setText("-173");
				textField_4.setText("-100");
				textField_5.setText("-173");
				textField_6.setText("-200");
				textField_10.setText("0");
				textField_7.setText("-100");
				textField_11.setText("173");
				textField_8.setText("100");
				textField_12.setText("173");
				textField_9.setText("200");
				textField_13.setText("0");
				break;
			default:
				return;
		}
		simulate();
	}

}
