
/*
 * ICS 4U
 * Culminating
 * Developer: Jack(Jianxiang) & Oswaldo
 * Last Edit: January 27/2016
 * ---------
 * Current Class: RenderPanel
 * Function: Display or give a visual feedback to the user, based on the data in WorkingSpace
 */

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

//This class inherit all the characteristic of JPanel
//And by @Overriding in order to change the action of this class and make it unique
@SuppressWarnings("serial")
public class RenderPanel extends JPanel {
	/*******************************
	 ***** Double Buffer Objects *****
	 *******************************
	 *********************************
	 * Creates an off-screen editable image, Then, Display the previous off
	 * screen image on the screen While showing the previous image, system at
	 * background is reworking the image and paint on the next image Refresh
	 * until heard repaint commands
	 * 
	 * ///Also These images reacts like/// ///Panels, so we can switch images
	 * instead of panels//
	 ********************************/

	// The main one for displaying boxes Here image has function of:
	// 1.Double buffering
	// 2.Zoom in and out function (by resizing the image)
	// 3.Double layers of graphics
	Image offscreen;
	Graphics2D bufferGraphics;

	// This is the double buffer place for Indent Text out put
	Image offscreenText;
	Graphics2D textOutGraphics;

	// This is the image for the setting panel
	Image offscreenSetting;
	Graphics2D settingGraphics;

	// This is the the workingSpace object
	WorkingSpace ws;

	/*******************************
	 ***** Hard Coded Temporary VARIABLES *****
	 *******************************/
	// Following are the only hard coded
	// values for the typing panel
	int lengthString = 0;
	int FontTitleH = 30;// Default Font height of title
	int FontBodyH = 30;// Default Font height of description
	int rowMax = 1, crow = 1;// Row counters, for automatic smartly draw string
								// on next line
	boolean change = false;// Boolean state the decrement of the size of the
							// font

