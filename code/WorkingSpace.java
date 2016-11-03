import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class WorkingSpace implements MouseMotionListener, MouseListener, ActionListener, KeyListener,
		MouseWheelListener, ComponentListener {

	/*******************************
	 ***** FRAME VARIABLES *****
	 *******************************/
	public JLabel hint;
	public JFrame jframe;
	/*******************************
	 ***** Class VARIABLES *****
	 *******************************/
	public static WorkingSpace workingArea = new WorkingSpace();
	static IntroductionPage page = IntroductionPage.page;// Introduction
	public RenderPanel renderPanel;// Render all the variables to the visual one

	// OBJECT list - a list of box
	public BoxList boxList = new BoxList();

	/*******************************
	 ***** Color VARIABLES *****
	 *******************************/
	public Color backgroundColor = new Color(206, 194, 160), BoxColorWhite = new Color(240, 241, 244),
			StringColor = new Color(111, 37, 24), PanelLineColor = new Color(0, 0, 0),
			FrameHighlighterColor = new Color(200, 100, 203), HoverPanelColor = new Color(240, 241, 244),
			HoverPanelStroke = new Color(131, 50, 40), HoverPanelString = new Color(100, 20, 30),
			tutorialHighlighterColor = Color.RED;
	/*******************************
	 ********** VARIABLES **********
	 *******************************/

	// Quantity of action in order to active
	// Typing and create new box
	public final int EnterNumberToActive = 1;// One enter to type
	public final int MouseNumberClickToActive = 2;// Double Click to create new
													// box

	// Mouse x,y coordinate in order to restore e.getX() & e.getY()
	public int mouseX, mouseY;
	// Count for the continuous clicking times in the same box
	// If it reaches the MouseNumberClickToActive
	public int clickInBoxCounter = 0;
	// Check if the box will moving together with the mouse
	// Happens only when creating first new object
	public boolean boxMovingDecision = true;
	// Mouse click position (x,y)
	public int mouseClickX = 0, mouseClickY = 0;
	// Difference between the Mouse position and box's up left corner
	// In order to make box appear at the proper position every time
	// mouse drag the box or change the box position
	// If not calculate it , box will auto reset position
	// of up-left corner with mouse position
	public int boxMouseDifferenceX = 200, boxMouseDifferenceY = 200;

	// Following variables are for the
	// Typing panel
	public int PanelX = 0, PanelY = 0, rotation = 0, boxSelectedID = -1, EnterCounter = 0, MouseOriginX = -1,
			MouseOriginY = -1, originPanelX = 0, originPanelY = 0, TimerTick = 0, TextPanelX = 0, TextPanelY = 0,
			TextPanelHeight = 0, TextPanelWidth = 0, MaxTextPanelHeight = 400, MaxTextPanelWidth = 400,
			changingRateTextPanel = 5, indent = 20, TitleHeight = 0, MaxTitleHeight = 60, arcTextPanel = 0,
			MaxarcTextPanel = 10, CursourX = 0, CursourY = -100, CursourHeight = 10, CursourSpace = 5, LetterSpace = 7,
			characterNum = -1, TextPanelMoveX = -1, TextPanelMoveY = -1, transparencyOfTextPanel = 7, times = 30,
			cursourTrim = 0, PanelTolerance = 10;

	// Default setting for the name of files and box
	String title = "", body = "", nameOfSelectedProject = "";
	char character = ' ';

	// Following variables are for mouse move hovering a certain object
	public int boxHoverID = -1, mouseHoverBoxWithPanelID = -1, hoverRemainingTicks = 0, hoverTicksTime = 50,
			hoverStringFontH = 15, hoverPanelMaxWidth = 200, ticks = 0;

	// Background zooming
	public double zoomPercentage = 1, scrollingSensitivity = 100000.0, PANELRATIO = 1;

	// Variable for holding the ID of the box in each action
	public int currentBoxNumberSelected = 0, ShiftMainIDSelected = -1, EnterMainIDSelected = -1;
	public int currentBoxCreated = 1;// restore the total box length

	// Boolean for identify different action and modes
	public boolean newBox = false, nextSelectBox = false, ShiftHold = false, Reset = false, PanelMovingDecision = false,
			PanelMoving = false, ResetEffects = false, EnterCheck = false,
			// Following are booleans for typing panel
			TypingDecision = false, typing = false, DoubleEnterOrMore = false, CursourBlink = false,
			PanelCancel = false, PanelConfirm = false, panelClosingAction = false, PanelAtLeft = false,
			PanelAtTop = false, PanelAtBot = false, PanelAtRight = false, PanelAlignedHorizon = false,
			PanelAlignedVertical = false, CHold = false, PanelSelected = false, PanelExtendingAnimationPreset = false,
			deleteChar = false, titleTextFull = false, bodyTextFull = false, DirectionKey = false,
			noAnimationHappen = false, bodyTyping = false, renderPanelRepaintBoolean = false;// Typing
																								// Panel
																								// Arcs
	public double arcs = 0;
	// Title Font
	Font TitleInPanelFont = new Font("Times New Roman", Font.BOLD, 20);

	// Check if Overwriting
	public boolean OverwritingWarning = false;

	/*******************************
	 ***** CIRCULAR MENU VARIABLES *****
	 *******************************/
	public int MasterCircleX, MasterCircleY;
	public int CircleSize = 50, xc, NumberOfCircle = 5, gapOfCircles = 20,
			MaxLength = (NumberOfCircle - 1) * (CircleSize + gapOfCircles), rotationAnimation = 0, hoverTick = 0,
			arrowDown = 0, arrowUp = 0, lineNum = 0;// rotate
	//
	public boolean mouseHoverCircle = false, MenuAppear = false, control_Increase = false;
	public boolean[] right1menuButtonClicked = new boolean[NumberOfCircle - 1],
			right1menuButtonHovered = new boolean[NumberOfCircle - 1];

	// Timer Gate for animation
	// such as cursor blink
	public Timer timer = new Timer(10, this);
	public boolean[] timerRun = { false, false, false, false, false };
	// 0: Panel Reset;
	// 1: Menu Hover;
	// 2: Typing Mode
	// 4: Buttons

	// Text Out Put Variables
	public TextOutput textOutput = new TextOutput(boxList);
	public int scrollYTextOutput = 0;
	public int scrollSensitisityTextOutput = 3;

	// ---------Setting----------//
	// Setting Panel Variables And Objects
	int TutorialFontSize = 20, BoxTitleSize = 20;
	public Slider slTutor = new Slider();
	public Slider slTitle = new Slider();
	/* _-_-_-_-_-_VARIABLES END_-_-_-_-_-_-_ */

	// Save Panel Variables
	public SaveAndLoad saveAndLoad = new SaveAndLoad();
	public TextField nameOfTheFile = new TextField(), rootOfTheFile = new TextField(),
			buttonCancel = new TextField("Cancel", 20), buttonConfirm = new TextField("Confirm", 20),
			text1 = new TextField("Name:", 20), text2 = new TextField("Root:", 20), text3 = new TextField("Image:", 20),
			text4 = new TextField("Text:", 20);
	public Switch switchImage = new Switch(), switchFile = new Switch();

	// Reading Panel- Load past files
	public SlidingSelector readFilesSelector = new SlidingSelector();
	public SaveLog logHistory = new SaveLog();

	/*******************************
	 ***** BUILD STRUCTURE & FRAMES & PRESETTING VARIABLES *****
	 *******************************/
	public WorkingSpace() {

		boxList.addNode(new Box());// CREATE DEFAULT OBJECT

		jframe = new JFrame("MY MIND");// TITLE OF PROGRAM

		jframe.setLayout(new BorderLayout());// GIVE A LAYOUT
		jframe.setVisible(true);// SHOW ON SCREEN
		jframe.setSize(800, 800);// DEFAULT SIZE ON SCREEN
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setBackground(backgroundColor);// SET DEFAULT BACKGROUND COLOR
		jframe.setLocationRelativeTo(null);
		jframe.setResizable(false);
		// SET PROGRAM DEFAULTLY CLOSABLE BY CLICKING THE RED CROSS BUTTON ON
		// LEFT TOP CORNER

		jframe.addKeyListener(this);
		// Add graphics panel on this frame
		jframe.add(renderPanel = new RenderPanel(), BorderLayout.CENTER);
		// Add Component Listener
		jframe.addComponentListener(this);
		renderPanel.setBackground(Color.BLACK);// Default frame color
		renderPanel.addMouseMotionListener(this);// ADD MOUSE MOVEMENTS TO THE
													// FRAME
		renderPanel.addMouseListener(this);// ADD MOUSE CLICK TO THE FRAME
											// GRAPHICS
		jframe.addMouseWheelListener(this);
		// jframe.setResizable(false);// RESIZABLITY: NON-AVAILABLE
		// MAKE PROGRAM WINDOW APPEAR IN CENTER ON THE SCREEN
		hint = new JLabel(" *Hint: ");// HINT PANEL
		// hint.addMouseMotionListener(this);// ADD MOUSE MOVEMENTS TO THE FRAME
		// hint.addMouseListener(this);// ADD MOUSE CLICK TO THE FRAME
		hint.setSize(20, 20);// SIZE
		hint.setHorizontalAlignment(JLabel.LEFT);// MAKE WORLDS ALIGNED LEFT
		hint.setFont(new Font("Serif", Font.PLAIN, 12));// SET FONT
		jframe.add(hint, BorderLayout.SOUTH);// MAKE HINT PANEL APPEAR AT BOTTOM
												// OF THE SCREEN
		MasterCircleX = jframe.getWidth() - 100;// MENUCIRCLE POSITION
		MasterCircleY = jframe.getHeight() - 125;// MENUCIRCLE POSITION
		// Center the box
		boxList.getNode(0).setBoxPosition((jframe.getWidth() - boxList.getNode(0).getBoxWidth()) / 2,
				(jframe.getHeight() - boxList.getNode(0).getBoxHeight()) / 2);

		// ----Preset all the save and loading part------
		saveAndLoadButtonsSetUp();

		// Try to read or create the binary file of log cache
		try {
			readFilesSelector.AddStringList(logHistory.readLog());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// ------Preset all the values of the panel-------
		// Assign them with default values
		setUpSettingPanel();// Preset All values here
	}

	/*********************************************
	 ***** Method for set up the load and save*****
	 *********************************************/
	public void saveAndLoadButtonsSetUp() {
		// Field Text Box Display
		nameOfTheFile.setFieldLimits(20);
		rootOfTheFile.setFieldLimits(20);
		nameOfTheFile.setPosition((jframe.getWidth() - 400) / 2 + 100, (jframe.getHeight() - 300) / 2 + 100);
		rootOfTheFile.setPosition((jframe.getWidth() - 400) / 2 + 100, (jframe.getHeight() - 300) / 2 + 140);
		rootOfTheFile.setColor(new Color(44, 168, 241), new Color(240, 241, 244), new Color(100, 100, 100),
				Color.WHITE);

		// Return current file location - Cannot be edited
		File temp;
		String filePath = "";
		try {
			temp = File.createTempFile(this.getClass().getName(), ".java");
			String absolutePath = temp.getAbsolutePath();
			filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Assign the string to the textField object
		rootOfTheFile.setString(
				filePath.substring(0, 9) + "..." + filePath.substring(filePath.length() - 8, filePath.length()));
		text1.setPosition((jframe.getWidth() - 400) / 2 + 20, (jframe.getHeight() - 300) / 2 + 100);
		text2.setPosition((jframe.getWidth() - 400) / 2 + 20, (jframe.getHeight() - 300) / 2 + 140);
		text1.setColor(backgroundColor, backgroundColor, Color.BLACK, backgroundColor);
		text2.setColor(backgroundColor, backgroundColor, Color.BLACK, backgroundColor);
		buttonCancel.setPosition((jframe.getWidth() - 400) / 2 + 20, (jframe.getHeight() - 300) / 2 + 250);
		buttonConfirm.setPosition((jframe.getWidth() - 400) / 2 + 280, (jframe.getHeight() - 300) / 2 + 250);

		text3.setColor(backgroundColor, backgroundColor, Color.BLACK, backgroundColor);
		text4.setColor(backgroundColor, backgroundColor, Color.BLACK, backgroundColor);
		text3.setPosition((jframe.getWidth() - 400) / 2 + 20, (jframe.getHeight() - 300) / 2 + 180);
		text4.setPosition((jframe.getWidth() - 400) / 2 + 200, (jframe.getHeight() - 300) / 2 + 180);
		switchImage.setPosition((jframe.getWidth() - 400) / 2 + 110, (jframe.getHeight() - 300) / 2 + 180);
		switchFile.setPosition((jframe.getWidth() - 400) / 2 + 280, (jframe.getHeight() - 300) / 2 + 180);
	}

	/*******************************
	 ***** CHECK IF MOUSE ENTER A CERTAIN BOX ***** And return the boolean
	 * statements
	 *******************************/
	public boolean boxAndMousePositionCheck(int i) {
		if (mouseX >= (boxList.getNode(i).getBoxX() * zoomPercentage + PanelX)
				&& mouseX <= (boxList.getNode(i).getBoxX() * zoomPercentage
						+ boxList.getNode(i).getBoxWidth() * zoomPercentage + PanelX)
				&& mouseY >= (boxList.getNode(i).getBoxY() * zoomPercentage + PanelY)
				&& mouseY <= (boxList.getNode(i).getBoxY() * zoomPercentage
						+ boxList.getNode(i).getBoxHeight() * zoomPercentage + PanelY)
				&& boxList.getNode(i).getMouseDragBoxCondition() != true)
			return true;
		else
			return false;
	}

	/*******************************
	 ***** CHECK IF MOUSE ENTER ANY BOXES AND RETURN THE POSITION***** Return the ID
	 * of the box where the mouse position is at
	 *******************************/
	public int boxAndMousePositionCheckAll() {
		int i = 0;
		do {
			if (boxAndMousePositionCheck(i))
				break;
			i++;
		} while (i < boxList.getLength());
		if (i >= boxList.getLength())
			i = -1;
		return i;
	}

	/*****************************************************************
	 ***** CHECK IF mouse enter the Abstract rectangle restriction *****
	 ****************************************************************/
	public boolean ifEnterTheSquareArea(int x, int y, int Width, int Height) {
		if (mouseX >= x && mouseX <= (x + Width) && mouseY >= y && mouseY <= (y + Height))
			return true;
		else
			return false;
	}

	/*******************************
	 ***** MOUSE EVENTS*****
	 *******************************/
	@Override
	public void mouseDragged(MouseEvent e) {
		// Shows up
		if (page.hide) {
			mouseHoverBoxWithPanelID = -1;

			clickInBoxCounter = 0;// Once dragging, reset the clicky counter to
									// 0
			mouseX = e.getX();
			mouseY = e.getY();

			if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
					&& !right1menuButtonClicked[3]) {
				// if ENTER key is not clicked more than 2 times, If 2 times,
				// system
				// enter typing mode that requires information typed by user and
				// store
				// inside each box
				if (!DoubleEnterOrMore) {
					// Moving box by dragging each individual boxes
					if (boxMovingDecision && !PanelMovingDecision) {
						hint.setText(" *Hint: Mouse Dragging! Release to set");// HINT
						// Check Position if mouse is dragging on any box object
						if (boxAndMousePositionCheckAll() != -1
								&& !boxList.getNode(currentBoxNumberSelected).getMouseDragBoxCondition()) {
							currentBoxNumberSelected = boxAndMousePositionCheckAll();
							boxMouseDifferenceX = (int) (mouseX
									- boxList.getNode(currentBoxNumberSelected).getBoxX() * zoomPercentage);
							boxMouseDifferenceY = (int) (mouseY
									- boxList.getNode(currentBoxNumberSelected).getBoxY() * zoomPercentage);
							boxList.getNode(currentBoxNumberSelected).setMouseDragBoxCondition(true);
						}
						// Reset box position every time mouse dragged and moved
						// while storing them inside each individual box
						// These changes wont change directly on the graphics
						// but directly to selected box object and pass the data
						// back to the graphics
						if (boxList.getNode(currentBoxNumberSelected).getMouseDragBoxCondition()) {
							// save the box position into the box object in the
							// boxList
							boxList.getNode(currentBoxNumberSelected)
									.setBoxX((int) ((mouseX - boxMouseDifferenceX) / zoomPercentage));
							boxList.getNode(currentBoxNumberSelected)
									.setBoxY((int) ((mouseY - boxMouseDifferenceY) / zoomPercentage));
						}

						// Refreshing frames every time mouse moved or dragging
						// happened
						renderPanelRepaintBoolean = true;
					}
					// Panel start open
					if (PanelMovingDecision) {
						// Following id statement will restore the previous
						// Panel
						// position and mouse position before mouse dragging or
						// changing
						// position
						if (!PanelMoving) {
							MouseOriginX = (int) (e.getX());
							MouseOriginY = (int) (e.getY());
							originPanelX = PanelX;
							originPanelY = PanelY;
							PanelMoving = true;
						}

						// Make sure panel is drawn on the point mouse clicked
						// Or the graphic panel will be repositioned to mouse
						// position
						PanelX = (int) (mouseX - MouseOriginX + originPanelX);
						PanelY = (int) (mouseY - MouseOriginY + originPanelY);

						// frame refreshing
						renderPanelRepaintBoolean = true;
					}
				}
			}

			// Assign function for key event to the buttons in Setting Panel
			if (right1menuButtonClicked[3]) {
				slTutor.sliderStateCheck(e.getX(), e.getY());
				slTitle.sliderStateCheck(e.getX(), e.getY());
				renderPanelRepaintBoolean = true;
			}
			// Assign function for key event to the buttons loading file Panel
			if (right1menuButtonClicked[2]) {
				readFilesSelector.checkState(e);
				renderPanelRepaintBoolean = true;
			}

			// Render gate, to minimize the frequency of repaint
			if (renderPanelRepaintBoolean) {
				renderPanel.repaint();
				renderPanelRepaintBoolean = false;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (page.hide) {
			mouseX = e.getX();
			mouseY = e.getY();
			int currentboxSelectedID = boxAndMousePositionCheckAll();

			/// Menu button check
			for (int i = 0; i < right1menuButtonHovered.length; i++) {
				if (MenuAppear && mouseX >= (MasterCircleX - (gapOfCircles + CircleSize) * (i + 1))
						&& mouseX <= (MasterCircleX - gapOfCircles * (i + 1) - CircleSize * i)
						&& mouseY <= (MasterCircleY + CircleSize) && mouseY >= MasterCircleY) {
					right1menuButtonHovered[i] = true;
				} else {
					right1menuButtonHovered[i] = false;
				}
			}

			// When the setting panel is opened
			if (right1menuButtonClicked[3]) {
				colorOfBoxPanel.checkState(e);
				colorOfBoxStroke.checkState(e);
				colorOfFrameHighlighter.checkState(e);
				colorOfFrameBackground.checkState(e);
				colorOfHoverHintPanel.checkState(e);
				colorOfHoverHintStroke.checkState(e);
				colorOfHoverHintString.checkState(e);
				colorOfTutorialString.checkState(e);

				renderPanelRepaintBoolean = true;
			}

			// -----Default Mode----//
			// When no other functional panel is turned on or opened
			if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
					&& !right1menuButtonClicked[3]) {
				////// description text showing
				if (mouseHoverBoxWithPanelID != currentboxSelectedID) {
					mouseHoverBoxWithPanelID = currentboxSelectedID;
					hoverRemainingTicks = 0;
					if (mouseHoverBoxWithPanelID != -1) {
						timerRun[3] = true;
						// --------Timer Gate: Control the timer on and off,
						// Minimize quantity of memory usages and threading
						if (timerRun[0] || timerRun[1] || timerRun[2] || timerRun[3] && !timer.isRunning())
							timer.start();
					}
				}
				if (mouseHoverBoxWithPanelID == -1) {
					if (timerRun[3]) {
						hoverRemainingTicks = 0;
						// --------Timer Gate: Control the timer on and off,
						// Minimize quantity of memory usages and threading
						if (!timerRun[0] && !timerRun[1] && !timerRun[2] && timerRun[3] && timer.isRunning())
							timer.stop();
						timerRun[3] = false;
					}
				}

				// --------Mouse Hover Box && Show information-----//
				// Showing each box info, once mouse hover them
				// But it wont show info once entering the typing info mode
				boxHoverID = boxAndMousePositionCheckAll();
				// if enter the box objects, show info for debugging
				// if enter the graphic panel but not the boxes , output mouse
				// entered
				if (boxHoverID != -1 && clickInBoxCounter == 0 && boxHoverID < boxList.getLength()) {
					hint.setText(" *Hint: Mouse Entered! Hovering Object! At Object# " + boxHoverID + " Info: "
							+ boxList.getNode(boxHoverID).toString()
							+ " ! Drag to reposition! Triple-Click to create a new object!");
					renderPanelRepaintBoolean = true;
				} else if (clickInBoxCounter == 0) {
					hint.setText(" *Hint: Mouse Entered!");
					boxHoverID = -1;
					renderPanelRepaintBoolean = true;
				}

				// -----------Create New Box-----------//
				// create new box and box move while moving mouse
				if (newBox) {
					mouseHoverBoxWithPanelID = -1;
					if (nextSelectBox) {
						if (currentboxSelectedID != -1) {
							hint.setText(" *Hint: Click to drop the box!");
							boxMouseDifferenceX = (int) (mouseX
									- boxList.getNode(currentboxSelectedID).getBoxX() * zoomPercentage);
							boxMouseDifferenceY = (int) (mouseY
									- boxList.getNode(currentboxSelectedID).getBoxY() * zoomPercentage);
							currentBoxNumberSelected = currentBoxCreated;
							currentBoxCreated++;
							// Create new object box
							Box box = new Box();
							boxList.addNode(box);
							nextSelectBox = false;
						}
					}
					boxList.getNode(currentBoxNumberSelected)
							.setBoxX((int) ((mouseX - boxMouseDifferenceX) / zoomPercentage));
					boxList.getNode(currentBoxNumberSelected)
							.setBoxY((int) ((mouseY - boxMouseDifferenceY) / zoomPercentage));

					renderPanelRepaintBoolean = true;
				}

				// -------Typing Mode--------//
				// Change transparency while mouse enter the button area to give
				// a visual feedback to user
				// Double enter mode(Typing mode)
				if (DoubleEnterOrMore && !panelClosingAction) {
					if (ifEnterTheSquareArea((jframe.getWidth() - MaxTextPanelWidth) / 2 + indent,
							TextPanelMoveY + MaxTextPanelHeight + indent, 60, 20)) {
						transparencyOfTextPanel = 10;
						PanelCancel = true;
						renderPanelRepaintBoolean = true;
					} else if (ifEnterTheSquareArea(
							(jframe.getWidth() - MaxTextPanelWidth) / 2 - indent - 60 + MaxTextPanelWidth,
							TextPanelMoveY + MaxTextPanelHeight + indent, 60, 20)) {
						transparencyOfTextPanel = 10;
						renderPanelRepaintBoolean = true;
						PanelConfirm = true;
						hint.setText("confirm!!!");
					} else {
						transparencyOfTextPanel = 7;
						PanelCancel = false;
						PanelConfirm = false;
						renderPanelRepaintBoolean = true;
					}
				}
			}
			// -------------Menu-----------//
			// Circular Menu Pops up when hovering
			// increment values
			if (mouseX >= MasterCircleX && mouseX <= (MasterCircleX + CircleSize)
					&& mouseY <= (MasterCircleY + CircleSize) && mouseY >= MasterCircleY) {
				mouseHoverCircle = true;

				timerRun[1] = true;
				if (timerRun[0] || timerRun[1] || timerRun[2] || timerRun[3] && !timer.isRunning())
					timer.start();
				// TIMER START// USED FOR TIMER BASED ANIMATION
			} else
				mouseHoverCircle = false;

			// Once MenuBar Appeared, mouse can stay inside the entire menu bar
			// (Change restriction once menu is full extended)
			if (MenuAppear && mouseX >= (MasterCircleX - MaxLength) && mouseX <= (MasterCircleX + CircleSize)
					&& mouseY <= (MasterCircleY + CircleSize) && mouseY >= MasterCircleY) {
				mouseHoverCircle = true;
			}

			// Check if it requires to refresh
			if (renderPanelRepaintBoolean) {
				renderPanel.repaint();
				renderPanelRepaintBoolean = false;
			}
		}

	}

	////////////////////////////////////////
	// ORDER OF MENU BUTTONS:
	// TEXT OUTPUT = right1menuButtonClicked[0]
	// SAVE = right1menuButtonClicked [1]
	// LOAD = right1menuButtonClicked [2]
	// SETTINGS = right1menuButtonClicked [3]
	////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e) {
		// Works only after the initial welcome animation
		if (page.hide) {
			// Panel of Text out put
			// Detect if certain menu button is clicked
			// And Give out the feedback
			for (int i = 0; i < right1menuButtonClicked.length; i++) {
				if (MenuAppear && mouseX >= (MasterCircleX - (gapOfCircles + CircleSize) * (i + 1))
						&& mouseX <= (MasterCircleX - (gapOfCircles) * (i + 1) - CircleSize * i)
						&& mouseY <= (MasterCircleY + CircleSize) && mouseY >= MasterCircleY) {
					if (right1menuButtonClicked[i])
						right1menuButtonClicked[i] = false;
					else
						right1menuButtonClicked[i] = true;
					for (int i2 = 0; i2 < right1menuButtonClicked.length; i2++) {
						if (i2 != i) {
							right1menuButtonClicked[i2] = false;
						}
					}
					break;
				}
			}

			// if textOutPut panel is clicked, enter the out put panel
			if (right1menuButtonClicked[0]) {
				// Do the preparation
				textOutput.prepareToOutput();
				textOutput.printLines(0, 0, textOutput.max(0), 0);
				textOutput.checkLoseBoxes();
				renderPanelRepaintBoolean = true;
			}

			// if Save panel is clicked , Pop up Saving panel
			if (right1menuButtonClicked[1]) {
				int ex = e.getX();
				int ey = e.getY();

				// ex=e.getX();
				// Give Button state with mouse clicking position,
				nameOfTheFile.textStateCheck(ex, ey);
				buttonCancel.textStateCheck(ex, ey);
				buttonConfirm.textStateCheck(ex, ey);
				switchFile.SwitchStateCheck(ex, ey);
				switchImage.SwitchStateCheck(ex, ey);

				timerRun[4] = true;
				renderPanelRepaintBoolean = true;

				// Pop Up overwriting sign
				// In order to make sure
				// that user is going to overwrite the old file
				if (OverwritingWarning) {
					// When Have same file name, ask user if to overwrite
					if (buttonCancel.getState() || buttonConfirm.getState()) {
						if (buttonCancel.getState()) {
							OverwritingWarning = false;
						} else {

							try {
								saveAndLoad.saveMap(nameOfTheFile.getString(), boxList);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							OverwritingWarning = false;
							right1menuButtonClicked[1] = false;

						}
						// Restate button back to false, check position.
						buttonCancel.setState(false);
						buttonConfirm.setState(false);
					}
				}

				// If the mouse entered buttons or switches area, and clicked in
				// this event,
				// Commands will run
				if (buttonCancel.getState() || buttonConfirm.getState()) {
					if (buttonCancel.getState()) {
						// cancel;Not Saved
					} else {
						// Confirmed saved

						// Save as file if switch is on
						if (switchFile.getState()) {
							try {
								if (logHistory.checkRepetition(nameOfTheFile.getString())) {
									OverwritingWarning = true;
									buttonConfirm.setState(false);
								}
								logHistory.addLog(nameOfTheFile.getString());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							// Not Overwriting
							if (!OverwritingWarning) {
								try {
									saveAndLoad.saveMap(nameOfTheFile.getString(), boxList);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							// save history

						}
						// Save as image if save switch is on
						if (switchImage.getState()) {
							try {
								saveAndLoad.saveMapAsImage(nameOfTheFile.getString(),
										(BufferedImage) renderPanel.offscreen);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					if (!OverwritingWarning) {
						right1menuButtonClicked[1] = false;// Close the save
															// pop-up
						try {
							readFilesSelector.AddStringList(logHistory.readLog());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			} else if (!right1menuButtonClicked[1]) // if save button clicked
				timerRun[4] = false;

			if (right1menuButtonClicked[2]) {
				// Load Place
				int ex = e.getX();
				int ey = e.getY();
				buttonCancel.textStateCheck(ex, ey);
				buttonConfirm.textStateCheck(ex, ey);
				readFilesSelector.checkMouseSelect(ex, ey);
				if (!buttonCancel.getState() && !buttonConfirm.getState()) {
					nameOfSelectedProject = readFilesSelector.getSelected();
				}
				if (buttonCancel.getState() || buttonConfirm.getState()) {
					right1menuButtonClicked[2] = false;
					if (buttonConfirm.getState()) {
						if (nameOfSelectedProject == "") {
							System.out.println("Nothing is selected");
						} else {
							try {

								saveAndLoad.readMap(nameOfSelectedProject, boxList, logHistory);
								textOutput.prepareToOutput();
								textOutput.printLines(0, 0, textOutput.max(0), 0);
								textOutput.checkLoseBoxes();
								// Reset Values
								currentBoxCreated = boxList.getLength();
								currentBoxNumberSelected = 0;
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}

			if (right1menuButtonClicked[3] || right1menuButtonClicked[0]) {
				setUpSettingPanel(e.getX(), e.getY());// Preset All values here
				renderPanelRepaintBoolean = true;
			}
			if (!right1menuButtonClicked[3] || !right1menuButtonClicked[0]) {
				ApplySettingToAll();
			}
			// Once mouse enters the Cancel button or confirm button while
			// typing
			// mode is running
			// If enter areas and also clicked, statements inside if will
			// process
			if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
					&& !right1menuButtonClicked[3]) {
				if (PanelCancel) {
					TimerTick = 0;
					PanelCancel = false;
					TypingDecision = false;// cant type
					title = "";
					boxList.getNode(boxSelectedID).setTitle(title);
					panelClosingAction = true;
				} else if (PanelConfirm) {
					TimerTick = 0;
					TypingDecision = false;// cant type
					PanelConfirm = false;
					panelClosingAction = true;
				}

				if (ShiftHold) {
					if (boxSelectedID != boxAndMousePositionCheckAll() && ShiftMainIDSelected >= 0) {

						clickInBoxCounter = 0;
						boxSelectedID = boxAndMousePositionCheckAll();
						boxList.getNode(ShiftMainIDSelected).addLink(boxSelectedID, boxList);
						hint.setText(" *Hint: connection built " + boxList.getNode(ShiftMainIDSelected).toString());
						renderPanelRepaintBoolean = true;
					}
				}

				// If not double enter, not in typing mode,
				else if (!DoubleEnterOrMore) {
					// check if create new box or not
					if (newBox & !nextSelectBox) {
						hint.setText(" *Hint: Object Created!");
						newBox = false;
						boxMovingDecision = true;
						clickInBoxCounter = 0;
					} else {
						mouseX = e.getX();
						mouseY = e.getY();

						// if click inside white box, clicking time adding
						if (boxAndMousePositionCheckAll() != -1) {
							PanelSelected = false;
							if (boxSelectedID != boxAndMousePositionCheckAll()) {
								clickInBoxCounter = 0;
								boxSelectedID = boxAndMousePositionCheckAll();
								renderPanelRepaintBoolean = true;
							}
							clickInBoxCounter++;
							hint.setText(" *Hint: Mouse Clicked! Times: " + clickInBoxCounter + "ClickedAt: "
									+ boxAndMousePositionCheckAll());
						} else {
							PanelSelected = true;
							renderPanelRepaintBoolean = true;
							hint.setText(" *Hint: Panel Selected!!");
						}

						// Following 'if': while mouse clicked 1 times in BOX,
						// decision
						// is made in order to select current box
						if (clickInBoxCounter == 1) {
							hint.setText("Hint: U ve selected this box");
							EnterCheck = true;// &&Delete check

							// Store the shift main ID
							ShiftMainIDSelected = boxSelectedID;

							EnterMainIDSelected = boxSelectedID;
							PanelExtendingAnimationPreset = true;

						} else {
							EnterCheck = false;
							PanelExtendingAnimationPreset = false;
							// if not one click
							ShiftMainIDSelected = -1;
							EnterMainIDSelected = -1;
						}

						// Following 'if': while mouse clicked 3 times in BOX,
						// decision
						// is made in order to create new box object
						if (clickInBoxCounter >= MouseNumberClickToActive) {
							hint.setText(" *Hint: Move your mouse to create your new object!!");
							boxMovingDecision = false;
							newBox = true;
							nextSelectBox = true;
							renderPanelRepaintBoolean = true;
						}
						// Following 'if': while mouse clicked out of the boxes,
						// clicking
						// time
						// will be cleared to 0
						if (boxAndMousePositionCheckAll() == -1) {
							boxMovingDecision = true; // box
							clickInBoxCounter = 0;
							renderPanelRepaintBoolean = true;
						}
					}
				}
				// Typing mode!!
				else {
					clickInBoxCounter = 0;
				}
			}

			// Check if it requires to refresh
			if (renderPanelRepaintBoolean) {
				renderPanel.repaint();
				renderPanelRepaintBoolean = false;
			}
		}
	}

	// PRESET VALUES PANEL BUTTONS, COLOR BOX, TEXTFIELD,
	public TextField tfBoxWidth = new TextField("Box Width:", 20);
	public TextField tfBoxHeight = new TextField("Box Height:", 20);
	public TextField tfGetBoxWidth = new TextField();
	public TextField tfGetBoxHeight = new TextField();

	public TextField colorOfBoxDisplay = new TextField("Color of the Box:", 20);
	public TextField colorOfBoxPanelDisplay = new TextField("Box Panel Color:", 20);
	public TextField colorOfBoxStrokeDisplay = new TextField("Box Stroke Color:", 20);

	public TextField colorOfFrameDisplay = new TextField("Color of the Frame:", 20);
	public TextField colorOfFrameHighlighterDisplay = new TextField("Frame Highlighter:", 20);
	public TextField colorOfFrameBackgroundDisplay = new TextField("Frame Background:", 20);

	public TextField colorOfHoverHintDisplay = new TextField("Color of Hovering Hint:", 20);
	public TextField colorOfHoverHintPanelDisplay = new TextField("Hint Panel:", 20);
	public TextField colorOfHoverHintStrokeDisplay = new TextField("Hint Stroke:", 20);
	public TextField colorOfHoverHintStringDisplay = new TextField("Hint String:", 20);

	public TextField colorOfTutorialStringDisplay = new TextField("Hovering Hint Color:", 20);

	public TextField TutorialFontSizeDisplay = new TextField("Font Size in Tutorial:", 20);
	public TextField TutorialFontSizeNumDisplay = new TextField(this.TutorialFontSize + "", 20);
	public TextField BoxTitleFontSizeDisplay = new TextField("Font Size of Box Title:", 20);
	public TextField BoxTitleFontSizeNumDisplay = new TextField(this.BoxTitleSize + "", 20);

	public ColorBox colorOfBoxPanel = new ColorBox(350, 265, 2, Color.BLACK, this.BoxColorWhite);
	public ColorBox colorOfBoxStroke = new ColorBox(350, 315, 2, Color.BLACK, this.StringColor);

	public ColorBox colorOfFrameHighlighter = new ColorBox(700, 415, 2, Color.BLACK, this.FrameHighlighterColor);
	public ColorBox colorOfFrameBackground = new ColorBox(700, 465, 2, Color.BLACK, this.backgroundColor);

	public ColorBox colorOfHoverHintPanel = new ColorBox(350, 415, 2, Color.BLACK, this.HoverPanelColor);
	public ColorBox colorOfHoverHintStroke = new ColorBox(350, 465, 2, Color.BLACK, this.HoverPanelStroke);
	public ColorBox colorOfHoverHintString = new ColorBox(350, 515, 2, Color.BLACK, this.HoverPanelString);
	public ColorBox colorOfTutorialString = new ColorBox(700, 515, 2, Color.BLACK, this.tutorialHighlighterColor);

	// Preset or Initial the values only once in the program (Called from the
	// preset of the WorkingSpace())
	public void setUpSettingPanel() {

		tfBoxWidth.setColor(Color.white, Color.black, Color.black, Color.gray);
		tfBoxWidth.setPosition(75, 100);
		tfGetBoxWidth.setColor(Color.white, Color.gray, Color.black, Color.white);
		tfGetBoxWidth.setPosition(tfBoxWidth.x + 150, tfBoxWidth.y);
		tfGetBoxWidth.setString(boxList.getNode(0).boxWidth + "");

		tfBoxHeight.setColor(Color.white, Color.black, Color.black, Color.gray);
		tfBoxHeight.setPosition(75, 150);
		tfGetBoxHeight.setColor(Color.white, Color.gray, Color.black, Color.white);
		tfGetBoxHeight.setPosition(tfBoxHeight.x + 150, tfBoxHeight.y);
		tfGetBoxHeight.setString(boxList.getNode(0).boxHeight + "");

		// Box Color set up
		colorOfBoxDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfBoxDisplay.setPosition(75, 220);

		colorOfBoxPanelDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfBoxPanelDisplay.setPosition(120, 270);

		colorOfBoxStrokeDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfBoxStrokeDisplay.setPosition(120, 320);

		colorOfFrameDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfFrameHighlighterDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfFrameBackgroundDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfHoverHintDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfHoverHintPanelDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfHoverHintStrokeDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfHoverHintStringDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		colorOfTutorialStringDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);

		colorOfFrameDisplay.setPosition(425, 370);
		colorOfFrameHighlighterDisplay.setPosition(470, 420);
		colorOfFrameBackgroundDisplay.setPosition(470, 470);
		colorOfHoverHintDisplay.setPosition(75, 370);
		colorOfHoverHintPanelDisplay.setPosition(120, 420);
		colorOfHoverHintStrokeDisplay.setPosition(120, 470);
		colorOfHoverHintStringDisplay.setPosition(120, 520);
		colorOfTutorialStringDisplay.setPosition(425, 520);

		slTitle.setPosition(500, 250);
		slTitle.setLength(150);
		slTitle.setCurrentMark(50);

		slTutor.setPosition(500, 150);
		slTutor.setLength(150);
		slTutor.setCurrentMark(100);

		TutorialFontSizeDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		TutorialFontSizeDisplay.setPosition(500, 100);
		TutorialFontSizeNumDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		TutorialFontSizeNumDisplay.setPosition(670, 140);
		BoxTitleFontSizeDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		BoxTitleFontSizeDisplay.setPosition(500, 200);
		BoxTitleFontSizeNumDisplay.setColor(Color.white, Color.black, Color.black, Color.gray);
		BoxTitleFontSizeNumDisplay.setPosition(670, 240);

	}

	// Set Up setting panel run this method and refresh
	// buttons when it is necessary
	// ex=MouseEvents' mouse position x
	// ey=MouseEvents' mouse position y
	public void setUpSettingPanel(int ex, int ey) {

		tfGetBoxWidth.textStateCheck(ex, ey);
		tfGetBoxHeight.textStateCheck(ex, ey);

		// cursor blinking on the textField
		if (tfGetBoxWidth.getState() || tfGetBoxHeight.getState()) {
			timerRun[4] = true;
			if (!timer.isRunning()) {
				ticks = 0;
				timer.start();
			}
		} else {
			timerRun[4] = false;
			if (!timerRun[0] && !timerRun[1] && !timerRun[2] && !timerRun[3])
				timer.stop();
		}

		// Restrict the width and auto reset the values
		if (!tfGetBoxWidth.getState()) {
			int temp = 0;
			if (tfGetBoxWidth.getString().trim() != "")
				temp = Integer.parseInt(tfGetBoxWidth.getString().trim());
			if (temp <= 50)
				tfGetBoxWidth.setString("50");
			if (temp >= 300)
				tfGetBoxWidth.setString("300");
		}

		// Restrict the height and auto reset the values
		if (!tfGetBoxHeight.getState()) {
			int temp = 0;
			if (tfGetBoxHeight.getString().trim() != "")
				temp = Integer.parseInt(tfGetBoxHeight.getString().trim());
			if (temp <= 50)
				tfGetBoxHeight.setString("50");
			if (temp >= 300)
				tfGetBoxHeight.setString("300");
		}

		slTutor.sliderStateCheck(ex, ey);
		slTutor.setSliderState(false);
		slTitle.sliderStateCheck(ex, ey);
		slTitle.setSliderState(false);
	}

	// Pass All the values from each individual buttons to the program
	public void ApplySettingToAll() {
		for (int i = 0; i < boxList.getLength(); i++) {
			boxList.getNode(i).setBoxWidth(Integer.parseInt(tfGetBoxWidth.getString()));
			boxList.getNode(i).setBoxHeight(Integer.parseInt(tfGetBoxHeight.getString()));
		}
		BoxColorWhite = colorOfBoxPanel.getSelectedColor();
		StringColor = colorOfBoxStroke.getSelectedColor();
		FrameHighlighterColor = colorOfFrameHighlighter.getSelectedColor();
		backgroundColor = colorOfFrameBackground.getSelectedColor();
		jframe.setBackground(backgroundColor);
		HoverPanelColor = colorOfHoverHintPanel.getSelectedColor();
		HoverPanelStroke = colorOfHoverHintStroke.getSelectedColor();
		HoverPanelString = colorOfHoverHintString.getSelectedColor();
		tutorialHighlighterColor = colorOfTutorialString.getSelectedColor();
		TutorialFontSize = (int) (slTutor.getPercentage() * 15) + 10;
		BoxTitleSize = (int) (slTitle.getPercentage() * 30) + 10;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// Reset some values when releasing buttons
	@Override
	public void mouseReleased(MouseEvent e) {
		if (right1menuButtonClicked[3]) {
			// for button and slider release
			slTutor.setSliderState(false);
			slTitle.setSliderState(false);
			TutorialFontSize = (int) (slTutor.getPercentage() * 15) + 10;
			BoxTitleSize = (int) (slTitle.getPercentage() * 30) + 10;

			BoxTitleFontSizeNumDisplay.setString(BoxTitleSize + "");
			TutorialFontSizeNumDisplay.setString(TutorialFontSize + "");
			renderPanelRepaintBoolean = true;
		}
		PanelMoving = false;
		if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
				&& !right1menuButtonClicked[3]) {
			for (int i = 0; i < currentBoxCreated; i++) {
				boxList.getNode(i).setMouseDragBoxCondition(false);
			}
			renderPanelRepaintBoolean = true;
		}
		// Check if it requires to refresh
		if (renderPanelRepaintBoolean) {
			renderPanel.repaint();
			renderPanelRepaintBoolean = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		hint.setText(" *Hint: Mouse Entered!");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		hint.setText(" *Hint: Mouse Exited");
		boxHoverID = -1;
	}

	/*******************************
	 ***** TIMER PERFORMANCE *****
	 *******************************/
	@Override
	public void actionPerformed(ActionEvent e) {
		// timer for the textField button cursor refreshing ticks
		if (timerRun[4])
			ticks++;

		// ---------Default panel--------//
		// ---------Main Box Panel-------//
		if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
				&& !right1menuButtonClicked[3]) {
			if (timerRun[3]) {
				hoverRemainingTicks += 1;
				if (hoverRemainingTicks >= hoverTicksTime)
					hoverRemainingTicks = hoverTicksTime;
				if (DoubleEnterOrMore)
					hoverRemainingTicks = 0;
				hint.setText(hoverRemainingTicks + "");
			}

			// Once entering typing mode
			TextPanelX = (jframe.getWidth() - MaxTextPanelWidth) / 2;
			TextPanelY = (jframe.getHeight() - MaxTextPanelHeight) / 2;
			if (DoubleEnterOrMore && timerRun[2]) {
				// --------Close panel Animation
				if (panelClosingAction) {
					TimerTick++;
					if (TimerTick % 2 == 0)
						transparencyOfTextPanel -= 1;
					if (transparencyOfTextPanel <= 0)
						transparencyOfTextPanel = 0;

					// Check Panel position related to the selected box
					// Orientation will decides whether to increment or
					// decrement the values and how panel is animated
					if (!PanelAtLeft && !PanelAtRight && !PanelAlignedHorizon) {
						if (TextPanelX < boxList.getNode(EnterMainIDSelected).getBoxX()) {
							PanelAtLeft = true;
						} else if (TextPanelX > boxList.getNode(EnterMainIDSelected).getBoxX()) {
							PanelAtRight = true;
						} else
							PanelAlignedHorizon = true;

						if (TextPanelY > boxList.getNode(EnterMainIDSelected).getBoxY()) {
							PanelAtTop = true;
						} else if (TextPanelY < boxList.getNode(EnterMainIDSelected).getBoxY()) {
							PanelAtBot = true;
						} else
							PanelAlignedVertical = true;
					}

					if (PanelAtRight) {
						TextPanelMoveX -= Math.abs(TextPanelX - boxList.getNode(EnterMainIDSelected).getBoxX()) / times;
						if (TextPanelMoveX <= boxList.getNode(EnterMainIDSelected).getBoxX()) {
							PanelAtRight = false;
							TextPanelMoveX = boxList.getNode(EnterMainIDSelected).getBoxX();
						}
					} else if (PanelAtLeft) {
						TextPanelMoveX += Math.abs(TextPanelX - boxList.getNode(EnterMainIDSelected).getBoxX()) / times;
						if (TextPanelMoveX >= boxList.getNode(EnterMainIDSelected).getBoxX()) {
							PanelAtLeft = false;
							TextPanelMoveX = boxList.getNode(EnterMainIDSelected).getBoxX();
						}
					} else if (PanelAlignedHorizon) {
						PanelAlignedHorizon = false;
						TextPanelMoveX = boxList.getNode(EnterMainIDSelected).getBoxX();
					}

					if (PanelAtTop) {
						TextPanelMoveY -= Math.abs(TextPanelY - boxList.getNode(EnterMainIDSelected).getBoxY()) / times;
						if (TextPanelMoveY <= boxList.getNode(EnterMainIDSelected).getBoxY()) {
							PanelAtTop = false;
							TextPanelMoveY = boxList.getNode(EnterMainIDSelected).getBoxY();
						}
					} else if (PanelAtBot) {
						TextPanelMoveY += Math.abs(TextPanelY - boxList.getNode(EnterMainIDSelected).getBoxY()) / times;
						if (TextPanelMoveY >= boxList.getNode(EnterMainIDSelected).getBoxY()) {
							PanelAtBot = false;
							TextPanelMoveY = boxList.getNode(EnterMainIDSelected).getBoxY();
						}
					} else if (PanelAlignedVertical) {
						PanelAlignedVertical = false;
						TextPanelMoveY = boxList.getNode(EnterMainIDSelected).getBoxY();
					}

					TextPanelWidth -= MaxTextPanelWidth / times;

					TextPanelHeight -= MaxTextPanelHeight / times;

					TitleHeight -= MaxTitleHeight / times;

					if (TitleHeight <= 0)
						TitleHeight = 0;
					if (TextPanelWidth <= 0)
						TextPanelWidth = 0;
					if (TextPanelHeight <= 0)
						TextPanelHeight = 0;

					// Reset everything to initial statement
					if ((TextPanelWidth == 0 && TextPanelHeight == 0) || transparencyOfTextPanel == 0) {
						EnterMainIDSelected = -1;
						DoubleEnterOrMore = false;
						PanelExtendingAnimationPreset = false;
						panelClosingAction = false;
						TypingDecision = false;
						PanelAtRight = false;
						PanelAtLeft = false;
						PanelAtTop = false;
						PanelAtBot = false;
						PanelAlignedHorizon = false;
						PanelAlignedVertical = false;
						TextPanelHeight = 0;
						TextPanelWidth = 0;
						TitleHeight = 0;
						arcTextPanel = 0;
						CursourX = 0;
						CursourY = -100;
						characterNum = -1;
						TextPanelX = 0;
						TextPanelY = 0;
						TextPanelMoveX = -1;
						TextPanelMoveY = -1;
						EnterCounter = 0;
						title = "";

						timerRun[2] = false;
						if (!timerRun[0] && !timerRun[1] && !timerRun[2] && timerRun[3] && timer.isRunning())
							timer.stop();
					}
				}
				// --------Open panel Animation
				else {
					if (!TypingDecision) {

						TimerTick++;
						if (TimerTick % 2 == 0)
							transparencyOfTextPanel += 1;
						if (transparencyOfTextPanel >= 7)
							transparencyOfTextPanel = 7;

						noAnimationHappen = false;
						if (PanelExtendingAnimationPreset) {
							TextPanelMoveX = boxList.getNode(EnterMainIDSelected).getBoxX();
							TextPanelMoveY = boxList.getNode(EnterMainIDSelected).getBoxY();

							if (TextPanelX <= TextPanelMoveX) {
								PanelAtLeft = true;
							} else if (TextPanelX > TextPanelMoveX) {
								PanelAtRight = true;
							}

							if (TextPanelY >= TextPanelMoveY) {
								PanelAtTop = true;
							} else if (TextPanelY < TextPanelMoveY) {
								PanelAtBot = true;
							}
							PanelExtendingAnimationPreset = false;
						}

						if (PanelAtRight) {
							noAnimationHappen = false;
							TextPanelMoveX += Math.abs(TextPanelX - boxList.getNode(EnterMainIDSelected).getBoxX())
									/ times;
							if (TextPanelMoveX >= TextPanelX
									|| (Math.abs(TextPanelX - boxList.getNode(EnterMainIDSelected).getBoxX())
											/ times == 0)) {
								noAnimationHappen = true;
								TextPanelMoveX = TextPanelX;
							}
						} else if (PanelAtLeft) {
							noAnimationHappen = false;
							TextPanelMoveX -= Math.abs(TextPanelX - boxList.getNode(EnterMainIDSelected).getBoxX())
									/ times;
							if (TextPanelMoveX <= TextPanelX
									|| (Math.abs(TextPanelX - boxList.getNode(EnterMainIDSelected).getBoxX())
											/ times == 0)) {
								noAnimationHappen = true;
								TextPanelMoveX = TextPanelX;
							}
						}

						if (PanelAtTop) {
							noAnimationHappen = false;
							TextPanelMoveY += Math.abs(TextPanelY - boxList.getNode(EnterMainIDSelected).getBoxY())
									/ times;
							if (TextPanelMoveY >= TextPanelY
									|| (Math.abs(TextPanelY - boxList.getNode(EnterMainIDSelected).getBoxY())
											/ times == 0)) {
								noAnimationHappen = true;
								TextPanelMoveY = TextPanelY;
							}
						} else if (PanelAtBot) {
							noAnimationHappen = false;
							TextPanelMoveY -= Math.abs(TextPanelY - boxList.getNode(EnterMainIDSelected).getBoxY())
									/ times;
							if (TextPanelMoveY <= TextPanelY
									|| (Math.abs(TextPanelY - boxList.getNode(EnterMainIDSelected).getBoxY())
											/ times == 0)) {
								noAnimationHappen = true;
								TextPanelMoveY = TextPanelY;
							}
						}

						if (TextPanelWidth == 0)
							TextPanelWidth = boxList.getNode(EnterMainIDSelected).getBoxWidth();
						if (TextPanelHeight == 0)
							TextPanelHeight = boxList.getNode(EnterMainIDSelected).getBoxHeight();

						TextPanelWidth += MaxTextPanelWidth / times;
						TextPanelHeight += MaxTextPanelHeight / times;

						if (TitleHeight < boxList.getNode(EnterMainIDSelected).getBoxHeight()) {
							TitleHeight = MaxTitleHeight;
							noAnimationHappen = true;
						} else
							noAnimationHappen = false;
						if (TextPanelWidth >= MaxTextPanelWidth) {
							TextPanelWidth = MaxTextPanelWidth;
							noAnimationHappen = true;
						} else
							noAnimationHappen = false;
						if (TextPanelHeight >= MaxTextPanelHeight) {
							arcTextPanel += 1;
							TextPanelHeight = MaxTextPanelHeight;
						}

						TitleHeight = boxList.getNode(EnterMainIDSelected).getBoxHeight();
						TitleHeight += MaxTitleHeight / times;
						if (TitleHeight >= MaxTitleHeight) {
							TitleHeight = MaxTitleHeight;
							noAnimationHappen = true;
						} else
							noAnimationHappen = false;

						if (arcTextPanel >= MaxarcTextPanel) {
							arcTextPanel = MaxarcTextPanel;
							noAnimationHappen = true;
						} else
							noAnimationHappen = false;

						// ------- noAnimationHappen------//
						// this variable control when the panel can be typed in
						// the value
						// it will make typing to true until
						// All the animation is finished
						if (noAnimationHappen) {

							TypingDecision = true;

						}

					}

					// ----------Typing Mode--------//
					if (TypingDecision) {
						TextPanelMoveX = TextPanelX;
						TextPanelMoveY = TextPanelY;
						CursourX = TextPanelX + indent;
						CursourY = TextPanelY + indent + CursourSpace * 2;

						if (bodyTyping)
							title = boxList.getNode(EnterMainIDSelected).getBody();
						else
							title = boxList.getNode(EnterMainIDSelected).getTitle();

						// ticks for the cursor blinking
						TimerTick++;
						if (TimerTick % 20 == 0) {
							if (CursourBlink)
								CursourBlink = false;
							else
								CursourBlink = true;
						}
						if (typing) {
							typing = false;
						}
					}
				}
			}

			// -------Panel Reset Animation----- //
			// Happens when pressed space button
			// speed up by hold down the shift
			if (ResetEffects) {
				double changingRate = 0.001;
				int PanelResetRate = 1;
				hint.setText(" *Hint: Panel Reseting!");
				if (zoomPercentage < 1) {
					if (ShiftHold) {
						zoomPercentage += changingRate * 5;
					} else
						zoomPercentage += changingRate;
				} else {
					if (ShiftHold) {
						zoomPercentage -= changingRate * 5;
					} else
						zoomPercentage -= changingRate;
				}

				// Shift hold is for speeding up
				if (ShiftHold) {
					PanelResetRate = 5;
				} else
					PanelResetRate = 1;
				if (PanelX > 0)
					PanelX -= PanelResetRate;
				else if (PanelX == 0)
					PanelX = 0;
				else
					PanelX += PanelResetRate;
				if (PanelY > 0)
					PanelY -= PanelResetRate;
				else if (PanelY == 0)
					PanelY = 0;
				else
					PanelY += PanelResetRate;

				if (zoomPercentage >= (1 - changingRate * 2) && zoomPercentage <= (1 + changingRate * 2)) {
					zoomPercentage = 1;
				}

				if (zoomPercentage >= (1 - changingRate * 2) && zoomPercentage <= (1 + changingRate * 2) && PanelX == 0
						&& PanelY == 0) {
					timerRun[0] = false;
					if (!timerRun[0] && !timerRun[1] && !timerRun[2] && timerRun[3] && timer.isRunning())
						timer.stop();
					hint.setText(" *Hint: Reseting finished!");
					Reset = false;
					ResetEffects = false;
				}
			}
			renderPanelRepaintBoolean = true;
		}

		// Menu Expanding Animation
		if (mouseHoverCircle) {
			int turns = MaxLength / 10;
			arcs += Math.PI / 2 / turns;
			xc += 10;
			hoverTick += 10;
			if (xc >= MaxLength) {
				xc = MaxLength;
				MenuAppear = true;
				arcs = Math.PI / 2;
			}
			renderPanelRepaintBoolean = true;
		}

		// Animation of Menu ICON
		// Animation of hovering Each Menu ICON

		////////////////////////////////////////
		// ORDER OF MENU BUTTONS:
		// TEXT OUTPUT = right1menuButtonClicked[0]
		// SAVE = right1menuButtonClicked [1]
		// LOAD = right1menuButtonClicked [2]
		// SETTINGS = right1menuButtonClicked [3]
		////////////////////////////////////////
		if (right1menuButtonHovered[3] && MenuAppear) {
			rotationAnimation += 3;
			if (rotationAnimation == 360) {
				rotationAnimation = 360;
			}
			renderPanelRepaintBoolean = true;
		}
		if (right1menuButtonHovered[1] && MenuAppear) {
			arrowDown += 4;
			if (arrowDown >= 0) {
				arrowDown = -200;
			}
			renderPanelRepaintBoolean = true;
		} else {
			// Return to the initial state
			arrowDown = 0;
			renderPanelRepaintBoolean = true;
		}
		if (right1menuButtonHovered[2] && MenuAppear) {
			arrowUp -= 4;
			if (arrowUp <= -200) {
				arrowUp = 0;
			}
			renderPanelRepaintBoolean = true;
		} else {
			// Return to the initial state
			arrowUp = 0;
			renderPanelRepaintBoolean = true;
		}
		if (right1menuButtonHovered[0] && MenuAppear) {
			if (hoverTick % 100 == 0) {
				if (!control_Increase) {
					if (lineNum <= 0) {
						control_Increase = true;
					} else
						lineNum -= 1;
				}
				if (control_Increase) {
					if (lineNum >= 3)
						control_Increase = false;
					else
						lineNum += 1;
				}
			}
			renderPanelRepaintBoolean = true;
		} else {
			// Return to the initial state
			lineNum = 3;
			renderPanelRepaintBoolean = true;
		}

		// Automatically Contracting
		if (!mouseHoverCircle && xc != 0) {
			MenuAppear = false;
			int turns = MaxLength / 5;
			arcs -= Math.PI / 2 / turns;
			xc -= 5;
			if (xc <= 0) {
				xc = 0;
				arcs = 0;

				timerRun[1] = false;
				if (!timerRun[0] && !timerRun[1] && !timerRun[2] && timerRun[3] && timer.isRunning())
					timer.stop();
				// stop timer in order to avoid useless memory
				// running
			}
			renderPanelRepaintBoolean = true;
		}

		// Check if it requires to refresh
		if (renderPanelRepaintBoolean) {
			renderPanel.repaint();
			renderPanelRepaintBoolean = false;
		}

	}

	////////////////////////////////////////
	// ORDER OF MENU BUTTONS:
	// TEXT OUTPUT = right1menuButtonClicked[0]
	// SAVE = right1menuButtonClicked [1]
	// LOAD = right1menuButtonClicked [2]
	// SETTINGS = right1menuButtonClicked [3]
	////////////////////////////////////////
	@Override
	public void keyTyped(KeyEvent e) {
		// Setting Mode of typing
		if (right1menuButtonClicked[3]) {
			char cr = e.getKeyChar();
			// Only accept numbers
			if (cr == '0' || cr == '1' || cr == '2' || cr == '3' || cr == '4' || cr == '5' || cr == '6' || cr == '7'
					|| cr == '8' || cr == '9' || cr == KeyEvent.VK_BACK_SPACE) {
				tfGetBoxWidth.key(e);
				tfGetBoxHeight.key(e);

			}

		}

		// save name, name editor
		if (right1menuButtonClicked[1]) {
			nameOfTheFile.key(e);// TextField has its own text detector
		}

		// Handling typing keys during waiting time
		if (DoubleEnterOrMore && !TypingDecision) {
			char tempKeyHandler = e.getKeyChar();
		}

		// ----Main Panel----//
		if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
				&& !right1menuButtonClicked[3]) {
			if (DoubleEnterOrMore && TypingDecision) {
				// show the character count
				if (bodyTyping)
					hint.setText("* Character Counter: " + boxList.getNode(boxSelectedID).getBody().length() + "");
				else
					hint.setText("* Character Counter: " + boxList.getNode(boxSelectedID).getTitle().length() + "");

				character = e.getKeyChar();
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					character = ' ';// Convert enter into space
				}

				// User Typed
				// It will be set back to false after paint out on screen
				typing = true;

				if (character == KeyEvent.VK_BACK_SPACE) {
					// make sure there is always one char at left of the cursor
					// to
					// be deleted
					if (title.length() >= 1 && cursourTrim < title.length()) {
						deleteChar = true;
						characterNum -= 1;
						if (cursourTrim == 0) {
							title = title.substring(0, title.length() - 1);
						} else {
							String Temp = title;
							title = Temp.substring(0, Temp.length() - 1 - cursourTrim)
									+ Temp.substring(Temp.length() - cursourTrim);
						}
					}
				} else if ((!titleTextFull && !bodyTyping) || (!bodyTextFull && bodyTyping)) {
					//if not deleting
					//add character after the string
					deleteChar = false;
					characterNum += 1;
					if (cursourTrim == 0) {
						title += character;
					} else {
						String Temp = title;
						title = Temp.substring(0, Temp.length() - cursourTrim) + character
								+ Temp.substring(Temp.length() - cursourTrim);
					}
				}
				
				//Show Warning If enter the max letters;
				//It depends on the minimum size of the font
				//so it may vary
				if (titleTextFull || bodyTextFull) {
					if (bodyTyping)
						hint.setText("*Caution: Maximum words! Body Paragraph");
					else
						hint.setText("Caution: Maximum words! Title");
				}

				//change the title and description While typing. 
				if (!bodyTyping)
					boxList.getNode(boxSelectedID).setTitle(title);
				else
					boxList.getNode(boxSelectedID).setBody(title);
				renderPanelRepaintBoolean = true;
			}
			if (EnterCheck && !DoubleEnterOrMore && !timerRun[2] && EnterMainIDSelected != -1) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					EnterCounter++;
					if (EnterCounter >= EnterNumberToActive) {
						DoubleEnterOrMore = true;
						transparencyOfTextPanel = 0;
						TextPanelMoveX = boxList.getNode(boxSelectedID).getBoxX();
						TextPanelMoveY = boxList.getNode(boxSelectedID).getBoxX();
						title = boxList.getNode(boxSelectedID).getTitle();

						timerRun[2] = true;
						if (timerRun[0] || timerRun[1] || timerRun[2] || timerRun[3] && !timer.isRunning())
							timer.start();
					}
					hint.setText(" *Hint: Enter typed times: " + EnterCounter);
					renderPanelRepaintBoolean = true;
				}
				if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					if (boxList.getLength() != 1) {
						hint.setText("Hint: DELETING");
						boxList.deleteNode(ShiftMainIDSelected);
						currentBoxCreated--;
						boxSelectedID = -1;
						clickInBoxCounter = -1;
						currentBoxNumberSelected = 0;
						mouseHoverBoxWithPanelID = -1;
						// Store the shift main ID
						ShiftMainIDSelected = boxSelectedID;
						EnterMainIDSelected = boxSelectedID;

						boxHoverID = -1;
					}
					renderPanelRepaintBoolean = true;
				}
			}
		}
		// Check if it requires to refresh
		if (renderPanelRepaintBoolean) {
			renderPanel.repaint();
			renderPanelRepaintBoolean = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
				&& !right1menuButtonClicked[3]) {
			if (!DoubleEnterOrMore) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_SHIFT && PanelSelected)// Move Panel
					PanelMovingDecision = true;
				if (key == KeyEvent.VK_SHIFT) {// Shift hold
					hint.setText(" *Hint: Shift Holded");
					ShiftHold = true;

					clickInBoxCounter = 1;
					renderPanelRepaintBoolean = true;

				} else if (key == KeyEvent.VK_SPACE) {// space
					hint.setText(" *Hint: Space pressed");
					Reset = true;
					ResetEffects = true;
					if (ResetEffects)
						timerRun[0] = true;
					if (timerRun[0] || timerRun[1] || timerRun[2] || timerRun[3] && !timer.isRunning())
						timer.start();
				}

				renderPanelRepaintBoolean = true;
			} else {
				int key = e.getKeyCode();
				if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP
						|| key == KeyEvent.VK_DOWN) && !DirectionKey) {
					DirectionKey = true;
					// Make sure that title copies the correct one we are
					// editing on
				}
				if (DirectionKey) {
					if (key == KeyEvent.VK_LEFT) {
						if (title.length() > 0 && cursourTrim < title.length()) {
							hint.setText(" *Hint: Left: ");
							cursourTrim += 1;
						}
					} else if (key == KeyEvent.VK_RIGHT) {
						if (cursourTrim > 0 && cursourTrim <= title.length()) {
							hint.setText(" *Hint: Right: ");
							cursourTrim -= 1;
						}
					} else if (key == KeyEvent.VK_UP) {
						if (bodyTyping) {

							if (cursourTrim >= boxList.getNode(boxSelectedID).getTitle().length())
								cursourTrim = boxList.getNode(boxSelectedID).getTitle().length();
							else if (cursourTrim <= 0)
								cursourTrim = 0;
							bodyTyping = false;
							title = boxList.getNode(boxSelectedID).getTitle();

						}
					} else if (key == KeyEvent.VK_DOWN) {
						if (!bodyTyping) {
							if (cursourTrim >= boxList.getNode(boxSelectedID).getBody().length())
								cursourTrim = boxList.getNode(boxSelectedID).getBody().length();
							else if (cursourTrim <= 0)
								cursourTrim = 0;
							bodyTyping = true;
							title = boxList.getNode(boxSelectedID).getBody();
						}
					}
					DirectionKey = false;
				}
			}
		}
		// Check if it requires to refresh
		if (renderPanelRepaintBoolean) {
			renderPanel.repaint();
			renderPanelRepaintBoolean = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
				&& !right1menuButtonClicked[3]) {
			if (!DoubleEnterOrMore) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_SHIFT) {
					PanelMovingDecision = false;
				}
				if (key == KeyEvent.VK_SHIFT) {
					ShiftHold = false;
					hint.setText(" *Hint: Shift released");
					ShiftMainIDSelected = -1;
					clickInBoxCounter = 0;
					renderPanelRepaintBoolean = true;
				}
			}
		}
		// Check if it requires to refresh
		if (renderPanelRepaintBoolean) {
			renderPanel.repaint();
			renderPanelRepaintBoolean = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!right1menuButtonClicked[0] && !right1menuButtonClicked[1] && !right1menuButtonClicked[2]
				&& !right1menuButtonClicked[3]) {
			if (!Reset && !DoubleEnterOrMore) {
				rotation = e.getUnitsToScroll();
				if (rotation > 0) {
					hint.setText(" *Hint: Mouse Scrolling! zoom in");
				} else
					hint.setText(" *Hint: Mouse Scrolling! zoom out");
				if (ShiftHold) {
					zoomPercentage += rotation / scrollingSensitivity * 100;
				} else
					zoomPercentage += rotation / scrollingSensitivity;
				renderPanelRepaintBoolean = true;
			}
		}else if (right1menuButtonClicked[0]) {
			System.out.println("JFrame height: " + (jframe.getHeight() - 57));
			System.out.println("Length of List: "
					+ textOutput.lineList.getLength() * textOutput.lineList.getNode(0).getLineHeight());
			if ((jframe.getHeight() - 57) < textOutput.lineList.getLength()
					* textOutput.lineList.getNode(0).getLineHeight()) {

				// The Line object that is at the bottom of the LineList
				Line bottomLine = new Line();
				bottomLine = textOutput.lineList.getNode(textOutput.lineList.getLength() - 1);

				scrollYTextOutput += e.getUnitsToScroll() * scrollSensitisityTextOutput;

				// Restricts list from moving down and disappearing
				if (scrollYTextOutput > 0)
					scrollYTextOutput = 0;

				// Restricts list from going up and disappearing
				else if ((bottomLine.getY() + bottomLine.lineHeight + scrollYTextOutput) < jframe.getHeight() - 57) {
					scrollYTextOutput = (jframe.getHeight() - 57) - bottomLine.getY() - bottomLine.lineHeight;
				}
				renderPanel.repaint();
			}
		}
		if (right1menuButtonClicked[2]) {
			readFilesSelector.checkScroll(e);
			renderPanelRepaintBoolean = true;

		}
		// Check if it requires to refresh
		if (renderPanelRepaintBoolean) {
			renderPanel.repaint();
			renderPanelRepaintBoolean = false;
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		MasterCircleX = jframe.getWidth() - 100;// MENUCIRCLE POSITION
		MasterCircleY = jframe.getHeight() - 125;// MENUCIRCLE POSITION
		renderPanelRepaintBoolean = true;
		saveAndLoadButtonsSetUp();
		// Check if it requires to refresh
		if (renderPanelRepaintBoolean) {
			renderPanel.repaint();
			renderPanelRepaintBoolean = false;
		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
