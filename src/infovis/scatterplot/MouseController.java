package infovis.scatterplot;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;
	Point startDrag, endDrag;

	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		
		startDrag = new Point(x, y);
        endDrag = startDrag;
        
        view.refresh();
		
		//Iterator<Data> iter = model.iterator();
		//view.getMarkerRectangle().setRect(x,y,w,h);
		view.repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		//add rect view
		view.setAreaRectangle(new Rectangle(startDrag.x, startDrag.y, 
				Math.abs(arg0.getX()-startDrag.x), Math.abs(arg0.getY()-startDrag.y)));

        startDrag = null;
        endDrag = null;
        view.repaint();
        
        view.brushAndLink(view.getAreaRectangle());
	}

	public void mouseDragged(MouseEvent arg0) {
		//endDrag = new Point(arg0.getX(), arg0.getY());
		view.setAreaRectangle(new Rectangle(startDrag.x, startDrag.y, 
				Math.abs(arg0.getX()-startDrag.x), Math.abs(arg0.getY()-startDrag.y)));
		view.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void setModel(Model model) {
		this.model  = model;	
	}

	public void setView(View view) {
		this.view  = view;
	}

}