	// _________________________________________________________________//
	/****************************************************************
	 * ******Paint Component (Overriding the jpanel's repaint) ************
	 * ***********In order to refresh and display on the screen while calling
	 * repaint method
	 ****************************************************************/
	@Override
	protected void paintComponent(Graphics g) {

		// Here we are not calling working space to create a new object of
		// working space
		// Instead, we are grabbing the identity of static workingArea created
		// inside the workingSpace, and renamed as 'ws' as short name of the
		// workingArea.
		// Though they are named differently, they are still same person,
		// As a result, here we can access and edit all the public values
		// directly from this class to the workingSpace
		ws = WorkingSpace.workingArea;

		// Normal screen while not at setting Panel and TextOutPut Mode
		if (!ws.right1menuButtonClicked[0] && !ws.right1menuButtonClicked[3]) {

			// Create images
			offscreen = createImage((int) (ws.jframe.getWidth() * ws.PANELRATIO),
					(int) (ws.jframe.getHeight() * ws.PANELRATIO));
			// Make Image editable and apply graphics on the image
			bufferGraphics = (Graphics2D) offscreen.getGraphics();

			/*******************************
			 *** Painting Graphics Below ***
			 *******************************/

			// MAKE GRAPHICS SMOOTH, Graphics rendering
			bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw All BOX OBJECTS BELOW
			drawAllBoxes();

			// Draw Connection - NESTED FOR LOOP
			for (int i = 0; i < ws.currentBoxCreated; i++) {
				for (int a = 0; a < ws.boxList.getNode(i).getList().length; a++) {
					bufferGraphics.setColor(ws.StringColor);
					// Smart Connection - where it automatically apply the
					// orientation
					// See details of codes in method
					drawConnection(ws.boxList.getNode(i), ws.boxList.getNode(ws.boxList.getNode(i).getList()[a]));
				}
			}

			// High light the selected box
			if (ws.PanelSelected) {
				bufferGraphics.setColor(ws.FrameHighlighterColor);
				bufferGraphics.drawRect(0, 0, ws.jframe.getWidth() - 1, ws.jframe.getHeight() - 1);
			}
		}
		// When under TextOutPut mode
		// shows indented graph from objects graph
		else if (ws.right1menuButtonClicked[0]) {
			// Text output image set up
			offscreenText = createImage(ws.jframe.getWidth(), ws.jframe.getHeight());
			textOutGraphics = (Graphics2D) offscreenText.getGraphics();

			// draw All graphics on the textOutGraphics - See Detail in the
			// method
			paintTextOutPutGraphics(textOutGraphics);
		}
		// When under Setting mode
		// Where User can change size of boxes and fonts and colors
		else if (ws.right1menuButtonClicked[3]) {
			// Set Up the Setting Panel
			offscreenSetting = createImage(ws.jframe.getWidth(), ws.jframe.getHeight());
			settingGraphics = (Graphics2D) offscreenSetting.getGraphics();

			// draw Everything from the method to this graphics
			paintSettingGraphics(settingGraphics);
		}

		// ------------Display images on the screen graphics ------------//
		// Only happen when system are not under setting nor textOutPut Mode
		if (!ws.right1menuButtonClicked[0] && !ws.right1menuButtonClicked[3]) {
			// Here -----Resizing the image----- By the zoomPercentage adjusted
			// by the scrolling mouse wheel
			// Resize the image and print after resized image on the screen
			g.drawImage(ResizedImage(offscreen, ws.jframe.getWidth(), ws.jframe.getHeight(),
					(int) (ws.zoomPercentage * ws.jframe.getWidth() * ws.PANELRATIO),
					(int) (ws.zoomPercentage * ws.jframe.getHeight() * ws.PANELRATIO)), 0, 0, this);
		} else if (ws.right1menuButtonClicked[0]) {
			// Draw Text output on screen
			g.drawImage(offscreenText, 0, 0, this);
		} else if (ws.right1menuButtonClicked[3]) {
			// draw setting panel in screen
			g.drawImage(offscreenSetting, 0, 0, this);
		}

		// Pops Up the Saving Panel Graphics on the screen graphics
		if (ws.right1menuButtonClicked[1]) {
			SavePanel((Graphics2D) g);
		}

		// -------Graphics Rendering--------//
		// Render smoothly
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// -------Popping-up & Static MENU Circle--------//
		// Menu popping up
		transparency(4, g);
		for (double i = ws.NumberOfCircle - 1; i >= 0; i--) {
			DrawMenu(1 * (i / 4), g);
		}
		DrawX(5, ws.StringColor, ws.arcs, g);

		// -------MENU HOVER HINTS--------//
		// menu control shown
		for (int i = 0; i < ws.right1menuButtonHovered.length; i++) {
			if (ws.right1menuButtonHovered[i]) {
				g.setColor(ws.StringColor);
				g.drawOval(ws.MasterCircleX - (ws.gapOfCircles + ws.CircleSize) * (i + 1) - 2, ws.MasterCircleY - 2,
						ws.CircleSize + 4, ws.CircleSize + 4);
			}
		}

		// -------MENU ICONS--------//
		/// Menu ICONS
		if (ws.timerRun[1]) {
			textOutPutButtonGraphics((Graphics2D) g, ws.MasterCircleX - (int) (ws.xc * ((1) / 4.0)), ws.MasterCircleY,
					ws.lineNum);
			saveButtonGraphics((Graphics2D) g, ws.MasterCircleX - (int) (ws.xc * ((2) / 4.0)), ws.MasterCircleY,
					ws.CircleSize, ws.arrowDown);
			loadButtonGraphics((Graphics2D) g, ws.MasterCircleX - (int) (ws.xc * ((3) / 4.0)), ws.MasterCircleY,
					ws.CircleSize, ws.arrowUp);
			settingButtonGraphics((Graphics2D) g, ws.MasterCircleX - (int) (ws.xc * ((4) / 4.0)), ws.MasterCircleY,
					ws.CircleSize, ws.rotationAnimation);
		}

		// -------DEBUGS String / WATERMARK STRING--------//
		// String Debugging
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g.drawString("MIND-MAP  " + " Panel: (" + ws.PanelX + ", " + ws.PanelY + ")", 40, 60);

		// -------NORMAL MODE--------//
		// The main stage of displaying boxes and editing
		if (!ws.right1menuButtonClicked[0] && !ws.right1menuButtonClicked[3] && !ws.right1menuButtonClicked[1]
				&& !ws.right1menuButtonClicked[2]) {
			// Double enter and draw the panel animation on the screen
			if (ws.DoubleEnterOrMore) {
				// graphics of text
				TextPanel(g);
			} else {
				// ----- Hover Hint BELOW -----
				// Only shows Up if the mouse staying in the area for a certain
				// time
				if (ws.hoverRemainingTicks == ws.hoverTicksTime && ws.mouseHoverBoxWithPanelID != -1
						&& ws.timerRun[3]) {
					// body=DESCRIPTION from the box where mouse is hovering on
					String body = ws.boxList.getNode(ws.mouseHoverBoxWithPanelID).getBody();
					Font BodyFont = new Font(Font.SANS_SERIF, Font.PLAIN, ws.hoverStringFontH);
					// While there is no description and appear the hints
					// of enter description
					if (body.trim() == "") {
						body = "[Please Enter Description]";
						BodyFont = new Font(Font.SANS_SERIF, Font.ITALIC, ws.hoverStringFontH);
					}

					// FontMath to calculate the string width
					g.setFont(BodyFont);
					FontMetrics math = g.getFontMetrics(BodyFont);

					// -------AUTO-CORRECTION / AUTO ADJUST -------//
					// Try to calculate the best width for the best display
					int width = math.stringWidth(body), lines = 1;
					while (width > ws.hoverPanelMaxWidth) {
						width = width / 2;
						lines = lines * 2;
					}

					// ----- Try to draw out the string-----//
					// Current line of the string
					String lineS = "";
					// counting the lines to adjust where to display each line
					int count = 0;
					g.setColor(ws.HoverPanelString);
					for (int i = 0; i < body.length(); i++) {
						lineS += body.charAt(i);
						if (math.stringWidth(lineS) >= width || i == body.length() - 1) {
							g.setFont(BodyFont);
							count++;
							// Distance between edges and lines are adjusted by
							// the distance of "-"
							g.drawString(lineS, ws.mouseX + 2 + math.stringWidth("_"),
									(int) (ws.mouseY + ws.hoverStringFontH + math.stringWidth("_") * 2
											+ math.getHeight() / 1.5 * count));
							lineS = "";// clear and check next line
						}
					}
					// Panel background for the havering panel
					transparency(4, g);// transparency marked by 0 (transparency
										// - 0.0f) to 10 (full color displayed -
										// 1.0f)
					// Panel background
					g.setColor(ws.HoverPanelColor);
					g.fillRect(ws.mouseX + 2, ws.mouseY + 20, width + math.stringWidth("_") * 2,
							(int) (+math.stringWidth("_") * 3 + math.getHeight() / 1.5 * count));
					// Panel Stroke (Wrapping Rectangle)
					g.setColor(ws.HoverPanelStroke);
					g.drawRect(ws.mouseX + 2, ws.mouseY + 20, width + math.stringWidth("_") * 2,
							(int) (+math.stringWidth("_") * 3 + math.getHeight() / 1.5 * count));
					// Reset the transparency back to normal
					transparency(10, g);// transparency marked by 0
										// (transparency - 0.0f) to 10 (full
										// color displayed - 1.0f)
				}
			}
		}

		// Scrolling for selection in loading mode
		// This code contains cropped image, so put to the last order
		if (ws.right1menuButtonClicked[2]) {
			OpenPanel((Graphics2D) g);
		}

	}

