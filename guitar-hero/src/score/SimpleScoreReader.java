package score;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dynamic.Chord;

public class SimpleScoreReader {

	public static List<Chord> readSimpleScore(File file) throws IOException{
		BufferedReader in  = new BufferedReader(new FileReader(file));

		String wholeFile = "";
		while (in.ready()){
			String line = in.readLine();
			wholeFile += line;
		}
		Pattern p = Pattern.compile("\\((.+?)\\)");
		Matcher m = p.matcher(wholeFile);
		
		Vector<Chord> score = new Vector<Chord>();
		
		System.out.println("=====SCORE======");
		while (m.find()){
			String curStr = m.group(1);
			System.out.println(curStr);
			score.add(Chord.getChordByName(curStr));
		}
		System.out.println("================");
		in.close();
		
		return score;
	}
	
}
