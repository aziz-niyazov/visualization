package infovis.paracoords;

import infovis.scatterplot.Model;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	Shape currentShape = null;
	Point startDrag, endDrag;
	private boolean movingLine = false;
	private double mouseOffsetX;
	private double mouseOffsetY;
	private int axisIndex;
	private int axisVal;
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		axisIndex = view.axisContains(e.getX(), e.getY());
		if(axisIndex != -1) {
			movingLine = true;
			mouseOffsetX = e.getX();
			mouseOffsetY = e.getY();
			axisVal = e.getX();
			System.out.println("CONTAINS " + axisVal);
		}else {
			System.out.println("NOT CONTAINS " + e.getX());
		startDrag = new Point(e.getX(), e.getY());
        endDrag = startDrag;
         view.refresh();
		}

        view.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		if(movingLine) {
			//view.setColumns(axisIndex, 20);
			movingLine = false;
		}else {
			view.setAreaRectangle(new Rectangle(startDrag.x, startDrag.y, 
					Math.abs(e.getX()-startDrag.x), Math.abs(e.getY()-startDrag.y)));	
		}
		

        startDrag = null;
        endDrag = null;
        view.repaint();
        view.highlight(view.getAreaRectangle());
	}

	public void mouseDragged(MouseEvent e) {
		
		if(movingLine) {
			view.setColumns(axisIndex, (int) (e.getX()));

			mouseOffsetX = e.getX();
			mouseOffsetY = e.getY();
		}else {
			view.setAreaRectangle(new Rectangle(startDrag.x, startDrag.y, 
					Math.abs(e.getX()-startDrag.x), Math.abs(e.getY()-startDrag.y)));	
		}
		
		
		view.repaint();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
