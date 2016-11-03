import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tutorial implements MouseListener, ActionListener {
	static WorkingSpace wA = WorkingSpace.workingArea;
	public JFrame tutorframe;
	public tutorGraph graph;
	public int w, h;
	public int w2, h2;

	public Timer timer = new Timer(80, this);
	int ticks = 0;

	public Tutorial(int w, int h) {
		this.w = w;
		this.h = h;

		tutorframe = new JFrame("MY MIND");// TITLE OF PROGRAM
		tutorframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// tutorframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tutorframe.setUndecorated(true);
		tutorframe.setBackground(new Color(0, 0, 0, 0));// transparency
		tutorframe.setLayout(new BorderLayout());// GIVE A LAYOUT
		tutorframe.setVisible(true);// SHOW ON SCREEN

		tutorframe.setLocationRelativeTo(null);
		tutorframe.setAlwaysOnTop(true);// Alway on top

		// Add panel
		graph = new tutorGraph();
		graph.setBackground(new Color(0, 0, 0, 100));
		tutorframe.setContentPane(graph);
		tutorframe.addMouseListener(this);
		timer.start();
		wA.jframe.setAlwaysOnTop(true);
		//
		// introframe.add(graph = new introGraph(), BorderLayout.CENTER);// ADD
		// SET PROGRAM DEFAULTLY CLOSABLE BY CLICKING THE RED CROSS BUTTON ON
		// LEFT TOP CORNER
		w2 = tutorframe.getWidth();
		h2 = tutorframe.getHeight();
		graph.Preset(w, h, w2, h2);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int ex = e.getX();
		int ey = e.getY();
		if (10 <= ex && ex <= 60 && ey <= 60 && ey >= 10) {
			tutorframe.dispose();
			wA.jframe.setResizable(true);
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

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tutorframe.setAlwaysOnTop(false);
		wA.jframe.setAlwaysOnTop(true);
		// TODO Auto-generated method stub
		ticks++;
		graph.setTicks(ticks);
		graph.repaint();
	}
}

@SuppressWarnings("serial")
class tutorGraph extends JPanel {
	static WorkingSpace wA = WorkingSpace.workingArea;
	int t = 0;
	int w = 0, h = 0, w2 = 0, h2 = 0, wd = 0, hd = 0;
	int heightOfTxt = 0;

	private void Button(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		// Close button
		g2d.setColor(new Color(200, 0, 0));
		g2d.fillRect(11, 10, 49, 50);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(10, 10, 50, 50);
		g2d.drawLine(10, 10, 60, 60);
		g2d.drawLine(10, 60, 60, 10);
	}

	public void Preset(int w, int h, int w2, int h2) {
		this.w = w;
		this.h = h;
		this.w2 = w2;
		this.h2 = h2;
		wd = (int) ((w2 - w) / 2.0);
		hd = (int) ((h2 - h) / 2.0);
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		LeftPart(g);
		RightPart(g);

	}

	public void RightPart(Graphics g) {
		int sum = 0;

		if (wA.newBox) {
			displayTxt(g,  wA.TutorialFontSize, "New Box Created", 10 + wd + w, 200, 20);
			sum += heightOfTxt;
			displayTxt(g,  wA.TutorialFontSize, "Option", 10 + wd + w, 200+sum, 20);
			sum += heightOfTxt;
			displayTxt(g,  wA.TutorialFontSize, "1.Set down Box: CLICK.", 20 + wd + w, 200+sum, 20);
		}else if(wA.TypingDecision){
			sum += heightOfTxt;
			displayTxt(g,  wA.TutorialFontSize, "Type!", 20 + wd + w, 200+sum, 20);
			sum += heightOfTxt;
			displayTxt(g,  wA.TutorialFontSize, "Top: Title!",  wd + w, 200+sum, 20);
			sum += heightOfTxt;
			displayTxt(g,  wA.TutorialFontSize, "Bottom: Description!",  wd + w, 200+sum, 20);
			sum += heightOfTxt;
			displayTxt(g,  wA.TutorialFontSize, "Use Up-Down ARROW KEYS to toggle Title or Description", 20 + wd + w, 200+sum, 20);
		}else {
						
			if (wA.boxSelectedID != -1 && !wA.PanelSelected) {
				displayTxt(g, 20, "ID: " + wA.boxSelectedID + " is selected", 10 + wd + w, 200, 20);
				sum += heightOfTxt;
				displayTxt(g, 20, "ID: " + wA.boxHoverID + " is Hovered", 10 + wd + w, 200+sum, 20);
			} else if (wA.PanelSelected && !wA.MenuAppear) {
				displayTxt(g, 20, "Panel is selected", 10 + wd + w, 200, 20);
				sum += heightOfTxt;
				displayTxt(g, 20, "Option:", 5 + wd + w, 200 + sum, 20);
//				sum += heightOfTxt;
//				displayTxt(g, 20, "1: Move the panel: Hold SHIFT and drag.", 20 + wd + w, 200 + sum, 20);
				sum += heightOfTxt;
				displayTxt(g, 20, "1: Zoom: SCROLL", 20 + wd + w, 200 + sum, 20);
				sum += heightOfTxt;
				displayTxt(g, 20, "2: Reset the working panel: SPACE", 20 + wd + w, 200 + sum, 20);
				sum += heightOfTxt;
				displayTxt(g, 20, "     Note: hold SHIFT to speed up.", 20 + wd + w, 200 + sum, 20);
			} else {
				displayTxt(g, 20, "ID: " + wA.boxHoverID + " ", 10 + wd + w, 200, 20);
			}

			if (wA.boxHoverID != -1) {
				sum += heightOfTxt;
				displayTxt(g,  wA.TutorialFontSize, "Option: ", 5 + wd + w, 200 + sum, 20);
				if(wA.boxHoverID==wA.boxSelectedID){
					sum += heightOfTxt;
					displayTxt(g, wA.TutorialFontSize, "1: Create new Box: Double Click", 15 + wd + w, 200 + sum, 20);	
					//sum += heightOfTxt;
					//displayTxt(g, 20, "2: Click working Panel to cancel", 15 + wd + w, 200 + sum, 20);	
				}else{
				sum += heightOfTxt;
				displayTxt(g,  wA.TutorialFontSize, "1: Select Box: Single Click", 15 + wd + w, 200 + sum, 20);
				sum += heightOfTxt;
				displayTxt(g, wA.TutorialFontSize, "2: Move Box: Drag", 15 + wd + w, 200 + sum, 20);
				sum += heightOfTxt;
				displayTxt(g,  wA.TutorialFontSize, "3: Create new Box: Double Click", 15 + wd + w, 200 + sum, 20);
				}
			}
//			else if(!wA.PanelSelected){
//				sum += heightOfTxt;
//				displayTxt(g, 20, "Option:", 5 + wd + w, 200 + sum, 20);
//				sum += heightOfTxt;
//				displayTxt(g, 20, "Click to select the working Panel", 20 + wd + w, 200 + sum, 20);
//			}
		}

		if (wA.MenuAppear) {
			if (wA.right1menuButtonHovered[0]) {
				displayTxt(g,  wA.TutorialFontSize, "<--Text OutPut", 15 + wd + w, 750, 20);
			} else if (wA.right1menuButtonHovered[1]) {
				displayTxt(g,  wA.TutorialFontSize, "<--Save", 15 + wd + w, 750, 20);
			} else if (wA.right1menuButtonHovered[2]) {
				displayTxt(g,  wA.TutorialFontSize, "<--Load", 15 + wd + w, 750, 20);
			} else if (wA.right1menuButtonHovered[3]) {
				displayTxt(g,  wA.TutorialFontSize, "<--Setting", 15 + wd + w, 750, 20);
			}
		} else if (wA.mouseHoverCircle) {
			displayTxt(g,  wA.TutorialFontSize, "<--Menu Opening", 15 + wd + w, 750, 20);
		} else
			displayTxt(g,  wA.TutorialFontSize, "<--Menu ", 15 + wd + w, 750, 20);
	}

	public void LeftPart(Graphics g) {
		Button(g);
		drawMouseLeft(g);
		g.setColor(wA.tutorialHighlighterColor);
		int sum = 0;
		displayTxt(g, wA.TutorialFontSize, "1. Select Object: Single Click", 10, 200, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "a. Edit Box: Select, then ENTER", 30, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "b. Create Connection: ", 30, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "   (1) Click on Box", 30, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g, wA.TutorialFontSize, "   (2) hold SHIFT", 30, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "   (3) click on the other Box", 30, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "c. Create new Box: Double Click", 30, 200 + sum, 20);

		sum += heightOfTxt + 20;
		displayTxt(g,  wA.TutorialFontSize, "2. Clear Selection: Click outside of Box", 10, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "a. Zoom: Scroll", 30, 200 + sum, 20);
//		sum += heightOfTxt;
//		displayTxt(g, 20, "b. Hold Shift and Drag: Move Panel", 30, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "c. Reset Panel: SPACE", 30, 200 + sum, 20);
		sum += heightOfTxt;
		displayTxt(g,  wA.TutorialFontSize, "Note: Hold SHIFT to Speed Up", 50, 200 + sum, 20);

		sum += heightOfTxt + 20;
		displayTxt(g,  wA.TutorialFontSize, "3. Move Box: Drag", 10, 200 + sum, 20);

	}

	public void setTicks(int ticks) {
		t = ticks;
	}

	public void drawMouseLeft(Graphics g) {

		g.setColor(Color.GRAY);
		g.drawRect((int) (0.25 * wd), hd, 50, 150);
		g.drawRect((int) (0.25 * wd) + 25, hd, 25, 50);
		g.drawRect((int) (0.25 * wd), hd, 25, 50);
		if (t % 20 >= 10) {
			g.setColor(wA.tutorialHighlighterColor);
			g.drawRect((int) (0.25 * wd), hd, 25, 50);
		}

	}

	public void displayTxt(Graphics g, int h, String txt, int x, int y, int boundary) {
		if (x <= wd)
			boundary += x;
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, h);
		g.setFont(font);
		FontMetrics fontMath = g.getFontMetrics(font);

		String stxt = "";
		int hn = 0;

		for (int i = 0; i < txt.length(); i++) {
			stxt += txt.charAt(i);
			if (fontMath.stringWidth(stxt) >= (wd - boundary)) {
				hn++;
				g.drawString(stxt, x, y + hn * fontMath.getHeight());
				stxt = "";
			}
		}
		g.drawString(stxt, x, y + (hn + 1) * fontMath.getHeight());

		heightOfTxt = (hn + 1) * fontMath.getHeight();
	}
}