	// _________________________________________________________________//
	/****************************************************************
	 * ****** Methods Holding detailed graphics of each mode ************
	 * *********
	 ****************************************************************/
	//Panel where user can read files saved before
	private void OpenPanel(Graphics2D G) {
		int width = 400, height = 160, x = (ws.jframe.getWidth() - width) / 2, y = (ws.jframe.getHeight() - height) / 2;

		G.setColor(ws.BoxColorWhite);
		G.fillRect(x, y, width, height);
		G.setColor(ws.StringColor);
		G.drawRect(x, y, width, height);

		Font FieldFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
		G.setFont(FieldFont);
		G.drawString("OPEN THE FILE", x + 90, y + 40);
		G.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		G.drawString("Scroll & Select & Confirm", x + 110, y + 60);

		//button class have its own paint class 
		ws.buttonCancel.paintTextButton(G);
		ws.buttonConfirm.paintTextButton(G);

		//File selector class have its own paint class
		ws.readFilesSelector.setPosition(x + 70, y + 70);
		ws.readFilesSelector.paintSlidingSelector(G);
	}

	//Interface for user to save files
	//Pop Up Over-writing warning by
	//checking through the log_cache
	private void SavePanel(Graphics2D G) {
		int width = 400, height = 140, x = (ws.jframe.getWidth() - width) / 2, y = (ws.jframe.getHeight() - height) / 2;
		G.setColor(ws.BoxColorWhite);
		G.fillRect(x, y, width, height);
		G.setColor(ws.StringColor);
		G.drawRect(x, y, width, height);
		ws.text1.paintTextButton(G);
		ws.text2.paintTextButton(G);
		ws.switchFile.drawSwitch(G);
		ws.switchImage.drawSwitch(G);
		ws.text3.paintTextButton(G);
		ws.text4.paintTextButton(G);
		ws.nameOfTheFile.paintTextField(G, ws.ticks);
		ws.rootOfTheFile.paintTextField(G, 0);
		ws.buttonCancel.paintTextButton(G);
		ws.buttonConfirm.paintTextButton(G);
		if (ws.OverwritingWarning) {
			G.setColor(ws.BoxColorWhite);
			G.fillRect(x - 5, y + 10, width + 10, height - 20);
			G.setColor(ws.StringColor);
			G.drawRect(x - 5, y + 10, width + 10, height - 20);
			Font FieldFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
			G.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			G.setFont(FieldFont);
			G.drawString("WARNING", x + 130, y + 50);
			G.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
			G.drawString("Are you sure to over-write the old saving?", x + 30, y + 100);
		}
	}

	//Setting Panel
	private void paintSettingGraphics(Graphics2D G) {
		G.setStroke(new BasicStroke(3));
		G.setColor(ws.BoxColorWhite);
		G.drawRect(20, 20, ws.jframe.getWidth() - 40, ws.jframe.getHeight() - 40 - 30);

		ws.tfBoxWidth.paintTextButton(G);
		ws.tfBoxHeight.paintTextButton(G);
		//TextField WIth ticks can enter values
		ws.tfGetBoxWidth.paintTextField(G, ws.ticks);
		ws.tfGetBoxHeight.paintTextField(G, ws.ticks);

		ws.colorOfBoxDisplay.paintTextButton(G);
		ws.colorOfBoxPanelDisplay.paintTextButton(G);
		ws.colorOfBoxStrokeDisplay.paintTextButton(G);

		ws.colorOfFrameDisplay.paintTextButton(G);
		ws.colorOfFrameHighlighterDisplay.paintTextButton(G);
		ws.colorOfFrameBackgroundDisplay.paintTextButton(G);

		ws.colorOfHoverHintDisplay.paintTextButton(G);
		ws.colorOfHoverHintPanelDisplay.paintTextButton(G);
		ws.colorOfHoverHintStrokeDisplay.paintTextButton(G);
		ws.colorOfHoverHintStringDisplay.paintTextButton(G);
		ws.colorOfTutorialStringDisplay.paintTextButton(G);

		ws.colorOfBoxPanel.paintMySelf(G);
		ws.colorOfBoxStroke.paintMySelf(G);
		ws.colorOfFrameBackground.paintMySelf(G);
		ws.colorOfFrameHighlighter.paintMySelf(G);
		ws.colorOfHoverHintPanel.paintMySelf(G);
		ws.colorOfHoverHintString.paintMySelf(G);
		ws.colorOfHoverHintStroke.paintMySelf(G);
		ws.colorOfTutorialString.paintMySelf(G);

		ws.slTutor.paintSlider(G);
		ws.slTitle.paintSlider(G);
		ws.TutorialFontSizeDisplay.paintTextButton(G);
		ws.TutorialFontSizeNumDisplay.paintTextButton(G);
		ws.BoxTitleFontSizeDisplay.paintTextButton(G);
		ws.BoxTitleFontSizeNumDisplay.paintTextButton(G);

	}

	//Indent text out put
	private void paintTextOutPutGraphics(Graphics2D g2) {

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		for (int i = 0; i < ws.textOutput.lineList.getLength(); i++) {
			// The Line object it is going to draw
			Line l = ws.textOutput.lineList.getNode(i);

			//// Draws box////
			g2.setColor(ws.BoxColorWhite);
			g2.fillRect(l.getX(), l.getY() + ws.scrollYTextOutput, l.getLineWidth(), l.getLineHeight());
			g2.setColor(ws.StringColor);
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(l.getX(), l.getY() + ws.scrollYTextOutput, l.getLineWidth(), l.getLineHeight());
			Font lines = new Font(Font.SANS_SERIF, Font.ITALIC, 22);
			FontMetrics fm = g2.getFontMetrics(lines);
			g2.setFont(lines);
			g2.drawString(l.getTitle(), l.getX() + 50,
					l.getY() + (l.getLineHeight() / 2 + Math.round(fm.getHeight() / 3)) + ws.scrollYTextOutput);
		}

	}

