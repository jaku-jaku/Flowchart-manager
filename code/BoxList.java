//////////////////////////////////////////
//Connects all the Box Objects in memory//
//////////////////////////////////////////
public class BoxList {
	protected Box top;

	public BoxList() {
		top = null;
	}

	//////
	public int getLength() {
		Box current = top;
		int count = 0;
		while (current != null) {
			count++;
			current = current.getNext();
		}
		return count;
	}
	///////////

	public void clearList() {
		top = null;
	}

	public void moveNodeToTheFront(Box lpr) {
		// ReArrange the ID order
		int temp = lpr.getID();
		this.getNode(temp).setID(-1);
		for (int i = temp - 1; i >= 0; i--) {
			this.getNode(i).setID(i + 1);
		}
		this.getNode(-1).setID(0);
		this.getNode(0).setIfThisBoxIsTheMain0(true);
		this.getNode(1).setIfThisBoxIsTheMain0(false);

		Box current = top, next = lpr.getNext();
		top = lpr;
		lpr.setNext(current);
		this.getNode(temp).setNext(next);

		for (int i = 1; i < this.getLength(); i++) {
			// clean connection & create new connection datas
			int[] connection = this.getNode(i).getList();
			int[] newC = new int[connection.length];
			for (int ii = 0; ii < connection.length; ii++) {
				if (connection[ii] == temp || connection[ii] == -1) {

				} else if (connection[ii] < temp) {
					newC[ii] = connection[ii] + 1;
				} else
					newC[ii] = connection[ii];
			}
			this.getNode(i).EmptyTheListOfConnection();

			int cBy = this.getNode(i).getIsConnectedBy();
			if (cBy == temp)
				this.getNode(i).setIsConnectedBy(-1);
			else if (cBy < temp)
				this.getNode(i).setIsConnectedBy(this.getNode(i).getIsConnectedBy() + 1);

			// Create new connection
			for (int ii = 0; ii < newC.length; ii++) {
				this.getNode(i).addLinkCopyOnly(newC[ii]);
			}
			this.getNode(i).deleteLink(0, this);
		}
		// Reset Connection
		this.getNode(0).EmptyTheListOfConnection();
		this.getNode(0).addLinkCopyOnly(1);
		this.getNode(1).setIsConnectedBy(0);

	}

	public void addNode(Box lpr) {
		if (top == null) {
			lpr.setID(0);
			top = lpr;
		} else {
			Box current = top;
			while (current.getNext() != null) {
				current = current.getNext();
			}
			lpr.setID(current.getID() + 1);
			current.setNext(lpr);
			// Set all other to be not main except BOX ID 0
			lpr.setIfThisBoxIsTheMain0(false);
		}
	}

	public void showList() {
		if (top == null) {
			System.out.println("Empty list ");
		} else {
			System.out.println("The current list:");

			Box current;
			current = top;

			while (current != null) {
				System.out.println(current.toString());
				current = current.getNext();
			}
			System.out.println("*******");
		}
	}

	public Box getNode(String name) {
		Box current = top;
		while (!(current == null || current.getTitle().equals(name))) {
			current = current.getNext();
		}
		return current;
	}

	public Box getNode(int ID) {
		Box current = top;
		while (!(current == null || current.getID() == ID)) {
			current = current.getNext();
		}
		return current;
	}

	// Node Delete
	public void deleteNode(int ID) {

		// Clear all connection that this box connects to
		for (int i = 0; i < this.getNode(ID).getList().length; i++)
			this.getNode(this.getNode(ID).getList()[i]).setIsConnectedBy(-1);
		// Clear the connection to this box
		if (this.getNode(ID).getIsConnectedBy() != -1)
			this.getNode(this.getNode(ID).getIsConnectedBy()).deleteLink(ID, this);

		// Delete node, reset the connection After the delete one
		Box current, previous;
		current = top;
		previous = null;
		if (current.getID() == ID) {
			top = current.getNext();
			current = null;
		} else {
			while (!(current == null || current.getID() == ID)) {
				previous = current;
				current = current.getNext();
			}
		}

		while (current != null) {
			current = current.getNext();
			previous.setNext(current);
			previous = previous.getNext();
		}

		// Reset the ID of the new box after shifting
		for (int i = ID; i < this.getLength(); i++) {
			this.getNode(i + 1).setID(i);
		}

		// Re-address the All the connection
		// && Auto-Fix all old connections to the new position
		for (int i = 0; i < this.getLength(); i++) {
			int tempConnectBy = this.getNode(i).getIsConnectedBy();
			int[] tempConnection = this.getNode(i).getList();
			int[] newConnection = new int[tempConnection.length];
			// //Re-address the ID at i's connection
			for (int ii = 0; ii < tempConnection.length; ii++) {
				int tempN = tempConnection[ii];
				if (tempN > ID) {
					newConnection[ii] += tempN - 1;
				} else
					newConnection[ii] += tempN;
			}
			this.getNode(i).EmptyTheListOfConnection();
			for (int ii = 0; ii < newConnection.length; ii++) {
				this.getNode(i).addLinkCopyOnly(newConnection[ii]);
			}
			// Re-address the connection ID of the master box connects to the
			// box
			if (tempConnectBy > ID) {
				this.getNode(i).setIsConnectedBy(tempConnectBy - 1);
			}
		}
		// DELETE END

	}
}
