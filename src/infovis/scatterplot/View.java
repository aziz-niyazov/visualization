package infovis.scatterplot;

import infovis.debug.Debug;
import sun.java2d.loops.DrawRect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class View extends JPanel {
	private Model model = null;
	private Rectangle2D markerRectangle = new Rectangle2D.Double(0, 0, 0, 0);
	private Rectangle2D areaRectangle = new Rectangle2D.Double();

	private int PLOT_SIZE;
	private int X_LABEL_SPACE;
	private int Y_LABEL_SPACE;
	private int NUM_KEYS;
	private int OFFSET;

	public Rectangle2D getAreaRectangle() {
		return areaRectangle;
	}

	public void setAreaRectangle(Rectangle2D areaRectangle) {
		this.areaRectangle = areaRectangle;
	}

	public void setMarkerRectangle(Rectangle2D markerRectangle) {
		this.markerRectangle = markerRectangle;
	}

	public Rectangle2D getMarkerRectangle() {
		return markerRectangle;
	}

	@Override
	public void paint(Graphics g) {

		X_LABEL_SPACE = (int) (getWidth() * 0.1); // 10% of the screen to the labels
		Y_LABEL_SPACE = (int) (getHeight() * 0.1);
		NUM_KEYS = model.getLabels().size();
		PLOT_SIZE = Math.min((getHeight() - X_LABEL_SPACE) / NUM_KEYS, (getWidth() - Y_LABEL_SPACE) / NUM_KEYS);
		OFFSET = (int) (PLOT_SIZE * 0.1);

		Graphics2D g2D = (Graphics2D) g;
		g2D.clearRect(0, 0, getWidth(), getHeight());

		// matrix grid
		for (int i = 0; i < NUM_KEYS; i++) {
			for (int j = 0; j < NUM_KEYS; j++) {
				g2D.drawRect(X_LABEL_SPACE + PLOT_SIZE * i, Y_LABEL_SPACE + PLOT_SIZE * j, PLOT_SIZE, PLOT_SIZE);
			}

		}
		// labels
		ArrayList<String> models = model.getLabels();
		for (int i = 0; i < NUM_KEYS; i++) {
			for (int j = 0; j < NUM_KEYS; j++) {
				g2D.drawString(models.get(j), 0, PLOT_SIZE + Y_LABEL_SPACE + PLOT_SIZE * j);
			}
			g2D.drawString(models.get(i), X_LABEL_SPACE + PLOT_SIZE * i, Y_LABEL_SPACE / 2);
		}

		// data points
		// get list of car Data

		ArrayList<Range> r = model.getRanges();
		// ArrayList<Data> d = model.getList();
		// matrix grid
		// System.out.println("keys " + NUM_KEYS);
		for (int x = 0; x < NUM_KEYS; x++) {
			// System.out.println("XXXXXXXXXXXXXXXXXXXXX-----------------------");
			for (int y = 0; y < NUM_KEYS; y++) {
				for (Data d : model.getList()) {
					double ratioX = (PLOT_SIZE - OFFSET) / (r.get(x).getMax() - r.get(x).getMin());
					int x_coord = (int) (ratioX * (r.get(x).getMax() - d.getValue(x)) + OFFSET / 2);

					// System.out.println("ratioX " + ratioX);
					// System.out.println("range " + r.get(x));
					// System.out.println("current X " + d.getValue(x));
					// System.out.println("x_coord " + x_coord);
					//
					// System.out.println("YYYYYYYYYYYYYYYYYYY");
					// double ratio = PLOT_SIZE/d.get(x).getValue(y);
					// System.out.println("vvvvv "+ d.get(x).getValue(y));
					double ratioY = (PLOT_SIZE - OFFSET) / (r.get(y).getMax() - r.get(y).getMin());
					int y_coord = (int) (ratioY * (r.get(y).getMax() - d.getValue(y)) + OFFSET / 2);

					// System.out.println("ratioY " + ratioY);
					// System.out.println("range " + r.get(y));
					// System.out.println("current Y " + d.getValue(y));
					// System.out.println("y_coord " + y_coord);
					//
					// System.out.println(X_LABEL_SPACE + "====" + PLOT_SIZE);
					g2D.setColor(d.getColor());
					g2D.drawRect(X_LABEL_SPACE + x_coord + x * PLOT_SIZE - 1,
							Y_LABEL_SPACE + y_coord + y * PLOT_SIZE - 1, 3, 3);
				}

			}
		}

		// draw areaRect
		g2D.setColor(Color.RED);
		g2D.draw(getAreaRectangle());
		
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void brushAndLink(Rectangle2D areaRect) {
		// TODO Auto-generated method stub
//		for (Data d : model.getList()) {
//
//		}
		
		ArrayList<Range> r = model.getRanges();
		for (int x = 0; x < NUM_KEYS; x++) {
			for (int y = 0; y < NUM_KEYS; y++) {
				//intersection
				int cellcoordX = (int) ((areaRect.getX() - X_LABEL_SPACE)/PLOT_SIZE);
				int cellcoordY = (int) ((areaRect.getY() - Y_LABEL_SPACE)/PLOT_SIZE);
				System.out.println("CELL " + cellcoordX + " " + cellcoordY);
				
				for (Data d : model.getList()) {
					double ratioX = (PLOT_SIZE - OFFSET) / (r.get(cellcoordX).getMax() - r.get(cellcoordX).getMin());
					int x_coord = (int) (ratioX * (r.get(cellcoordX).getMax() - d.getValue(cellcoordX)) + OFFSET / 2);

					double ratioY = (PLOT_SIZE - OFFSET) / (r.get(cellcoordY).getMax() - r.get(cellcoordY).getMin());
					int y_coord = (int) (ratioY * (r.get(cellcoordY).getMax() - d.getValue(cellcoordY)) + OFFSET / 2);
					
					
					int rectX = (int) (areaRect.getX() - cellcoordX*PLOT_SIZE - X_LABEL_SPACE);
					int rectY = (int) (areaRect.getY() - cellcoordY*PLOT_SIZE - Y_LABEL_SPACE);
					int rectXf = (int) (rectX + areaRect.getWidth());
					int rectYf = (int) (rectY + areaRect.getHeight());
					
					
					if(x_coord >= rectX && x_coord <= rectXf) {
						if(y_coord >= rectY && y_coord <= rectYf) {
							d.setColor(Color.GREEN);

							System.out.println("rect " + rectX + " " + rectY + " "+ 
									rectXf + " " + rectYf);
							System.out.println("data " + x_coord + " " + y_coord);
						}
					}

					
				}

			}
		}
		setAreaRectangle(new Rectangle2D.Double(0, 0, 0, 0));
	}

	public void refresh() {
		// TODO Auto-generated method stub
		for(Data d: model.getList()) {
			d.setColor(Color.BLACK);
		}
	}
}
