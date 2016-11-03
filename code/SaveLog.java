import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SaveLog {

	public SaveLog() {

	}

	public void addLog(String name) throws IOException {
		if (checkRepetition(name))
			System.out.println("Repeat");
		else {
			RandomAccessFile raf = new RandomAccessFile("logCache.bin", "rw");
			byte[] nameBytes = new byte[20];
			name.getBytes(0, name.length(), nameBytes, 0);
			raf.seek(raf.length());
			raf.write(nameBytes);
			raf.close();
		}
	}

	public boolean checkRepetition(String name) throws IOException {
		boolean state = false;
		String[] a = this.readLog();
		for (int i = 0; i < a.length; i++) {
			if (name.trim().equals(a[i].trim())) {
				state = true;
			}
		}
		return state;
	}

	public int getIndexNumOf(String name) throws IOException {
		// return -1 if not found
		String[] a = this.readLog();
		int num = -1;
		for (int i = 0; i < a.length; i++) {
			if (name.trim().equals(a[i].trim())) {
				num = i;
				break;
			}
		}
		return num;
	}

	public String[] readLog() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("logCache.bin", "rw");
		long quantity = raf.length() / 20;
		String[] log = new String[(int) quantity];
		for (int i = 0; i < quantity; i++) {
			byte[] nameBytes2 = new byte[20];
			raf.seek(i * 20);
			raf.read(nameBytes2);
			log[i] = new String(nameBytes2, 0);
		}
		raf.close();
		return log;
	}

	public void deleteLog(int indexNum) throws IOException {
		if (indexNum != -1) {
			RandomAccessFile raf = new RandomAccessFile("logCache.bin", "rw");
			long quantity = raf.length() / 20;
			raf.seek((indexNum + 1) * 20);
			byte[] handleBytes = new byte[(int) ((quantity - indexNum) * 20)];
			raf.read(handleBytes);
			raf.seek((indexNum) * 20);
			raf.write(handleBytes);
			raf.setLength(raf.length() - 20);
			raf.close();
		}
	}
}
