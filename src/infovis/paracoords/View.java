package infovis.paracoords;

import infovis.scatterplot.Data;
import infovis.scatterplot.Model;
import infovis.scatterplot.Range;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class View extends JPanel {
	private Model model = null;
	private int NUM_KEYS;
	private int DISTANCE;
	private int X_OFFSET;
	private int Y_OFFSET;
	private int Y_LABEL_SPACE;
	private int LINE_LENGTH;
	private int LINE_OFFSET;
	private Rectangle2D areaRectangle = new Rectangle2D.Double();
	private ArrayList<ArrayList<Integer>> lines = new ArrayList<ArrayList<Integer>>();
	private ArrayList<Integer> coord;
	private ArrayList<Integer> columns;
	private ArrayList<Line2D> axis = new ArrayList<>();


	public ArrayList<Line2D> getAxis() {
		return axis;
	}

	public void setAxis(ArrayList<Line2D> axis) {
		this.axis = axis;
	}

	@Override
	public void paint(Graphics g) {
		NUM_KEYS = model.getLabels().size();
		DISTANCE = Math.min(getWidth() / NUM_KEYS, getHeight() / NUM_KEYS);
		X_OFFSET = (int) (getWidth() * 0.1);
		Y_OFFSET = (int) (getHeight() * 0.1);
		Y_LABEL_SPACE = (int) (getHeight() * 0.05);
		LINE_LENGTH = Y_OFFSET * 9;
		LINE_OFFSET = (int) ((LINE_LENGTH - Y_LABEL_SPACE - Y_OFFSET) * 0.1);

		Graphics2D g2D = (Graphics2D) g;
		g2D.clearRect(0, 0, getWidth(), getHeight());

		
		if(columns == null) {
			columns = new ArrayList<>();
			for(int i = 0; i < NUM_KEYS; i++) {
				columns.add(X_OFFSET + i * DISTANCE);
			}
			
		}
		
		ArrayList<String> models = model.getLabels();
		ArrayList<Range> r = model.getRanges();
		
		
		for (int i = 0; i < NUM_KEYS; i++) {
			// lines with labels
			//g2D.drawLine(X_OFFSET + i * DISTANCE, Y_OFFSET, 
			//		X_OFFSET + i * DISTANCE, LINE_LENGTH - Y_LABEL_SPACE);
			
			
			
			
			

			g2D.drawLine(columns.get(i), Y_OFFSET, columns.get(i), LINE_LENGTH - Y_LABEL_SPACE);
			g2D.drawString(models.get(i), i * DISTANCE + X_OFFSET - 20, LINE_LENGTH);

			for (Data d : model.getList()) {
				// data points on each line
				double ratio1 = ((LINE_LENGTH - Y_LABEL_SPACE - Y_OFFSET - LINE_OFFSET)
						/ (r.get(i).getMax() - r.get(i).getMin()));
				int y_coord = (int) (ratio1 * (r.get(i).getMax() - d.getValue(i)) + LINE_OFFSET / 2);
				
				g2D.setColor(d.getColor());
				g2D.drawRect(columns.get(i), Y_OFFSET + y_coord - 1, 2, 2);
				//reset
				g2D.setStroke(new BasicStroke(1));
				g2D.setColor(Color.BLACK);
			}
		}
		for (int i = 0; i < NUM_KEYS; i++) {
			// lines with labels
			//g2D.drawLine(X_OFFSET + i * DISTANCE, Y_OFFSET, X_OFFSET + i * DISTANCE, LINE_LENGTH - Y_LABEL_SPACE);
			//g2D.drawString(models.get(i), i * DISTANCE + X_OFFSET - 20, LINE_LENGTH);

			for (Data d : model.getList()) {
				// exclude last element i+1 i.e NUM_KEYS+1
				if (i == NUM_KEYS - 1)
					break;
				
				// lines to connect data points
				// data points on each line
				double ratio1 = ((LINE_LENGTH - Y_LABEL_SPACE - Y_OFFSET - LINE_OFFSET)
						/ (r.get(i).getMax() - r.get(i).getMin()));
				int y_coord = (int) (ratio1 * (r.get(i).getMax() - d.getValue(i)) + LINE_OFFSET / 2);
				
				double ratio2 = ((LINE_LENGTH - Y_LABEL_SPACE - Y_OFFSET - LINE_OFFSET)
						/ (r.get(i + 1).getMax() - r.get(i + 1).getMin()));
				int y2_coord = (int) (ratio2 * (r.get(i + 1).getMax() - d.getValue(i + 1)) + LINE_OFFSET / 2);

				
				//checking color of the data point and changing the line related to it
				if(d.getColor() == Color.RED) {
					//save lines in the array
					coord = new ArrayList<Integer>();
					coord.add(columns.get(i) - X_OFFSET);
					coord.add(y_coord);
					coord.add((columns.get(i+1) - X_OFFSET));
					coord.add(y2_coord);
					lines.add(coord);
				}
				else {
					g2D.setColor(Color.BLUE);
					g2D.setStroke(new BasicStroke(1));
					g2D.drawLine(columns.get(i), Y_OFFSET + y_coord, columns.get(i+1),Y_OFFSET + y2_coord);
				}	
				
				//reset
				g2D.setColor(Color.BLACK);
				g2D.setStroke(new BasicStroke(1));

			}
		}
		//draw highlighted lines
		g2D.setColor(Color.RED);
		g2D.setStroke(new BasicStroke(2));
		
		for (int i = 0; i < lines.size(); i++) {
			g2D.drawLine(X_OFFSET + lines.get(i).get(0), Y_OFFSET + lines.get(i).get(1), 
					X_OFFSET + lines.get(i).get(2),Y_OFFSET + lines.get(i).get(3));
		}
		//reset and clear array
		lines = new ArrayList<>();
		g2D.setColor(Color.BLACK);
		g2D.setStroke(new BasicStroke(1));
		

		// draw areaRect
		g2D.setColor(Color.RED);
		g2D.draw(getAreaRectangle());

	}

	public Rectangle2D getAreaRectangle() {
		return areaRectangle;
	}

	public void setAreaRectangle(Rectangle2D areaRectangle) {
		this.areaRectangle = areaRectangle;
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void refresh() {
		// TODO Auto-generated method stub
		for (Data d : model.getList()) {
			d.setColor(Color.BLACK);
		}

	}

	public void highlight(Rectangle2D areaRect) {
		// TODO Auto-generated method stub
		ArrayList<String> models = model.getLabels();
		ArrayList<Range> r = model.getRanges();
		for (int i = 0; i < NUM_KEYS; i++) {
			for (Data d : model.getList()) {

				double ratio = ((LINE_LENGTH - Y_LABEL_SPACE - Y_OFFSET - LINE_OFFSET)
						/ (r.get(i).getMax() - r.get(i).getMin()));
				int y_coord = (int) (ratio * (r.get(i).getMax() - d.getValue(i)) + LINE_OFFSET / 2);
				int x_coord = i * DISTANCE;
				
				
				int rectX = (int) (areaRect.getX() - X_OFFSET);
				int rectY = (int) (areaRect.getY() - Y_OFFSET);
				int rectXf = (int) (rectX + areaRect.getWidth());
				int rectYf = (int) (rectY + areaRect.getHeight());
				
				
				//
				
				if (x_coord >= rectX && x_coord <= rectXf) {
					if (y_coord >= rectY && y_coord <= rectYf) {
						d.setColor(Color.RED);
					}
				}

			}
		}

		 setAreaRectangle(new Rectangle2D.Double(0, 0, 0, 0));
	}

	public int axisContains(int x, int y) {
		// TODO Auto-generated method stub
		int a = -1;
		for(int i = 0; i < NUM_KEYS; i++) {
			if(columns.get(i) == x) {
				a = i;
				break;
			}
		}
		return a;
	}

	public void setColumns(int i, int x) {
		// TODO Auto-generated method stub
		columns.set(i, x);
		//System.out.println("new X " + columns[i]);
	}
	

}
