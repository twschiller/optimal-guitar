package score;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Chord;
import model.GuitarModel;

/**
 * Functions to read a score from a file
 * @author Todd Schiller
 *
 */
public class ScoreReader {

	public static Pattern held = Pattern.compile("\\[(.*?)\\]");
	
	public static List<Chord> readSimpleScore(File file) throws IOException{
		BufferedReader in  = new BufferedReader(new FileReader(file));

		String wholeFile = "";
		while (in.ready()){
			String line = in.readLine();
			wholeFile += line;
		}
		
		System.out.println("score file:" + wholeFile);
		
		Pattern p = Pattern.compile("\\((.*?)\\)");
		Matcher m = p.matcher(wholeFile);
		
		Vector<Chord> score = new Vector<Chord>();
		
		System.out.println("=====SCORE======");
		while (m.find()){
			//System.out.println(m.group());
			
			String curStr = m.group(1);
			String parts[] = curStr.split(",");
			
			Integer notes[] = new Integer[GuitarModel.NUM_STRINGS];
			boolean holds[] = new boolean[GuitarModel.NUM_STRINGS];
			
			int n = 0;
			for (String part : parts){
				//System.out.println("examine part:" + part);
				Matcher m2 = held.matcher(part);
				boolean found = m2.find();
				
				if (!found){
					if (part.trim().equals("X")){
						notes[n] = null;
						holds[n] = false;
					}else{
						notes[n] = new Integer(part);
						holds[n] = false;
					}
				}else{
					notes[n] = new Integer(m2.group(1));
					holds[n] = true;
				}
				n++;
			}
			Chord nc = new Chord(notes,holds); 
			
			System.out.println(nc);
			
			score.add(nc);
		}
		System.out.println("================");
		in.close();
		
		return score;
	}
}
