import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

public class TextField {
	int x, y, width, height, fieldLimits;
	boolean clicked;
	Color DisplayColor, StrokeColor, TextColor, ClickColor;
	String info;

	public TextField() {
		info = "0";
		x = 0;
		y = 0;
		fieldLimits = 4;
		height = 20;
		width = 13 * fieldLimits;

		DisplayColor = new Color(14, 138, 241);
		StrokeColor = new Color(240, 241, 244);
		TextColor = new Color(0, 0, 0);
		ClickColor = new Color(120, 120, 140);
	}
	
	public TextField(String Name,int h) {
		info = Name;
		x = 0;
		y = 0;
		height = h+2;
		clicked=false;

		DisplayColor = new Color(14, 138, 241);
		StrokeColor = new Color(240, 241, 244);
		TextColor = new Color(0, 0, 0);
		ClickColor = new Color(120, 120, 140);
	}

	public void setColor(Color DisplayColor, Color StrokeColor, Color TextColor, Color ClickColor) {
		this.DisplayColor = DisplayColor;
		this.StrokeColor = StrokeColor;
		this.TextColor = TextColor;
		this.ClickColor = ClickColor;
	}

	public int getFieldLimits() {
		return fieldLimits;
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

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setState(boolean t){
		clicked=t;
	}

	public void setSize(int w, int h) {
		height = h;
		width = w;
		fieldLimits = width / 13;
	}

	public void setWidth(int w) {
		width = w;
		fieldLimits = width / 13;
	}
	public void setFieldLimits(int limit){
		fieldLimits =limit;
		width=limit*13;
	}

	public void setHeight(int h) {
		height = h;
	}

	public void setString(String n) {
		info = n;
	}

	public String getString() {
		return info;
	}

	public void textStateCheck(int ex, int ey) {
		if (x <= ex && ex <= (x + width) && y <= ey && ey <= (y + height)) {
			if (clicked)
				clicked = false;
			else
				clicked = true;
		} else
			clicked = false;
	}

	public void key(KeyEvent e) {
		if (clicked) {
			String txt = this.getString();
			char k = e.getKeyChar();
			int l = txt.length();
			if (k == KeyEvent.VK_BACK_SPACE && l > 0) {
				txt = txt.substring(0, l - 1);
			} else if (info.length() <= fieldLimits)
				txt += k + "";
			this.setString(txt.trim());
		}
	}

	public boolean getState() {
		return clicked;
	}

	public void paintTextField(Graphics2D G, int ticks) {
		G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		G.setStroke(new BasicStroke(3));

		transparency(3, G);
		G.setColor(Color.BLACK);
		G.fillRect(x + 3, y + 3, width, height);// shadow
		transparency(10, G);
		G.setColor(StrokeColor);
		G.drawRect(x - 1, y - 1, width + 1, height + 1);
		if (clicked)
			G.setColor(ClickColor);
		else
			G.setColor(DisplayColor);
		G.fillRect(x, y, width, height);

		// drawString
		Font FieldFont = new Font(Font.SANS_SERIF, Font.BOLD, height - 2);
		G.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		G.setFont(FieldFont);
		FontMetrics fontMath = G.getFontMetrics(FieldFont);

		G.setColor(TextColor);
		if (ticks % 100 >= 50 && clicked)
			G.drawString(info + "|", x + width - 6 - fontMath.stringWidth(info),
					(int) (y + 2 + fontMath.getHeight() / 1.5));
		else
			G.drawString(info, x + width - 2 - fontMath.stringWidth(info), (int) (y + 2 + fontMath.getHeight() / 1.5));

	}

	public void paintTextButton(Graphics2D G) {
		G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		G.setStroke(new BasicStroke(3));
		Font FieldFont = new Font(Font.SANS_SERIF, Font.BOLD, height - 2);
		FontMetrics fontMath = G.getFontMetrics(FieldFont);
		width=fontMath.stringWidth(info)+5;

		transparency(3, G);
		G.setColor(Color.BLACK);
		G.fillRect(x + 3, y + 3, width, height);// shadow
		transparency(10, G);
		G.setColor(StrokeColor);
		G.drawRect(x - 1, y - 1, width + 1, height + 1);
		if (clicked)
			G.setColor(ClickColor);
		else
			G.setColor(DisplayColor);
		G.fillRect(x, y, width, height);

		// drawString
		
		G.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		G.setFont(FieldFont);

		G.setColor(TextColor);
			G.drawString(info, x +4 , (int) (y + 2 + fontMath.getHeight() / 1.5)+2);

	}

	public void transparency(float i, Graphics g) {
		float alpha = i * 0.1f;
		AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		((Graphics2D) g).setComposite(alcom);
	}
}
