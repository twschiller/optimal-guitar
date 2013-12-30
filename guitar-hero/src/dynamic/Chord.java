package dynamic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Chord implements Comparable<Chord>{
	private String name;

	private static HashMap<String, Chord> byName = new HashMap<String, Chord>();
	
	private static int lastID = -1;
	private int id;
	
	public static Chord getChordByName(String name){
		return byName.get(name);
	}
	
	public static Set<Chord> readChords(File file) throws IOException{
		BufferedReader in  = new BufferedReader(new FileReader(file));
		
		Set<Chord> chords = new HashSet<Chord>();
		
		
		while (in.ready()){
			String line = in.readLine();
			if (line.equals(""))
				continue;
			chords.add(new Chord(line.trim()));
		}
		in.close();
		return chords;
	}
	
	public Chord(String name) {
		if(byName.containsKey(name))
			throw new IllegalArgumentException("Chord " + name + " already exists");	
		byName.put(name, this);
		this.name = name;
		lastID++;
		id = lastID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Position))
			return false;
		Chord p = (Chord) arg0;
		return id == p.id;
	}

	@Override
	public int compareTo(Chord arg0) {
		if (id == arg0.id)
			return 0;
		else if (id < arg0.id)
			return -1;
		else
			return 1;
	}
	
}
