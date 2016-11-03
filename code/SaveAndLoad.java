
/////////////////////////////////////////////////////
//////Class for Saving and Loading the MindMap///////
/////////////////////////////////////////////////////

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import javax.imageio.ImageIO;

public class SaveAndLoad {

	// ORDER FOR WRITING/READING ON FILE:
	/*
	 * -"" ---> blank space
	 *
	 * -ID
	 *
	 * -Title
	 *
	 * -Description
	 *
	 * -x coordinate
	 *
	 * -y coordinate
	 *
	 * -Length of array
	 *
	 * -Order of objects (GRAPHICALLY) --> array
	 *
	 ***************************************************
	 */
	public SaveAndLoad() {
	}

	// Takes the file name and object as a parameter
	public void saveMap(String fileName, BoxList list) throws IOException {

		fileName += ".txt";
		PrintWriter file = new PrintWriter(new FileWriter(fileName));
		Box current = list.top;
		while (current != null) {
			file.println("");
			file.println(current.getID()); // ID
			if (current.getTitle().trim() != "") {
				System.out.println("No title!");
				file.println(current.getTitle()); // Title
			} else
				file.println("default");

			if (current.getBody().trim() != "")
				file.println(current.getBody()); // Description
			else
				file.println("Please Add Description");
			file.println(current.getBoxX()); // Box X
			file.println(current.getBoxY()); // Box Y
			int[] connectsTo = current.getList(); // Makes the ArrayList into an
													// array
			file.println(connectsTo.length); // Length of arrays
			for (int n = 0; n < connectsTo.length; n++) {
				file.println(connectsTo[n]);
			}
			current = current.getNext();
		}
		file.close();
	}

	// Takes file name, linked list of Box objects, and the binary log file
	// Converts the info in text file into a Linked List of Box Objects
	void readMap(String fileName, BoxList list, SaveLog sL) throws IOException {
		fileName = fileName.trim() + ".txt";

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			list.clearList();
			while (bufferedReader.readLine() != null) {
				Box newBox = new Box();
				newBox.setID(Integer.parseInt(bufferedReader.readLine())); // ID
				newBox.setTitle(bufferedReader.readLine()); // Title
				newBox.setBody(bufferedReader.readLine()); // Description
				newBox.setBoxX(Integer.parseInt(bufferedReader.readLine())); // BoxX
				newBox.setBoxY(Integer.parseInt(bufferedReader.readLine())); // BoxY
				// Number of Boxes it connects to
				int repeat = Integer.parseInt(bufferedReader.readLine());
				// Repeats "length of the array list" times
				for (int i = 0; i < repeat; i++) {
					newBox.addLinkCopyOnly(Integer.parseInt(bufferedReader.readLine()));
				}
				System.out.println(newBox.getIsConnectedBy());
				// Adds object to the linked list to store it in memory
				list.addNode(newBox);
			}
			bufferedReader.close(); // Closes file

			// Error Trapping
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
			sL.deleteLog(sL.getIndexNumOf(fileName.trim().substring(0, fileName.length() - 4)));
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

		// Resets the "graphical" connections for box
		for (int i = 0; i < list.getLength(); i++) {
			if (list.getNode(i).getList().length != 0) {
				for (int ii = 0; ii < list.getNode(i).getList().length; ii++) {
					list.getNode(list.getNode(i).getList()[ii]).setIsConnectedBy(i);
				}
			}
		}
	}

	// Grabs the buffered image from render panel and saves it as a .jpg file
	void saveMapAsImage(String fileName, BufferedImage image) throws IOException {
		while (fileName.indexOf(fileName.length() - 1) == '.') {
			fileName.substring(0, fileName.length() - 2);
		}
		// Error trapping; No Name
		if (fileName.trim() == "")
			fileName = "NoName";
		fileName += ".jpg";

		File f = new File(fileName);
		if (!ImageIO.write(image, "JPEG", f)) {
			throw new RuntimeException("Unexpected error writing image");
		} else
			ImageIO.write(image, "JPEG", f);

	}
}
