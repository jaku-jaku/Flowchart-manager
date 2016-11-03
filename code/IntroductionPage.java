import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.CubicCurve2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class IntroductionPage implements MouseListener, ActionListener, MouseMotionListener {
	static WorkingSpace wA = WorkingSpace.workingArea;
	public static IntroductionPage page = new IntroductionPage();
	static Tutorial tutor;

	public JFrame introframe;
	public introGraph graph;
	public Timer timer = new Timer(10, this);
	public int aX, aY, ticks;
	public float alphaV;
	public boolean start = false, tutorial = false, exit = false, hide = false, tutorialState = false,
			introState = false;

	public IntroductionPage() {
		introframe = new JFrame("MY MIND");// TITLE OF PROGRAM
		introframe.setSize(600, 300);// DEFAULT SIZE ON SCREEN
		introframe.setLocationRelativeTo(null);
		introframe.setUndecorated(true);
		introframe.setLayout(new BorderLayout());// GIVE A LAYOUT
		introframe.setVisible(true);// SHOW ON SCREEN

		introframe.setAlwaysOnTop(true);// Alway on top
		// introframe.setBackground(new Color(0, 0, 0, 0));//transparency

		// Add panel
		graph = new introGraph();
		introframe.setContentPane(graph);
		// graph.setBackground(new Color(0, 0, 0, 0));//transparency
		alphaV = 1.0f;

		introframe.addMouseListener(this);
		introframe.addMouseMotionListener(this);

		ticks = 0;
		timer.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (start) {
			hide = true;
			wA.jframe.setResizable(true);
		}
		if (exit) {
			System.exit(1);// exit program
		}
		if (tutorial) {
			tutorialState = true;
			hide = true;
			introframe.dispose();
			tutor = new Tutorial(wA.jframe.getWidth() + 2, wA.jframe.getHeight());
		}

		if(introState&&10<=e.getX()&&e.getX()<=40&&e.getY()<=40&&10<=e.getY())
			hide=true;
	}

	// Empty Overriding
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

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		ticks++;
		graph.repaint();

		if (hide) {
			alphaV -= 0.1f;
		}
		if (alphaV <= 0) {
			alphaV = 0;
			if (hide)
				introframe.setVisible(false);

		}
		introframe.setOpacity(alphaV);
	}

	public void check(int ex, int ey) {
		if (40 <= ex && ex <= 240 && 100 <= ey && ey <= 200)
			start = true;
		else
			start = false;
		if (340 <= ex && ex <= 540 && 40 <= ey && ey <= 90)
			tutorial = true;
		else
			tutorial = false;
		if (340 <= ex && ex <= 540 && 210 <= ey && ey <= 260)
			exit = true;
		else
			exit = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		check(e.getX(), e.getY());
		if ( exit || tutorial || start)
			graph.repaint();
	}
}

@SuppressWarnings("serial")
class introGraph extends JPanel {
	IntroductionPage p;
	int n = 100;

	@Override
	public void paintComponent(Graphics g) {
		p = IntroductionPage.page;
		super.paintComponent(g);
		if (p.introState) {
			IntroDraw(g);
		} else {
			// draw(g);
			buttonAnimation(g, p.ticks);
			if (p.ticks > 200 && p.ticks <= 300) {
				n--;
			}
			if (n <= 0)
				n = 0;
			transparency(n, g);
			if (p.ticks <= 300) {
				AnimationString(g, "MIND", 40, 250, 5, 3, 20, 0, new Font(Font.SANS_SERIF, Font.BOLD, 80));
				AnimationString(g, "MAP", 340, 100, 5, 3, 20, 50, new Font(Font.SANS_SERIF, Font.BOLD, 80));
			}
		}
	}

	public void IntroDraw(Graphics g){
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
		g.drawString("MyMind", 220,60);
		g.drawRect(210, 20, 180,45);
		displayTxt(g,24,"-Welcome to MyMind!",55,80,20);
		displayTxt(g,20,"  -Transforming your ideas into reality...",95,120,20);
		displayTxt(g,15,"           (We help you organise, plan, and share!)",80,200,20);


	}
	
