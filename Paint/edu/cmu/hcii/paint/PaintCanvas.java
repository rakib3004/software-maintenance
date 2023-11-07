package edu.cmu.hcii.paint;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PaintCanvas extends JPanel {

    Vector history;
    
    Vector paintObjects;

    private PaintObject temporaryObject;
    private PaintObject hoveringObject;
    
    public PaintCanvas(int initialWidth, int initialHeight) {
        
        setPreferredSize(new Dimension(initialWidth, initialHeight));
        
        paintObjects = new Vector();
        
        history = new Vector();
        
    }

    public void setResizeCanvas(int updatedWidth, int updatedHeight){
        setPreferredSize(new Dimension(updatedWidth, updatedHeight));
    }

    public void updatePreferredSize(PaintObject newObject) {
        Rectangle objectBounds = newObject.getBoundingBox();
        Dimension currentPreferredSize = getPreferredSize();
        int newWidth = Math.max(currentPreferredSize.width, objectBounds.x + objectBounds.width);
        int newHeight = Math.max(currentPreferredSize.height, objectBounds.y + objectBounds.height);

        if (newWidth != currentPreferredSize.width || newHeight != currentPreferredSize.height) {
            setPreferredSize(new Dimension(newWidth, newHeight));
            revalidate();
        }
    }
    public Dimension getPreferredSize() {
        int extendedWidth = 3000;
        int extendedHeight = 2400;
        for (Object obj : paintObjects) {
            PaintObject temp = (PaintObject) obj;
            Rectangle newBounds = temp.getBoundingBox();
            extendedWidth = Math.max(extendedWidth, newBounds.x + newBounds.width);
            extendedHeight = Math.max(extendedHeight, newBounds.y + newBounds.height);
        }
        return new Dimension(extendedWidth, extendedHeight);
    }


    public void paintComponent(Graphics g) {
        
		((Graphics2D) g).addRenderingHints(
			new java.awt.RenderingHints(
				java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON));

        super.paintComponent(g);

//        Rectangle clipBounds = g.getClipBounds();
//        g.setColor(Color.white);
//        g.fillRect((int)clipBounds.getX(), (int)clipBounds.getX(),
//                    (int)clipBounds.getWidth(), (int)clipBounds.getHeight());



        Iterator paintObjectIterator = paintObjects.iterator();
        while(paintObjectIterator.hasNext())
			try {
		        ((PaintObject)paintObjectIterator.next()).paint((Graphics2D)g); 
			} catch(Exception e) { 
				System.err.println("The graphics context isn't a Graphics2D. No anti-aliasing!");
			}
        
        if(temporaryObject != null) temporaryObject.paint((Graphics2D)g);
        
		if(hoveringObject != null) {
			
			Rectangle rect = hoveringObject.getBoundingBox();
			g.setColor(Color.black);
			g.drawRect((int)rect.getX() - 1, (int)rect.getY() - 1, (int)rect.getWidth() + 2, (int)rect.getHeight() + 2);
			hoveringObject.paint((Graphics2D)g);
			
		}
        
    }
    
    public int sizeOfHistory() { return history.size(); }
    
    public void setTemporaryObject(PaintObject temporaryObject) {
        
        this.temporaryObject = temporaryObject;
        repaint();
        
    }
    
    public void setHoveringObject(PaintObject hoveringObject) {
    	
    	this.hoveringObject = hoveringObject;
    	repaint();
    	
    }

    public void addPaintObject(PaintObject newObject) {

        history.addElement(new Vector(paintObjects));
        paintObjects.addElement(newObject);
       // updatePreferredSize(newObject);
        repaint();

    }
    
    public void clear() {
        
        history.addElement(new Vector(paintObjects));
        paintObjects.removeAllElements();
        repaint();

    }

    public void undo() { 
        
        paintObjects = (Vector)history.lastElement();
        history.removeElement(history.lastElement());
        repaint();
        
    }


}
