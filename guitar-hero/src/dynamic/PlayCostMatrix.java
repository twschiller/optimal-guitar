package dynamic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class PlayCostMatrix {
	private HashMap<Position, HashMap<Chord, Double> > cost = new HashMap<Position, HashMap<Chord, Double> >();
	
	public void setCost(Position p, Chord c, Double playCost){
		HashMap<Chord, Double> costFromPos = cost.get(p);
		if (costFromPos == null){
			cost.put(p, new HashMap<Chord,Double>());
			costFromPos = cost.get(p);
		}
		costFromPos.put(c, playCost);
	}
	
	public static PlayCostMatrix readMatrix(File file) throws IOException{
		BufferedReader in  = new BufferedReader(new FileReader(file));
		PlayCostMatrix pcm = new PlayCostMatrix();
		
		while (in.ready()){
			String line = in.readLine();
			
			if (line.equals(""))
				continue;
			
			String args[] = line.split(",");
			pcm.setCost(Position.getPositionByName(args[0]), Chord.getChordByName(args[1]), new Double(args[2]));
		}
		in.close();
		return pcm;
	}
	
	public void writeMatrix(File out) throws IOException{
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(out)));

		for (Position p : cost.keySet()){
			HashMap<Chord,Double> tp = cost.get(p);
			for (Chord c : tp.keySet()){
				pw.println(p + "," + c + "," + tp.get(c));
			}
		}
		pw.close();
	}
	
	public Double getCost(Position p, Chord c){
		HashMap<Chord, Double> costFromPos = cost.get(p);
		if (costFromPos == null)
			return Double.POSITIVE_INFINITY;
		
		Double cost =  costFromPos.get(c);
		return cost == null ? Double.POSITIVE_INFINITY : cost ;
	}
	
	
	
}
