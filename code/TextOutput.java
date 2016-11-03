///////////////////////////////////////////
//-Outputs the mind map in list form
//-Easier to visualize hierarchy
///////////////////////////////////////////
public class TextOutput {

	public BoxList boxList;
	public int row; // Row of each Line on the list that will be output
	public LineList lineList; // Linked List that holds the Line Objects
	public WorkingSpace workingArea = WorkingSpace.workingArea;

	public TextOutput(BoxList boxList) {
		row = -1; // Sets row to -1 so that first row is 1
		this.boxList = boxList;
	}

	// Recursive method creates Line objects and adds them to a Line linked list
	// in the order in which they will be output
	public void printLines(int ID, int iteration, int max, int indent) {
		row++;
		Line l = new Line();

		//////////// Sets info in each Line object///////////
		l.setRow(row);
		l.setIndentCol(indent);
		l.setID(ID);
		// Error trapping for title
		if (boxList.getNode(ID).getTitle().trim() == "") { // if it does not
															// have a title
			System.out.println("Doesn't have title :(");
			l.setTitle("default: " + ID);
		} else {// if it has a title
			System.out.println("Has title!");
			l.setTitle(boxList.getNode(ID).getTitle());
		}
		//////////////////////////////////////////////////

		lineList.addNode(l); // Adds the newly created line to the Linked List

		while (iteration <= max) { // Are there are other levels further in?
			// Finds the ID of the next Line it should output
			int next = seek(ID, iteration);

			// Finds the number of objects that object connects to
			int nextMax = max(next);

			// Calls itself to keep creating Line objects
			printLines(next, 0, nextMax, indent + 1);

			iteration++;
		}
		// When it finishes creating all the objects in one level
		indent--;
	}

	// Checks for Boxes that are not connected (graphically) in the MindMap
	// And runs paintLines starting with them to access every single Box
	public void checkLoseBoxes() {
		for (int i = 0; i < boxList.getLength(); i++) {
			if (boxList.getNode(i).getID() != 0 && boxList.getNode(i).getIsConnectedBy() == -1) {
				int max = max(boxList.getNode(i).getID());
				printLines(boxList.getNode(i).getID(), 0, max, 0);
			}
		}
	}

	// Returns the ID of the next Line that should be output
	public int seek(int ID, int iteration) {
		int connectedTo[] = boxList.getNode(ID).getList();
		int next = connectedTo[iteration];
		return next;
	}

	// Returns the # of boxes attached to that Box
	public int max(int ID) {
		return (boxList.getNode(ID).getList().length - 1);
	}

	public void setRow(int row) {
		this.row = row;
	}

	// "Deletes" the old Linked List of Line Objects
	// to "refresh" the new list
	public void prepareToOutput() {
		System.out.println("List cleared!");
		lineList = null;
		lineList = new LineList();
		setRow(-1);

	}
}