	// _________________________________________________________________//
	/****************************************************************
	 * ** Menu Graphics + Methods Holding each graphics of buttons appeared on
	 * the menuBar* Graphics are all made by functions and mathematically There
	 * are no image imports in this program All button graphics are animated by
	 * the timer values in WorkingSpace
	 ****************************************************************/
	// Rotation based on the sine function and cosine function
	public void DrawX(int indent, Color color, double arc, Graphics bufferGraphics1) {
		bufferGraphics1.setColor(color);
		((Graphics2D) bufferGraphics1).setStroke(new BasicStroke(3));
		((Graphics2D) bufferGraphics1).draw(new Line2D.Double((ws.MasterCircleX + indent + (20 - Math.cos(arc) * 20)),
				(ws.MasterCircleY + ws.CircleSize / 2 + Math.sin(arc) * 20),
				(ws.MasterCircleX + ws.CircleSize - indent - (20 - Math.cos(arc) * 20)),
				(ws.MasterCircleY + ws.CircleSize / 2 - Math.sin(arc) * 20)));
		((Graphics2D) bufferGraphics1)
				.draw(new Line2D.Double((ws.MasterCircleX + indent + (20 - Math.cos(arc + Math.PI / 2) * 20)),
						(ws.MasterCircleY + ws.CircleSize / 2 + Math.sin(arc + Math.PI / 2) * 20),
						(ws.MasterCircleX + ws.CircleSize - indent - (20 - Math.cos(arc + Math.PI / 2) * 20)),
						(ws.MasterCircleY + ws.CircleSize / 2 - Math.sin(arc + Math.PI / 2) * 20)));
	}

	// Make menu appear at the same time but with different speed
	public void DrawMenu(double speed, Graphics bufferGraphics1) {
		transparency(8, bufferGraphics1);
		bufferGraphics1.setColor(Color.WHITE);
		((Graphics2D) bufferGraphics1).setStroke(new BasicStroke(3));
		bufferGraphics1.fillOval(ws.MasterCircleX - (int) (ws.xc * speed), ws.MasterCircleY, ws.CircleSize,
				ws.CircleSize);
		bufferGraphics1.setColor(ws.StringColor);
		bufferGraphics1.drawOval(ws.MasterCircleX - (int) (ws.xc * speed), ws.MasterCircleY, ws.CircleSize,
				ws.CircleSize);
	}

	// text out put button graphics
	public void textOutPutButtonGraphics(Graphics2D g, int x, int y, int num) {
		g.fillOval(x + 7 + 2, y + 5 * 3 - 2, 5, 5);
		g.fillRect(x + 14 + 2, y + 5 * 3 - 2, 20, 5);
		if (num >= 2) {
			g.fillOval(x + 10 + 1, y + 5 * 5 - 2, 5, 5);
			g.fillRect(x + 17 + 1, y + 5 * 5 - 2, 20, 5);
		}
		if (num >= 3) {
			g.fillOval(x + 13, y + 5 * 7 - 2, 5, 5);
			g.fillRect(x + 20, y + 5 * 7 - 2, 20, 5);
		}
	}

	// save button graphics
	public void saveButtonGraphics(Graphics2D g, int x, int y, int size, int animation) {
		animation = animation / 30;
		Polygon Arrow = new Polygon();
		int shift = (size - 16) / 2;
		Arrow.addPoint(x + 5 + shift, y + 10 + animation);
		Arrow.addPoint(x + 11 + shift, y + 10 + animation);
		Arrow.addPoint(x + 11 + shift, y + 22 + animation);
		Arrow.addPoint(x + 16 + shift, y + 22 + animation);
		Arrow.addPoint(x + 8 + shift, y + 32 + animation);
		Arrow.addPoint(x + 0 + shift, y + 22 + animation);
		Arrow.addPoint(x + 5 + shift, y + 22 + animation);
		g.fill(Arrow);
		Polygon Ushape = new Polygon();
		Ushape.addPoint(x + 10, y + 30);
		Ushape.addPoint(x + 10, y + 40);
		Ushape.addPoint(x + size - 10, y + 40);
		Ushape.addPoint(x + size - 10, y + 30);
		Ushape.addPoint(x + size - 15, y + 30);
		Ushape.addPoint(x + size - 15, y + 35);
		Ushape.addPoint(x + 15, y + 35);
		Ushape.addPoint(x + 15, y + 30);
		g.fill(Ushape);
	}

	// Load Button Graphics
	public void loadButtonGraphics(Graphics2D g, int x, int y, int size, int animation) {
		animation = animation / 30;
		Polygon Arrow = new Polygon();
		int shift = (size - 16) / 2;
		Arrow.addPoint(x + 11 + shift, y + 32 + animation);
		Arrow.addPoint(x + 11 + shift, y + 25 + animation);
		Arrow.addPoint(x + 16 + shift, y + 25 + animation);
		Arrow.addPoint(x + 8 + shift, y + 10 + animation);
		Arrow.addPoint(x + 0 + shift, y + 25 + animation);
		Arrow.addPoint(x + 5 + shift, y + 25 + animation);
		Arrow.addPoint(x + 5 + shift, y + 32 + animation);
		g.fill(Arrow);
		Polygon Ushape = new Polygon();
		Ushape.addPoint(x + 10, y + 30);
		Ushape.addPoint(x + 10, y + 40);
		Ushape.addPoint(x + size - 10, y + 40);
		Ushape.addPoint(x + size - 10, y + 30);
		Ushape.addPoint(x + size - 15, y + 30);
		Ushape.addPoint(x + size - 15, y + 35);
		Ushape.addPoint(x + 15, y + 35);
		Ushape.addPoint(x + 15, y + 30);
		g.fill(Ushape);
	}

