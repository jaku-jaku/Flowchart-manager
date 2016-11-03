import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class JColorChooser extends JFrame implements ActionListener {
	boolean ifClickOutside = false;
	Color selectedColor = new Color(0, 0, 0);
	int width=350,height=360;
	ColorChooserPanel panel;
	Timer t=new Timer(10,this);

	public JColorChooser(Color color, int x, int y) {
		this.setLocation(x, y);
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		this.setSize(width,height);
		 panel = new ColorChooserPanel(color, this);
		getContentPane().add(panel);
		setVisible(true);
	}
	
	public Color getFinalColor() {
		return selectedColor;
	}

	public boolean ifClosed() {
		return ifClickOutside;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		panel.repaint();
	}

}

@SuppressWarnings("serial")
class ColorChooserPanel extends JPanel implements MouseMotionListener, MouseListener {
	private final int SIZE = 300;
	private BufferedImage gradient = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_4BYTE_ABGR);
	private BufferedImage hueStrip = new BufferedImage(15, SIZE, BufferedImage.TYPE_4BYTE_ABGR);
	private Rectangle hueRect = new Rectangle(SIZE + 20, 10, 15, SIZE);
	private Rectangle gradientRect = new Rectangle(10, 10, SIZE, SIZE);
	private float hue;
	private Color selectedColor;
	private float selectedSaturation;
	private float selectedBrightness;
	private JColorChooser jframe;

	public ColorChooserPanel(Color OldColor, JColorChooser frame) {
		jframe = frame;
		selectedColor = OldColor;
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		generateHueStrip();
		float[] temphsb = Color.RGBtoHSB(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue(),
				null);
		hue = temphsb[0];
		selectedSaturation = temphsb[1];
		selectedBrightness = temphsb[2];
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		generateGradient();
		g2d.drawImage(gradient, null, 10, 10);
		g2d.drawImage(hueStrip, null, SIZE + 20, 10);
		g2d.setColor(selectedColor);
		g2d.fillRect(10, SIZE + 20, 30, 30);
		hueSelectedPointer(g2d);
		brightnessSelectedPointer(g2d);
		saturationSelectedPointer(g2d);
	}

	private void hueSelectedPointer(Graphics2D G) {
		Polygon triangle = new Polygon();
		triangle.addPoint(SIZE + 20 + 15 + 2, (int) (hue * SIZE) + 10);
		triangle.addPoint(SIZE + 20 + 15 + 8 + 2, (int) (hue * SIZE) + 5);
		triangle.addPoint(SIZE + 20 + 15 + 8 + 2, (int) (hue * SIZE) + 15);
		G.setColor(Color.getHSBColor(hue, 1.0f, 1.0f));
		G.fill(triangle);
		G.setColor(Color.BLACK);
		G.draw(triangle);
	}

	private void brightnessSelectedPointer(Graphics2D G) {
		Polygon triangle = new Polygon();
		triangle.addPoint((int) (selectedBrightness * SIZE + 10), SIZE + 10);
		triangle.addPoint((int) (selectedBrightness * SIZE + 5), SIZE + 18);
		triangle.addPoint((int) (selectedBrightness * SIZE + 15), SIZE + 18);
		G.setColor(Color.getHSBColor(0.0f, 0.0f, selectedBrightness));
		G.fill(triangle);
		G.setColor(Color.BLACK);
		G.draw(triangle);
	}

	private void saturationSelectedPointer(Graphics2D G) {
		Polygon triangle = new Polygon();
		triangle.addPoint(10, (int) (selectedSaturation * SIZE + 10));
		triangle.addPoint(2, (int) (selectedSaturation * SIZE + 5));
		triangle.addPoint(2, (int) (selectedSaturation * SIZE + 15));
		G.setColor(Color.getHSBColor(hue, 1 - selectedSaturation, 1.0f));
		G.fill(triangle);
		G.setColor(Color.BLACK);
		G.draw(triangle);
	}

	private void generateHueStrip() {
		for (int i = 0; i < SIZE; i++) {
			Color hsb = Color.getHSBColor((float) (i / (double) SIZE), (float) 1.0, (float) 1);
			for (int k = 0; k < 15; k++)
				hueStrip.setRGB(k, i, hsb.getRGB());
		}
	}

	private void generateGradient() {
		for (int saturation = SIZE - 1; saturation >= 0; saturation--) {
			for (int value = SIZE - 1; value >= 0; value--) {
				Color hsb = Color.getHSBColor(hue, (float) saturation / SIZE, (float) value / SIZE);// Floating
																									// HSB
																									// color

				gradient.setRGB(value, SIZE - 1 - saturation, hsb.getRGB());
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (hueRect.contains(x, y)) {
			x -= SIZE + 20;
			y -= 10;
			Color rgb = new Color(hueStrip.getRGB(x, y));
			int red = rgb.getRed();
			int blue = rgb.getBlue();
			int green = rgb.getGreen();
			hue = Color.RGBtoHSB(red, green, blue, null)[0];
			selectedColor=new Color(Color.HSBtoRGB(hue, selectedSaturation, selectedBrightness));
		} else if (gradientRect.contains(x, y)) {
			selectedColor = new Color(gradient.getRGB(x - 10, y - 10));
			selectedBrightness = (float) (x - 10) / (float) SIZE;
			selectedSaturation = (float) (y - 10) / (float) SIZE;
		} else {
			jframe.selectedColor = selectedColor;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
			jframe.ifClickOutside = true;
			jframe.selectedColor = selectedColor;
			System.out.println(jframe.ifClickOutside);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseClicked(e);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}