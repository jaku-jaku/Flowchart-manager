import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Switch {
	int x, y, n;
	boolean clicked;

	public Switch() {
		x = 0;
		y = 0;
		n = 0;// Circle Shift Distance
		clicked = true;
	}

	public boolean getState(){
		return clicked;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setPosition(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void setX(int x){
		this.x=x;
	}
	
	public void setY(int y){
		this.y=y;
	}
	
	public void SwitchStateCheck(int ex, int ey) {
		if (x <= ex && ex <= (x + 40) && y <= ey && ey <= (y + 20)) {
			if (clicked)
				clicked = false;
			else
				clicked = true;
		}
	}

	public void drawSwitch(Graphics2D G) {
		if (clicked)
			n = 22;
		else
			n = 0;
		
		G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (clicked)
			G.setColor(new Color(14, 138, 241));
		else
		G.setColor(new Color(120,120,140));
		G.fillRoundRect(x, y, 40, 20, 20, 20);
		G.setColor(new Color(240, 241, 244));
		G.setStroke(new BasicStroke(3));
		G.drawRoundRect(x, y, 40, 20, 20, 20);
		transparency(3, G);
		G.setColor(new Color(10, 10, 10));
		G.fillOval(x - 1 + n, y - 1, 20 + 3, 20 + 3);
		transparency(10, G);
		G.setColor(new Color(240, 241, 244));
		G.fillOval(x + n, y, 20, 20);
	}

	public void transparency(float i, Graphics g) {
		float alpha = i * 0.1f;
		AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		((Graphics2D) g).setComposite(alcom);
	}
}
