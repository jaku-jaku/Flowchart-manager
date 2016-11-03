import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Slider {
	int x, y, length, currentMarkedLengthX;
	boolean dragged, clicked;

	public Slider() {
		x = 0;
		y = 0;
		length = 80;
		currentMarkedLengthX = 40;
		dragged = false;
		clicked = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int l) {
		length = l;
	}

	public boolean getIfDragged() {
		return dragged;
	}

	public boolean getIfClicked() {
		return clicked;
	}

	public int getCurrentMark() {
		return currentMarkedLengthX;
	}

	public double getPercentage() {
		return currentMarkedLengthX / (double) length;
	}

	public void setCurrentMark(int xx) {
		if (0 <= xx && xx <= length) {
			currentMarkedLengthX = xx;
		} else if (xx < 0) {
			currentMarkedLengthX = 0;
		} else if (xx >= length) {
			currentMarkedLengthX = length;
		}
	}
	
	public void setCurrentMarkPer(double percentage) {
		this.setCurrentMark((int) percentage * length);
	}
	
	public void setSliderState(boolean s){
		clicked=s;
	}
	
	public void sliderStateCheck(int ex,int ey){
		if(x<=ex&&ex<=(x+length)&&(y-6)<=ey&&ey<=(y+12)){
			clicked=true;
			int dif=ex-x;
			this.setCurrentMark(dif);
		}else
			clicked=false;
	}
	
	public void paintSlider(Graphics2D G){
		G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		G.setStroke(new BasicStroke(3));
		
		G.setColor(new Color(120, 120, 140));
		G.fillRoundRect(x, y, length, 8,5,5);
		
		G.setColor(new Color(14, 138, 241));
		G.fillRoundRect(x, y,currentMarkedLengthX, 8,5,5);
		
		G.setColor(new Color(240, 241, 244));
		G.drawRoundRect(x-1, y-1, length, 8,5,5);
		
		G.setColor(new Color(240, 241, 244));
		G.fillOval(x+currentMarkedLengthX-5, y-6, 18, 18);
		
		if(clicked){
			G.setColor(new Color(200,200,240));
		G.fillOval(x+currentMarkedLengthX-4, y-5, 16, 16);
		}
	}

	public void transparency(float i, Graphics g) {
		float alpha = i * 0.1f;
		AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		((Graphics2D) g).setComposite(alcom);
	}
}