	// Setting buttons are based on the circular function
	// and sine+cosine functions in order to make perfect
	// 2D path of a gear symbol
	// Rotation are made by Affine transform
	public void settingButtonGraphics(Graphics2D g, int x, int y, int size, double rotate) {
		double angle = 0, r = 20, outerR = 22;
		int centreX = x + size / 2, centreY = y + size / 2, rr = 20;
		double[] yy = new double[8], xx = new double[8];
		// Outer circle
		angle = Math.toRadians((60 - outerR) / 2);
		xx[0] = Math.cos(angle) * r;
		yy[0] = Math.sin(angle) * r;
		angle = Math.toRadians((60 - outerR) / 2 + outerR);
		xx[1] = Math.cos(angle) * r;
		yy[1] = Math.sin(angle) * r;
		angle = Math.toRadians((60 - outerR) / 2 + outerR + (60 - outerR));
		xx[2] = Math.cos(angle) * r;
		yy[2] = Math.sin(angle) * r;
		angle = Math.toRadians(90);
		xx[3] = Math.cos(angle) * r;
		yy[3] = Math.sin(angle) * r;
		// Inner Circle
		r = 15;
		angle = Math.toRadians(0);
		xx[4] = Math.cos(angle) * r;
		yy[4] = Math.sin(angle) * r;
		angle = Math.toRadians(12.5);
		xx[5] = Math.cos(angle) * r;
		yy[5] = Math.sin(angle) * r;
		angle = Math.toRadians(47.5);
		xx[6] = Math.cos(angle) * r;
		yy[6] = Math.sin(angle) * r;
		angle = Math.toRadians(72.5);
		xx[7] = Math.cos(angle) * r;
		yy[7] = Math.sin(angle) * r;

		// Path2D are similar to Polygon but can be assigned by values of double
		// and float
		Path2D gear = new Path2D.Float();
		double X[] = { xx[5], xx[0], xx[1], xx[6], xx[7], xx[2], -xx[2], -xx[7], -xx[6], -xx[1], -xx[0], -xx[5], -xx[5],
				-xx[0], -xx[1], -xx[6], -xx[7], -xx[2], xx[2], xx[7], xx[6], xx[1], xx[0], xx[5] };
		double Y[] = { -yy[5], -yy[0], -yy[1], -yy[6], -yy[7], -yy[2], -yy[2], -yy[7], -yy[6], -yy[1], -yy[0], -yy[5],
				yy[5], yy[0], yy[1], yy[6], yy[7], yy[2], yy[2], yy[7], yy[6], yy[1], yy[0], yy[5] };

		gear.moveTo(X[0] + centreX, Y[0] + centreY);
		for (int i = 1; i < X.length; i++) {
			gear.lineTo(X[i] + centreX, Y[i] + centreY);
		}
		gear.closePath();

		// Affine transform in order to
		// rotate the gear path
		AffineTransform op = new AffineTransform();
		op.rotate(Math.toRadians(rotate), centreX, centreY);
		Shape transformed = op.createTransformedShape(gear);

		g.draw(transformed);

		g.drawOval(centreX - rr / 2, centreY - rr / 2, rr, rr);
	}

	// _________________________________________________________________//
	/****************************************************************
	 * ****** Typing Panel Graphics************ All the values assigned below
	 * are specifically adjusted in workingArea in order to create a smooth
	 * animation
	 ****************************************************************/

	public void TextPanel(Graphics g) {

		// Gradually shows up from 0.0f to 0.7f
		transparency(ws.transparencyOfTextPanel, g);

		((Graphics2D) g).setStroke(new BasicStroke(3));// line thickness

		// Paint the typing panel
		// Values are animated in workingArea
		// increasing size of typing panel based on the timer
		g.setColor(ws.BoxColorWhite);
		g.fillRoundRect(ws.TextPanelMoveX, ws.TextPanelMoveY, ws.TextPanelWidth, ws.TextPanelHeight, ws.arcTextPanel,
				ws.arcTextPanel);
		g.setColor(ws.StringColor);
		g.drawRoundRect(ws.TextPanelMoveX, ws.TextPanelMoveY, ws.TextPanelWidth, ws.TextPanelHeight, ws.arcTextPanel,
				ws.arcTextPanel);

		((Graphics2D) g).setStroke(new BasicStroke(1));// line thickness
		// Dash stroke
		float dash1[] = { 10.0f };
		BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);// dash
		((Graphics2D) g).setStroke(dashed);
		// Draw the Dash outline
		g.setColor(ws.PanelLineColor);
		int infoH = ws.TextPanelHeight - ws.indent * 3 - ws.MaxTitleHeight;
		int infoW = ws.TextPanelWidth - ws.indent * 2;
		int infoX = ws.TextPanelMoveX + ws.indent;
		int infoY = ws.TextPanelMoveY + ws.indent * 2 + ws.TitleHeight;
		g.drawRoundRect(ws.TextPanelMoveX + ws.indent, ws.TextPanelMoveY + ws.indent, ws.TextPanelWidth - ws.indent * 2,
				ws.TitleHeight, ws.arcTextPanel, ws.arcTextPanel);
		g.drawRoundRect(infoX, infoY, infoW, infoH, ws.arcTextPanel, ws.arcTextPanel);

