
public class LineList {
	protected Line top;

	public LineList() {
		top = null;
	}

	public void addNode(Line l) {
		if (top == null) {
			l.setID(0);
			top = l;
		} else {
			Line current = top;
			while (current.getNext() != null) {
				current = current.getNext();
			}
			l.setID(current.getID() + 1);
			current.setNext(l);
		}
	}

	public Line getNode(int ID) {
		Line current = top;
		while (!(current == null || current.getID() == ID)) {
			current = current.getNext();
		}
		return current;
	}

	public int getLength() {
		Line current = top;
		int count = 0;
		while (current != null) {
			count++;
			current = current.getNext();
		}
		return count;
	}
}