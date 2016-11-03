import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
////////////////////////////////////////////////
////Popping up panel for selecting the color////
////////////////////////////////////////////////

public class ColorBox {
	int x, y, w, h, edgeThickness;
	Color edgeColor, SelectedColor;
	Rectangle Area = new Rectangle();
	boolean ifEntered = false;
	JColorChooser ccc;

	public ColorBox() {
		x = 0;
		y = 0;
		w = 30;
		h = 30;
		edgeThickness = 0;
		edgeColor = new Color(0, 0, 0);
		SelectedColor = new Color(0, 0, 0);
		Area.setBounds(x, y, w, h);
	}

	public ColorBox(int x, int y, int edgeThickness, Color edgeColor, Color SelectedColor) {
		this.x = x;
		this.y = y;
		this.w = 30;
		this.h = 30;
		this.edgeThickness = edgeThickness;
		this.edgeColor = edgeColor;
		this.SelectedColor = SelectedColor;
		Area.setBounds(x, y, w, h);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		Area.setBounds(x, y, w, h);
	}

	public void setX(int x) {
		this.x = x;
		Area.setBounds(x, y, w, h);
	}

	public void setY(int y) {
		this.y = y;
		Area.setBounds(x, y, w, h);
	}

	public void setSize(int width, int height) {
		this.w = w;
		this.h = h;
		Area.setBounds(x, y, w, h);
	}

	public void setEdgeThickness(int ET) {
		this.edgeThickness = ET;
	}

	public void setEdgeColor(Color edgeColor) {
		this.edgeColor = edgeColor;
	}

	public void setSelectedColor(Color SelectedColor) {
		this.SelectedColor = SelectedColor;
	}

	public boolean checkState(MouseEvent e) {
		if (Area.contains(e.getX(), e.getY())) {
			ifEntered = true;
		}
		return ifEntered;
	}

	public void paintMySelf(Graphics2D G) {
		G.setStroke(new BasicStroke(edgeThickness));
		G.setColor(edgeColor);
		G.drawRect(x - 1, y - 1, w + 1, h + 1);
		G.setColor(SelectedColor);
		G.fillRect(x, y, w, h);
		if(ifEntered){
			if(ccc==null)
				ccc=new JColorChooser(SelectedColor, x+300,y);
			else
				ccc.show();
			ccc.t.start();
			
			if(ccc.ifClickOutside){
				this.SelectedColor=ccc.selectedColor;
				ifEntered=false;
				ccc.hide();
				ccc.ifClickOutside=false;
				ccc.t.stop();
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int getEdgeThickness() {
		return edgeThickness;
	}

	public Color getEdgeColor() {
		return edgeColor;
	}

	public Color getSelectedColor() {
		return SelectedColor;

	}
}