		// After increments and all other animation
		// typing panel will be finally editable
		if (ws.TextPanelHeight == ws.MaxTextPanelHeight) {

			// Cancel and confirm buttons
			g.setColor(ws.BoxColorWhite);
			g.fillRoundRect(ws.TextPanelX + ws.indent, ws.TextPanelMoveY + ws.MaxTextPanelHeight + ws.indent, 60, 20,
					ws.arcTextPanel, ws.arcTextPanel);
			g.fillRoundRect(ws.TextPanelX - ws.indent - 60 + ws.MaxTextPanelWidth,
					ws.TextPanelMoveY + ws.MaxTextPanelHeight + ws.indent, 60, 20, ws.arcTextPanel, ws.arcTextPanel);

			g.setColor(ws.StringColor);
			((Graphics2D) g).setStroke(new BasicStroke(3));
			g.drawRoundRect(ws.TextPanelX + ws.indent, ws.TextPanelMoveY + ws.MaxTextPanelHeight + ws.indent, 60, 20,
					ws.arcTextPanel, ws.arcTextPanel);
			g.drawRoundRect(ws.TextPanelX - ws.indent - 60 + ws.MaxTextPanelWidth,
					ws.TextPanelMoveY + ws.MaxTextPanelHeight + ws.indent, 60, 20, ws.arcTextPanel, ws.arcTextPanel);

			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
			g.drawString("Cancel", ws.TextPanelX + ws.indent + 5,
					ws.TextPanelMoveY + ws.MaxTextPanelHeight + ws.indent + 15);
			g.drawString("Confirm", ws.TextPanelX - ws.indent - 60 + ws.MaxTextPanelWidth + 2,
					ws.TextPanelMoveY + ws.MaxTextPanelHeight + ws.indent + 15);

			// Typing area
			// With Animation of cursor blinking
			// by simply adding '|' to specific points
			// and shows up based on the timer ticks
			if (ws.TypingDecision) {
				Font TitleFont = new Font(Font.SANS_SERIF, Font.BOLD, FontTitleH);
				String sen0 = ws.boxList.getNode(ws.boxSelectedID).getTitle().toUpperCase();
				String sen = "";
				g.setFont(TitleFont);
				FontMetrics fontMath = g.getFontMetrics(TitleFont);

				// Calculate difference
				// in order to make text only animated to one side
				int widthDifference = fontMath.charWidth('|') - fontMath.charWidth(' ');

				// -------Cursor blinking animation-----
				if (!ws.bodyTyping) {
					sen = sen0.substring(0, sen0.length() - ws.cursourTrim) + "|"
							+ sen0.substring(sen0.length() - ws.cursourTrim);

					if (ws.CursourBlink)
						sen = sen0.substring(0, sen0.length() - ws.cursourTrim) + " "
								+ sen0.substring(sen0.length() - ws.cursourTrim);
				} else {
					sen = sen0;
				}

				// Set boundary for the text
				Rectangle2D boundary = fontMath.getStringBounds(sen, g);

				boundary.setRect(0, 0, boundary.getWidth(), fontMath.getHeight() / 1.5);
				if (ws.CursourBlink && !ws.bodyTyping)
					boundary.setRect(0, 0, boundary.getWidth() + widthDifference, fontMath.getHeight() / 1.5);

				// ------Print string----
				g.drawString(sen, (int) ((ws.MaxTextPanelWidth - boundary.getWidth()) / 2 + ws.TextPanelX),
						(int) ((ws.MaxTitleHeight + boundary.getHeight()) / 2.0 + ws.TextPanelY + ws.indent));

				// -----Automatically Resize the Font-----//
				if (boundary.getWidth() >= (infoW - boundary.getWidth() / sen.length())) {
					if (FontTitleH == 10) {
						ws.titleTextFull = true;
					} else {
						FontTitleH -= 2;
						change = true;
					}
				}
				// when there were decrement of font
				// delete one line, will make text resized larger
				// until the text reaches the max font size
				else if (ws.deleteChar && change) {
					ws.titleTextFull = false;
					ws.deleteChar = false;
					FontTitleH += 2;
					if (FontTitleH >= 30)
						change = false;
				}
				// Font size auto adjusting End___
				// _____________________________//

				// -------Description PART-----//
				// While typing body paragraph of the text panel
				String body0 = ws.boxList.getNode(ws.boxSelectedID).getBody();
				String body = "";
				if (ws.bodyTyping) {
					if (body0.length() < ws.cursourTrim)
						ws.cursourTrim = body0.length();
					if (0 > ws.cursourTrim)
						ws.cursourTrim = 0;
					
					body = body0.substring(0, body0.length() - ws.cursourTrim) + "|"
							+ body0.substring(body0.length() - ws.cursourTrim);

					if (ws.CursourBlink)
						body = body0.substring(0, body0.length() - ws.cursourTrim) + " "
								+ body0.substring(body0.length() - ws.cursourTrim);
				} else {
					body = body0;
				}
				// body = body + rowMax + " " + crow + "";

				// delete and make font larger!!!!
				if (ws.deleteChar) {
					if (ws.bodyTextFull)
						ws.bodyTextFull = false;
					if (FontBodyH < 30) {
						if (rowMax > crow) {
							FontBodyH += 2;

							if (FontBodyH >= 30)
								FontBodyH = 30;

							ws.deleteChar = false;
							rowMax = 0;
						}
					}
				}

				// ----Automatically move to a new line -----//
				Font BodyFont = new Font(Font.SANS_SERIF, Font.BOLD, FontBodyH);
				g.setFont(BodyFont);
				FontMetrics bodyMath = g.getFontMetrics(BodyFont);
				int widthMax = infoW - ws.indent * 3;
				String currentLine = "";
				int i = 0, row = 1;

				while (i < body.length()) {
					if (bodyMath.stringWidth(currentLine) <= widthMax) {
						currentLine += body.charAt(i);
						i++;
					} else {
						g.drawString(currentLine, infoX + ws.indent,
								(int) (infoY + bodyMath.getHeight() / 1.5 * row + ws.indent * row / 2.0));
						row++;
						currentLine = "";
					}
				}

				// adjusting font size only when one line is deleted completely
				crow = row;
				if (ws.deleteChar) {
					if (rowMax < crow)
						rowMax = crow;
				}

				g.drawString(currentLine, infoX + ws.indent,
						(int) (infoY + bodyMath.getHeight() / 1.5 * row + ws.indent * row / 2.0));

				if (((int) (bodyMath.getHeight() / 1.5 * row + ws.indent * row / 2.0) >= infoH) && !ws.deleteChar) {
					if (FontBodyH <= 10) {
						FontBodyH = 10;
						ws.bodyTextFull = true;
					} else {
						FontBodyH -= 2;
					}
				}
			}
		}

	}

	// _________________________________________________________________//
	/****************************************************************
	 * ****** Box and Connection Graphics************ 1. Smart Connection
	 * methods according to the orientation 2. Auto Adjusting Title displayed on
	 * the box
	 ****************************************************************/
	public void drawAllBoxes() {
		// BOX OBJECTS BELOW
		// Check each boxes one by one from the list of boxes
		for (int i = 0; i < ws.currentBoxCreated; i++) {
			// Assign values
			int x, y, h, w;
			x = ws.boxList.getNode(i).getBoxX();
			y = ws.boxList.getNode(i).getBoxY();
			w = ws.boxList.getNode(i).getBoxWidth();
			h = ws.boxList.getNode(i).getBoxHeight();
			// --------Draw the layout----//
			bufferGraphics.setColor(ws.StringColor);
			bufferGraphics.drawRect(x - 1, y - 1, w + 1, h + 1);
			bufferGraphics.setColor(ws.BoxColorWhite);
			bufferGraphics.fillRect(x, y, w, h);

			// ------------TITLE---------//
			String title = ws.boxList.getNode(i).getTitle();
			Font boxTitleFont = new Font(Font.SANS_SERIF, Font.BOLD, ws.BoxTitleSize);
			bufferGraphics.setFont(boxTitleFont);
			FontMetrics boxFontMath = bufferGraphics.getFontMetrics(boxTitleFont);
			bufferGraphics.setColor(ws.StringColor);

			// ------Smart detection of the title length ----//
			// If the length is longer than the width of the box on
			// the screen, Automatically hide the middle parts
			// In addition, Automatically Center the title to the box
			String displayTitle = "";
			if (boxFontMath.stringWidth(title) >= (w - boxFontMath.stringWidth("  "))) {
				String displayTitleP = "", displayTitleA = "";
				int charCounter = 0;
				while (boxFontMath.stringWidth(displayTitle) < (w - boxFontMath.stringWidth("  "))) {
					if (charCounter % 2 == 0)
						displayTitleP += title.charAt(charCounter);
					else
						displayTitleA = title.charAt(title.length() - 1 - charCounter / 2) + displayTitleA;
					displayTitle = displayTitleP + "..." + displayTitleA;
					charCounter++;
				}
			}
			// Display entirely if it is able to display full text
			else
				displayTitle = title;
			// draw the string after auto adjusting
			bufferGraphics.drawString(displayTitle, (int) (x + (w - boxFontMath.stringWidth(displayTitle)) / 2.0),
					(int) (y + (boxFontMath.getHeight() / 1.5 + h) / 2.0));
		}

		// Hovering Mark of boxes
		// Hovering to indicate the mouse and box position
		if (ws.boxHoverID != -1) {
			bufferGraphics.setColor(ws.StringColor);
			bufferGraphics.setStroke(new BasicStroke(3));// line thickness
			bufferGraphics.drawRect(ws.boxList.getNode(ws.boxHoverID).getBoxX() - 1,
					ws.boxList.getNode(ws.boxHoverID).getBoxY() - 1,
					ws.boxList.getNode(ws.boxHoverID).getBoxWidth() + 2,
					ws.boxList.getNode(ws.boxHoverID).getBoxHeight() + 2);
		}
	}

	// Combining 5 methods after this method in order to automatically detect
	// the orientation and which sides to connect or be connected
	public void drawConnection(Box MainObj, Box SecondaryObj) {
		/*
		 * MainObj = The box tries to connect to other box SecondaryObj = The
		 * box will be connected by MainObj - 5 Points of both Main and Second
		 * Object
		 */
		// -------N-------- //
		// ---------------- //
		// W------C------E-//
		// ---------------- //
		// -------S-------//

		Point MainCentre = new Point(MainObj.getBoxX() + (int) (MainObj.boxWidth / 2.0),
				MainObj.getBoxY() + (int) (MainObj.getBoxHeight() / 2.0));
		Point SecondaryCentre = new Point(SecondaryObj.getBoxX() + (int) (SecondaryObj.boxWidth / 2.0),
				SecondaryObj.getBoxY() + (int) (SecondaryObj.getBoxHeight() / 2.0));

		Point NorthOfSec = new Point(SecondaryCentre.x, SecondaryObj.getBoxY());
		Point SouthOfSec = new Point(SecondaryCentre.x, SecondaryObj.getBoxY() + SecondaryObj.getBoxHeight());
		Point EastOfSec = new Point(SecondaryObj.getBoxX() + SecondaryObj.getBoxWidth(), SecondaryCentre.y);
		Point WestOfSec = new Point(SecondaryObj.getBoxX(), SecondaryCentre.y);
		Point NorthOfMain = new Point(MainCentre.x, MainObj.getBoxY());
		Point SouthOfMain = new Point(MainCentre.x, MainObj.getBoxY() + MainObj.getBoxHeight());
		Point EastOfMain = new Point(MainObj.getBoxX() + MainObj.getBoxWidth(), MainCentre.y);
		Point WestOfMain = new Point(MainObj.getBoxX(), MainCentre.y);

		// decision made by the cross decision
		int decision = SecondaryPositionRelatedToMainObject(MainObj, SecondaryObj);

		// Connects the sides between main and secondary objects
		// According to the orientation made by the above decision
		if (decision == 1) {
			drawCubicLineNS(NorthOfMain, SouthOfSec);
			triangleEndNS(NorthOfMain, SouthOfSec, 10, 10);
		} else if (decision == 2) {
			drawCubicLineEW(EastOfMain, WestOfSec);
			triangleEndEW(EastOfMain, WestOfSec, 10, 10);
		} else if (decision == 3) {
			drawCubicLineNS(SouthOfMain, NorthOfSec);
			triangleEndNS(SouthOfMain, NorthOfSec, -10, 10);
		} else if (decision == 4) {
			drawCubicLineEW(WestOfMain, EastOfSec);
			triangleEndEW(WestOfMain, EastOfSec, -10, 10);
		}
	}

	// ------------Bezier Curve---------///
	// Built-in Bezier-Curve of Cubic line
	// These 2 methods will automatically calculate the best bezier points
	// according to the two ends (Simply by calculating the midpoints)

	// Draw the North and South sides (Up and down - Vertical Curves)
	public void drawCubicLineNS(Point a, Point b) {
		bufferGraphics.setStroke(new BasicStroke(2));
		CubicCurve2D cubicLine = new CubicCurve2D.Double();
		cubicLine.setCurve(a.getX(), a.getY(), a.getX(), (int) ((a.getY() + b.getY()) / 2), b.getX(),
				(int) ((a.getY() + b.getY()) / 2), b.getX(), b.getY());
		bufferGraphics.draw(cubicLine);
	}

	// Draw the East and West sides (Left and Right - Horizontal Curves)
	public void drawCubicLineEW(Point a, Point b) {
		bufferGraphics.setStroke(new BasicStroke(2));
		CubicCurve2D cubicLine = new CubicCurve2D.Double();
		cubicLine.setCurve(a.getX(), a.getY(), (int) ((a.getX() + b.getX()) / 2), a.getY(),
				(int) ((a.getX() + b.getX()) / 2), b.getY(), b.getX(), b.getY());
		bufferGraphics.draw(cubicLine);
	}

	// --------Triangles of inputs and out put-------//
	// Horizontal Direction
	public void triangleEndEW(Point a, Point b, int w, int h) {
		Polygon mark = new Polygon();
		mark.addPoint(b.x + w, b.y);
		mark.addPoint(b.x, b.y + h / 2);
		mark.addPoint(b.x, b.y - h / 2);
		bufferGraphics.fill(mark);

		// Start
		Polygon markS = new Polygon();
		markS.addPoint(a.x + w, a.y);
		markS.addPoint(a.x, a.y + h / 2);
		markS.addPoint(a.x, a.y - h / 2);
		bufferGraphics.fill(markS);
	}

	// Vertical Direction
	public void triangleEndNS(Point a, Point b, int w, int h) {
		Polygon mark = new Polygon();
		mark.addPoint(b.x, b.y - w);
		mark.addPoint(b.x + h / 2, b.y);
		mark.addPoint(b.x - h / 2, b.y);
		bufferGraphics.fill(mark);

		// Start
		Polygon markS = new Polygon();
		markS.addPoint(a.x, a.y - w);
		markS.addPoint(a.x + h / 2, a.y);
		markS.addPoint(a.x - h / 2, a.y);
		bufferGraphics.fill(markS);
	}

	// Codes for deciding the orientation between main and secondary Object
	public int SecondaryPositionRelatedToMainObject(Box MainObj, Box SecondaryObj) {
		int decision = -1;// One Hover another entirely
		int N = 1, E = 2, S = 3, W = 4;

		// Center of the secondary objects
		Point SecondaryCentre = new Point(SecondaryObj.getBoxX() + (int) (SecondaryObj.boxWidth / 2.0),
				SecondaryObj.getBoxY() + (int) (SecondaryObj.getBoxHeight() / 2.0));

		// -------Linear equations of two diagonal of MainObj------
		Point NEofMain = new Point(MainObj.getBoxX() + MainObj.getBoxWidth(), MainObj.getBoxY());
		double slope = Math.abs(MainObj.boxHeight / (double) MainObj.boxWidth);
		double shift = NEofMain.y - slope * NEofMain.x;
		Point NWofMain = new Point(MainObj.getBoxX(), MainObj.getBoxY());
		double slope2 = -slope;
		double shift2 = NWofMain.y - slope2 * NWofMain.x;

		double YpointOnNE_SW = slope * SecondaryCentre.getX() + shift;
		double YpointOnNW_SE = slope2 * SecondaryCentre.getX() + shift2;

		// Make the x value of center of secondary object as x value and compare
		// the y value with secondary's center in order to make decision of the
		// orientation.
		if (SecondaryCentre.getY() > YpointOnNE_SW) {
			if (SecondaryCentre.getY() > YpointOnNW_SE)
				decision = S;
			else
				decision = W;
		} else {
			if (SecondaryCentre.getY() > YpointOnNW_SE)
				decision = E;
			else
				decision = N;
		}
		return decision;
	}

	// _________________________________________________________________//
	/****************************************************************
	 * ****** Toolkits (Methods are used for supporting above methods in order
	 * to create better graphics)***************************
	 ****************************************************************/
	// Color Rainbow Effects -- Code I used last year --
	public static Color ColorLighter(int ampp, int shiftCC) {
		int Frequency = 0;
		int center = 255 - ampp;
		int Y1 = (int) (Math.sin(Frequency + shiftCC) * ampp + center);
		int Y2 = (int) (Math.sin(Frequency + 2 + shiftCC) * ampp + center);
		int Y3 = (int) (Math.sin(Frequency + 4 + shiftCC) * ampp + center);
		Color vvv1 = new Color(Y1, Y2, Y3);
		return (vvv1);
	}

	// Transparency one line instead of reassigning the alpha values
	// make 0-10 as 0.0f - 1.0f transparency level
	public void transparency(float i, Graphics g) {
		float alpha = i * 0.1f;
		AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		((Graphics2D) g).setComposite(alcom);
	}

	// Resize the image
	// Methods helped minimize the inputs and give a clear idea of the function
	public Image ResizedImage(Image originImage, int originWidth, int origintHeight, int newWidth, int newHeight) {
		// Create a resizedImage and wait for the graphics
		BufferedImage resizedImage = new BufferedImage(originWidth, origintHeight, BufferedImage.TYPE_INT_RGB);
		// Make image editable
		Graphics2D g2 = resizedImage.createGraphics();
		// Rendering the images in order to give a smooth appearance
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// Apply the image and resize it on the graphics
		g2.drawImage(originImage, ws.PanelX, ws.PanelY, newWidth, newHeight, null);

		return resizedImage;
	}// resize zoom

}
