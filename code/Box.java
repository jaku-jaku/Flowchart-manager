
///////////////////////////////////////////////////////
//Box: Hold the info particular to each graphical Box//
///////////////////////////////////////////////////////

import java.awt.Dimension;
import java.awt.Point;
import java.util.*;

public class Box {
	//////////////////////////////////////////////
	///////// Identity of each box & Links////////
	//////////////////////////////////////////////

	// Memory address of Box it is connected to (in memory)
	private Box NextBox;
	// Holds the objects it connects to (graphically)
	private List<Integer> linkTo;
	private int ID, isConnectedBy;
	private boolean ifThisBoxIsTheMain0;
	private String Title, Body;
	//////////////////////////////////////////////

	//////////////////////////////////////////////
	////////////// Graphics Info//////////////////
	//////////////////////////////////////////////
	public int boxX, boxY, boxHeight, boxWidth;
	public boolean mouseDraggedBox;

	//////////////////////////////////////////////
	/////////// Identity Algorithms Below:////////
	//////////////////////////////////////////////
	public Box() {
		NextBox = null;// DEFAULT SETTING FOR THE NEXT ONE MADE AFTER THIS
		ifThisBoxIsTheMain0 = true;// default set all boxes as main Box
		ID = -1;// DEFAULT SETTING FOR THE ID IN THE LIST//-1 MEANS IT HAS NOT
		isConnectedBy = -1;// NOT CONNECTED TO THE LIST YET
		Title = "";
		Body = "";
		linkTo = new ArrayList<Integer>();// ARRAYLIST FOR THE GRAPHICAL
											// CONNECTIONS THAT BOX CONTAINS
		mouseDraggedBox = false;// DEFAULT BOOLEAN TO STATE IF THE MOUSE IS
								// HOVERING OVER THE OBJECT VISUALLY
		boxHeight = 100;// BOX HEIGHT
		boxWidth = 200;// BOX WIDTH
		boxX = 0;// BOX X-COORDINATE
		boxY = 0;// BOX Y-COORDINATE
	}// SET DEFAULT BOX

	public Box(String m) {
		NextBox = null;// DEFAULT SETTING FOR THE NEXT ONE MADE AFTER THIS
		ifThisBoxIsTheMain0 = true;// default set all boxes as main
		ID = -1;// DEFAULT SETTING FOR THE ID IN THE LIST//-1 MEANS IT HAS NOT
		isConnectedBy = -1;
		// CONNECT TO THE LIST YET
		Title = m;// DEFUALT BOX TITLE
		Body = "";
		linkTo = new ArrayList<Integer>();// ARRAYLIST FOR THE CONNECTION THAT
											// BOX CONTAINS
		mouseDraggedBox = false;// DEFAULT BOOLEAN TO STATE IF THE MOUSE IS
								// HOVERING OVER THE OBJECT VISUALLY
		boxHeight = 100;// BOX HEIGHT
		boxWidth = 100;// BOX WIDTH
		boxX = 0;// BOX X-COORDINATE
		boxY = 0;// BOX Y-COORDINATE
	}// SET DEFAULT BOX

	public void setIfThisBoxIsTheMain0(boolean decision) {
		ifThisBoxIsTheMain0 = decision;
	}

	public void setIsConnectedBy(int id) {
		isConnectedBy = id;
	}

	// Adds
	public void addLink(int iD, BoxList checkList) {
		if (iD == 0 && iD != ID) {
			this.setIsConnectedBy(-1);
			this.deleteLink(0, checkList);
			checkList.getNode(iD).setIsConnectedBy(-1);
			checkList.moveNodeToTheFront(this);
			this.addLink(1, checkList);
			checkList.getNode(1).setIsConnectedBy(0);
		} else if (iD > 0) {
			/// Error Trapping:If trying to connect to the one
			// it was connected by direction of the connection will change
			if (this.isConnectedBy == iD) {
				this.setIsConnectedBy(-1);
				checkList.getNode(iD).deleteLink(this.ID, checkList);
				addLink(iD, checkList);
			} else {
				boolean addornot = true;
				for (int ii = 0; ii < this.getList().length; ii++) {
					// If the ID of the object already exists, do not add it
					if (iD == this.getList()[ii])
						addornot = false;
				}
				// Error Trapping: Prevents a box from connecting with itself
				if (iD == this.ID)
					addornot = false;

				//////// LASER BEAM DANGER!!!!+=============
				if (addornot) {
					linkTo.add(iD);
					if (checkList.getNode(iD).getIsConnectedBy() != -1)
						checkList.getNode(checkList.getNode(iD).getIsConnectedBy()).deleteLink(iD, checkList);
					checkList.getNode(iD).setIsConnectedBy(this.ID);
				}
			}
		}
	}

