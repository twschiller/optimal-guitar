package convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import model.GuitarModel;

/**
 * Function / Program to convert a bit of well-formed guitar tab into the format used by the
 * progression program 
 * @author Todd Schiller
 */
public class TabConvert {
	
	/**
	 * Convert a file containing a bit of tab into a file of the format used by the progression program.
	 * An example of valid tab is:
	 * 
	 * E-------5-7-----7-|-8-----8-2-----2-|-0---------0-----|-----------------|
     * B-----5-----5-----|---5-------3-----|---1---1-----1---|-0-1-1-----------|
     * G---5---------5---|-----5-------2---|-----2---------2-|-0-2-2-----------|
     * D-7-------6-------|-5-------4-------|-3---------------|-----------------|
     * A-----------------|-----------------|-----------------|-2-0-0---0---8-7-|
     * E-----------------|-----------------|-----------------|-----------------|
	 *
	 * @param inFile the input file
	 * @param outFile the output file
	 * @throws IOException
	 */
	public static void readTab(File inFile, File outFile) throws IOException{
		Vector<Vector<Integer> > notes = new Vector<Vector<Integer> >();
		
		Vector<String> strings = new Vector<String>();
		
		for (int i =0 ; i < GuitarModel.NUM_STRINGS; i++)
			notes.add(new Vector<Integer>());
		
		BufferedReader in = new BufferedReader(new FileReader(inFile));
		while (in.ready()){
			strings.add(in.readLine());
		}
		in.close();
		
		int strlen = strings.firstElement().length();
		
		for (int i =1 ; i <strlen; i++){
			Integer cn[] = new Integer[GuitarModel.NUM_STRINGS];
			boolean allN = true;
			for (int s = 0 ; s < GuitarModel.NUM_STRINGS; s++){
				try{
					char tmp[] = {strings.get(s).charAt(i)};
					Integer n = new Integer(new String(tmp));
					cn[s] = n;
					allN = false;
				}catch(Exception e){
					cn[s] = null;
				}
			}
			if (!allN){
				for (int s = 0; s < GuitarModel.NUM_STRINGS  ; s++){
					notes.get(s).add(cn[s]);
				}
			}
		}
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));

		int len = notes.firstElement().size();
		
		for (int b =0 ; b < len; b++){
			out.print("(");
			for (int s = GuitarModel.NUM_STRINGS-1 ; s >= 0; s--){
				if (s != GuitarModel.NUM_STRINGS-1){
					out.print(",");
				}
				out.print( notes.get(s).get(b) == null ? "X" :  notes.get(s).get(b) );
			}
			out.println(")");
		}
		
		out.close();
	}
	
	
	public static void main(String args[]) throws IOException {
		if (args.length != 2){
			System.out.println("usage: tabconvert /path/to/tab/file /path/to/output/file");
			System.exit(1);
		}
		
		readTab(new File(args[0]), new File(args[1]));
	}
}