	public void displayTxt(Graphics g, int h, String txt, int x, int y, int boundary) {

		Font font = new Font(Font.SANS_SERIF, Font.BOLD, h);
		g.setFont(font);
		FontMetrics fontMath = g.getFontMetrics(font);

		String stxt = "";
		int hn = 0;

		for (int i = 0; i < txt.length(); i++) {
			stxt += txt.charAt(i);
			if (fontMath.stringWidth(stxt) >= (500 - boundary)) {
				hn++;
				g.drawString(stxt, x, y + hn * fontMath.getHeight());
				stxt = "";
			}
		}
		g.drawString(stxt, x, y + (hn + 1) * fontMath.getHeight());
		
		g.drawRect(10, 10, 30, 30);
		g.drawLine(12,12, 38, 38);
		g.drawLine(12,38,38,12);
	}
	
	// Calculate string width in order to animate letters with a time delay.
	// animate each letter in string output one by one
	public void AnimationString(Graphics g, String txt, int x, int y, int singlespace, int upDownTimes, int delayrate,
			int delay, Font font) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		FontMetrics fontMath = g.getFontMetrics(font);
		int breakspace = 0;
		for (int i = 0; i < txt.length(); i++) {
			if (i != 0)
				breakspace = fontMath.stringWidth(txt.substring(0, i)) + singlespace;
			AnimationLetter(txt.charAt(i) + "", g2, p.ticks - delayrate * i - delay, upDownTimes, breakspace + x, y,
					font);
		}

	}

	// Animate individual letter//
	// String letter,
	// Graphics g,
	// n = ticks in put,
	// upDownTimes = number of periods,
	// x, y position
	// Font
	public void AnimationLetter(String A, Graphics2D g, int n, int upDownTimes, int x, int y, Font font) {
		g.setColor(Color.BLACK);
		g.setColor(ColorLighter(100, n / 20));
		g.setFont(font);
		if (n >= 100)
			n = 100;
		g.drawString(A, x, (int) heightCal(n, 100 / upDownTimes) + y);
	}

	// Combination of sine and quadratic function in order to create smooth path
	// F(g())
	public double heightCal(double t, double period) {
		double h = 0, a = 1;
		a = 0.002 * (t - 100) * (t - 150);
		h = a * Math.cos((t / period * (2 * Math.PI)));
		if (a <= 0)
			h = 0;
		return h;
	}

	public void transparency(float i, Graphics g) {
		float alpha = i * 0.01f;
		AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		((Graphics2D) g).setComposite(alcom);
	}

	public int trans = 0;

	public void buttonAnimation(Graphics g, int t) {
		((Graphics2D) g).setStroke(new BasicStroke(2));

		if (t > 280) {
			if ((t - 280) % 5 == 0) {
				trans++;
			}
		}
		if (trans >= 10)
			trans = 10;

		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		transparency(trans * 10, g);

		// My mind block section to start the program
		if (p.start)
			g.setColor(ColorLighter(100, t / 40));
		else
			g.setColor(Color.BLACK);
		g.drawRect(40, 100, 200, 100);
		g.drawString("Start", 50, 130);
		g.drawString("My", 120, 160);
		g.drawString("Mind", 170, 190);

		if (p.tutorial)
			g.setColor(ColorLighter(100, t / 40));
		else
			g.setColor(Color.BLACK);
		g.drawRect(340, 40, 200, 50);
		g.drawString("Tutorial", 390, 75);
		drawCubicLine(240, 150, 340, 65, (Graphics2D) g);

		if (p.exit)
			g.setColor(ColorLighter(100, t / 40));
		else
			g.setColor(Color.BLACK);
		g.drawRect(340, 210, 200, 50);
		g.drawString("Exit", 415, 245);
		drawCubicLine(240, 150, 340, 235, (Graphics2D) g);
	}

	public void drawCubicLine(int x, int y, int x1, int y1, Graphics2D g) {
		g.setStroke(new BasicStroke(2));
		CubicCurve2D cubicLine = new CubicCurve2D.Double();
		cubicLine.setCurve(x, y, (int) ((x + x1) / 2), y, (int) ((x + x1) / 2), y1, x1, y1);
		g.draw(cubicLine);
	}

	public static Color ColorLighter(int ampp, int shiftCC) {
		int Frequency = 0;
		int center = 255 - ampp;
		int Y1 = (int) (Math.sin(Frequency + shiftCC) * ampp + center);
		// sin max=1 min=-1;begin at 0;neutral color: 128;
		// int v = (int) (Math.cos (frequency * a) * amp + center)
		// cos max=1;min=-1;begin at 1;start from 255;
		int Y2 = (int) (Math.sin(Frequency + 2 + shiftCC) * ampp + center);
		int Y3 = (int) (Math.sin(Frequency + 4 + shiftCC) * ampp + center);
		Color vvv1 = new Color(Y1, Y2, Y3);
		return (vvv1);
	}
}
