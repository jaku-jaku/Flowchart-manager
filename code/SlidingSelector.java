import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class SlidingSelector {
	int x, y, boxTotal, boxDisplay, boxTPLocationY;// boxTopPointerLocation
	double boxTPpercentage;
	String[] nameList;
	TextField[] tf;

	public SlidingSelector() {
		x = 100;
		y = 100;
		boxDisplay = 3;
		boxTPpercentage = 1;
	}

	public SlidingSelector(int x, int y) {
		this.x = x;
		this.y = y;
		boxDisplay = 3;
		boxTPLocationY = 0;
		boxTPpercentage = 1;
	}

	public int getHeightTotal() {
		return 25 * nameList.length;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getBoxTPLocationY() {
		return boxTPLocationY;
	}

	public void setBoxTPLocation(int t) {
		boxTPLocationY = t;
	}

	public boolean checkIfEntered(int ex, int ey) {
		if (tf.length != 0) {
		if ((x + tf[0].getWidth() + 10) <= ex && ex <= (x + tf[0].getWidth() + 8 + 10) && (y - 5) <= ey
				&& ey <= (25 * nameList.length + 10 + y)) {
			return true;
		} else
			return false;
		}else
			return false;
	}

	public void checkState(MouseEvent e) {
		if (tf.length != 0) {
			if (checkIfEntered(e.getX(), e.getY())) {
				boxTPLocationY = e.getY() - y;
				if (boxTPLocationY >= (int) ((25 * nameList.length) * (1.0 - boxTPpercentage))) {
					boxTPLocationY = (int) ((25 * nameList.length) * (1.0 - boxTPpercentage));
				}
			}
		}
	}

	public void checkScroll(MouseWheelEvent e) {
		if (tf.length != 0) {
			if (x <= e.getX() && e.getX() <= (tf[0].getWidth() + x + 20) && y <= e.getY()
					&& e.getY() <= (y + 25 * boxDisplay)) {
				int rotation = e.getUnitsToScroll();

				boxTPLocationY += rotation;

				if (boxTPLocationY >= (int) ((25 * nameList.length) * (1.0 - boxTPpercentage))) {
					boxTPLocationY = (int) ((25 * nameList.length) * (1.0 - boxTPpercentage));
				} else if (boxTPLocationY <= 0) {
					boxTPLocationY = 0;
				}

			}
		}
	}

	public void AddStringList(String[] n) {

		nameList = new String[n.length];
		for (int i = 0; i < n.length; i++)
			nameList[i] = n[i];

		tf = new TextField[n.length];
		for (int i = 0; i < nameList.length; i++) {
			String temp = nameList[i];

			tf[i] = new TextField(temp, 20);
			tf[i].setFieldLimits(20);
		}
		boxTPpercentage = 3.0 / ((double) nameList.length)-0.05;

	}

	public void checkMouseSelect(int ex, int ey) {
		for (int i = 0; i < nameList.length; i++) {
			tf[i].textStateCheck(ex, ey);
		}
	}

	public String getSelected() {
		int num = -1;
		for (int i = 0; i < nameList.length; i++) {
			if (tf[i].getState()) {
				num = i;
				break;
			}
		}
		if (num == -1)
			return "";
		else
			return tf[num].getString();
	}

	public void paintSlidingSelector(Graphics2D G) {
		int w;
		if (tf.length == 0) {
			w = 260;
			boxTPLocationY = 0;
			boxTPpercentage = 1;
		} else
			w = tf[0].getWidth();
		G.setClip(x - 5, y - 5, 8 + w + 20, 25 * boxDisplay);

		for (int i = 0; i < nameList.length; i++) {
			tf[i].setPosition(x, y - boxTPLocationY + i * 25);
			tf[i].paintTextField(G, 0);
		}
		G.drawRect(x + -5, y - 5, 8 + w, 25 * boxDisplay);

		G.setColor(new Color(141, 60, 60));
		G.fillRect(x + w + 13, y - 5, 2, 25 * boxDisplay + 10);
		G.setColor(new Color(131, 50, 40));
		G.fillRect(x + w + 11, (int) (y + boxTPLocationY* ((double) nameList.length) / ((double) nameList.length+2) - 5), 6, (int) ((25 + 10) * boxTPpercentage));
	}

}
