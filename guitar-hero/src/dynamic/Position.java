package dynamic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class Position implements Comparable<Position>{
	private String name;
	private static int lastID = -1;
	private static HashMap<Integer, Position> map = new HashMap<Integer, Position>();
	private static HashMap<String, Position> byName = new HashMap<String, Position>();
	private int id;

	public Position(String name){
		if (byName.containsKey(name))
			throw new IllegalArgumentException("Position " + name + " already exists");
		lastID++;
		id = lastID;
		map.put(id, this);
		byName.put(name, this);
		this.name = name;
	}

	public static Position getPositionByName(String name){
		return byName.get(name);
	}
	
	public static SortedSet<Position> readPositions(File file) throws IOException{
		BufferedReader in  = new BufferedReader(new FileReader(file));
		
		SortedSet<Position> positions = new TreeSet<Position>();
		
		while (in.ready()){
			String line = in.readLine();
			if (line.equals(""))
				continue;
			positions.add(new Position(line.trim()));
		}
		in.close();
		return positions;
	}
	
	public static Position getPositionByID(int id){
		return map.get(id);
	}
	
	public static int getLastPositionID(){
		if (lastID < 0)
			throw new Error("No positions have been created");
		return lastID;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return (int) id;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Position))
			return false;
		Position p = (Position) arg0;
		return id == p.id;
	}

	@Override
	public int compareTo(Position arg0) {
		if (id == arg0.id)
			return 0;
		else if (id < arg0.id)
			return -1;
		else
			return 1;
	}
	
	
}
