import java.io.*;
public class ScanIR {
	public void scan(String filename) throws Exception{
		String line;
		RandomAccessFile fr = new RandomAccessFile(filename, "rw");
		line = fr.readLine();
		while(line != null){
			if(line.startsWith("Move ")) 
		}
		
	}
}