	public void EmptyTheListOfConnection() {
		if (!linkTo.isEmpty())
			linkTo.clear();
	}

	// ADD LINK (INPUT/PARAMETER IS THE ID OF THE
	// OBJECT THAT CONNECTS TO THIS
	// OBJECT)
	public void addLinkCopyOnly(int i) {
		if (i >= 0) {
			boolean addornot = true;
			for (int ii = 0; ii < this.getList().length; ii++) {
				if (i == this.getList()[ii])
					addornot = false;
			}
			// Error Trapping: Prevents a box from connecting with itself
			if (i == this.ID)
				addornot = false;
			if (addornot)
				linkTo.add(i);
		}
	}

	// Delete a graphical connection
	public void deleteLink(int i, BoxList list) {
		boolean delete = false;
		for (int ii = 0; ii < this.getList().length; ii++) {
			if (this.getList()[ii] == i)
				delete = true;
		}
		if (delete) {
			list.getNode(i).setIsConnectedBy(-1);
			linkTo.remove(linkTo.indexOf(i));
		}
	}

	// Returns an array of the Box objects it is connecting to
	public int[] getList() {
		return convertToArray(linkTo);
	}

	// Converts an Array List into an Array
	public static int[] convertToArray(List<Integer> integers) {
		// Makes a new array the same length as the Array List
		int[] result = new int[integers.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = integers.get(i).intValue();
		}
		return result;
	}

	// Returns the ID of the object to which it is connected
	// If it is not connected to anything, returns -1
	public int getIsConnectedBy() {
		return isConnectedBy;
	}

	// toString for general info. For debugging only
	public String toString() {
		String sentence = "";
		sentence += "MAIN: " + ifThisBoxIsTheMain0 + ", ID: " + ID + " , Title: " + Title + ", Connectby:"
				+ isConnectedBy + " , Connection: ";
		for (int i = 0; i < this.getList().length; i++) {
			sentence += this.getList()[i] + " ,";
		}
		return sentence;
	}

	///////////////////////////////////////////
	///////// Getter and Setter Methods////////
	////// for Memory (not graphical) info/////
	///////////////////////////////////////////

	// Get the next object in the BoxList
	public Box getNext() {
		return NextBox;
	}

	// Sets the next object in the BoxList
	// (The object it connects to in memory)
	public void setNext(Box obj) {
		NextBox = obj;
	}

	// ID of a box is its position on the BoxList
	public int getID() {
		return ID;
	}

	public void setID(int i) {
		ID = i;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String Title) {
		this.Title = Title.toUpperCase();
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String Body) {
		this.Body = Body;
	}
	///////////////////////////////////////////
	///////////////////////////////////////////

	///////////////////////////////////////////
	///////// Getter and Setter Methods////////
	//////////// for Graphical info////////////
	///////////////////////////////////////////

	// Setter Methods
	// sets x-coordinate of box
	public void setBoxX(int boxX) {
		this.boxX = boxX;
	}

	// sets y-coordinate of box
	public void setBoxY(int boxY) {
		this.boxY = boxY;
	}

	//// sets x & y coordinates of box
	public void setBoxPosition(int boxX, int boxY) {
		this.boxX = boxX;
		this.boxY = boxY;
	}// SET BOX COORDINATE

	public void setBoxHeight(int height) {
		boxHeight = height;
	}

	public void setBoxWidth(int width) {
		boxWidth = width;
	}

	// Sets both height and width of box
	public void setBoxSize(int height, int width) {
		boxHeight = height;
		boxWidth = width;
	}

	// Sets coordinates and size of box
	public void setBox(int boxX, int boxY, int height, int width) {
		this.boxX = boxX;
		this.boxY = boxY;
		boxHeight = height;
		boxWidth = width;
	}

	// Sets condition of the relationship between the mouse and the box
	// Used to determine whether the mouse is hovering over box or not
	public void setMouseDragBoxCondition(boolean TF) {
		mouseDraggedBox = TF;
	}

	// Getter Methods
	public int getBoxX() {
		return boxX;
	}

	public int getBoxY() {
		return boxY;
	}

	public int getBoxWidth() {
		return boxWidth;
	}

	public int getBoxHeight() {
		return boxHeight;
	}

	public Point boxPoint() {
		return new Point(boxX, boxY);
	}

	public Dimension boxDimension() {
		return new Dimension(boxWidth, boxHeight);
	}

	public boolean getMouseDragBoxCondition() {
		return mouseDraggedBox;
	}

	////////////////////////////////////////////////
	////////////////////////////////////////////////

}
