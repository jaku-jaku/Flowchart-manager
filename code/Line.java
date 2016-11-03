///////////////////////////////////////////////////
//Hold the info particular to each graphical Line//
///////////////////////////////////////////////////

public class Line {

	public int lineX, lineY, ID, row, indentCol, indentWidth = 100, lineHeight = 75, lineWidth = 450;
	// Memory address of Line it is connected to (in memory, in the Linked List)
	public Line NextLine; 
	public String Title = "";

	public Line() {
		NextLine = null;
	}

	// Setter Methods

	public void setTitle(String Title) {
		this.Title = Title;
	}

	public void setX(int lineX) {
		this.lineX = lineX;
	}

	public void setY(int lineY) {
		this.lineY = lineY;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setRow(int row) {
		this.row = row;
		setY(row * lineHeight);
	}

	//Sets the column in which it is in
	//Transforms column into an x-value
	public void setIndentCol(int indentCol) {
		this.indentCol = indentCol;
		setX(indentCol * indentWidth);
	}

	//How much is each column indented
	public void setIndentWidth(int indentWidth) {
		this.indentWidth = indentWidth;
	}

	public void setNext(Line NextLine) {
		this.NextLine = NextLine;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	// Getter Methods

	public String getTitle() {
		return this.Title;
	}

	public int getX() {
		return lineX;
	}

	public int getY() {
		return lineY;
	}

	public int getID() {
		return ID;
	}

	public int getRow() {
		return row;
	}

	public int getIndentCol() {
		return indentCol;
	}

	public int getIndentWidth() {
		return indentWidth;
	}

	public Line getNext() {
		return NextLine;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public int getLineWidth() {
		return lineWidth;
	}

}
