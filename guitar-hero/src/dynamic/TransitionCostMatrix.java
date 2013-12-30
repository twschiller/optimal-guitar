package dynamic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class TransitionCostMatrix {
	
	private HashMap<Position, HashMap<Position, Double> > cost = new HashMap<Position, HashMap<Position, Double> >();
	
	public static TransitionCostMatrix readMatrix(File file) throws IOException{
		BufferedReader in  = new BufferedReader(new FileReader(file));
		TransitionCostMatrix tcm = new TransitionCostMatrix();
		
		while (in.ready()){
			String line = in.readLine();
			
			if (line.equals(""))
				continue;
			
			String args[] = line.split(",");
			tcm.setCost(Position.getPositionByName(args[0]), Position.getPositionByName(args[1]), new Double(args[2]));
		}
		in.close();
		return tcm;
	}
	
	public void writeMatrix(File out) throws IOException{
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(out)));

		for (Position p0 : cost.keySet()){
			HashMap<Position,Double> tp = cost.get(p0);
			for (Position p : tp.keySet()){
				pw.println(p0 + "," + p + "," + tp.get(p));
			}
		}
		
		pw.close();
	}
	
	public void setCost(Position p0, Position p, Double transitionCost){
		HashMap<Position, Double> costFromPos = cost.get(p0);
		if (costFromPos == null){
			cost.put(p0, new HashMap<Position,Double>());
			costFromPos = cost.get(p0);
		}
		costFromPos.put(p, transitionCost);
	}
	
	public Double getCost(Position p0, Position p){
		HashMap<Position, Double> costFromPos = cost.get(p0);
		if (costFromPos == null)
			return Double.POSITIVE_INFINITY;
		
		Double cost = costFromPos.get(p);
		return cost == null ? Double.POSITIVE_INFINITY : cost ;
	}
}
