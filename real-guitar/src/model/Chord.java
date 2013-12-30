package model;

import java.util.Arrays;

/**
 * Class for a chord, a collection of positions to be held at a given point.
 * A chord can consist of a single string being played (or no strings at all)
 * @author Todd Schiller
 */
public class Chord {

	private final boolean holds[];//true if note is held from before
	private final Integer notes[];
	
	@SuppressWarnings("serial")
	public static class NoLowestFretException extends Exception{
		private Chord c;
		public Chord getChord(){
			return c;
		}
		public NoLowestFretException(Chord c) {
			this.c = c;
		}		
	}
	
	@SuppressWarnings("serial")
	public static class NoHighestFretException extends Exception{
		private Chord c;
		public Chord getChord(){
			return c;
		}
		public NoHighestFretException(Chord c) {
			this.c = c;
		}		
	}
	
	
	
	public Long calcNum(){
		Long num = new Long(0);
		double base = HandForm.MAX_SPAN+2;
		Integer low = null;
		
		try {
			low = getLowestFret();
		} catch (NoLowestFretException e) {
			return new Long(0);
		}
		
		for (int i =0; i < GuitarModel.NUM_STRINGS; i++){
			if (notes[i] == null){
				num += 0L;
			}else if(notes[i] == 0){
				num += (long) (Math.pow(base, i));
			}else{
				int val = (notes[i] - low);
				if (low == 0){
					val += 1;
				}else{
					val += 2;
				}
				if (val >= base){
					throw new Error("invalid val");
				}
				num += (long) (Math.pow(base, i) * val);
			}
		}
		return num;
	}
	

	/**
	 * Get the lowest fret played on any string
	 * @return the lowest fret played on any string
	 * @throws NoLowestFretException no strings should be played
	 */
	public int getLowestFret() throws NoLowestFretException{
		Integer low = null;
		for (Integer note : notes){
			if (low == null){
				low = note;
			}else if (note != null && note < low){
				low = note;
			}
			
		}
		if (low == null)
			throw new NoLowestFretException(this);
		return low;
	}
	
	/**
	 * Get the number of notes in the chord that are on the specified fret
	 * @param fret
	 * @return the number of strings being held at the fret
	 */
	public int numNotesAtFret(int fret){
		int cnt = 0;
		
		for (Integer note : notes){
			if (note == null)
				continue;
			if (note == fret)
				cnt++;
		}
		return cnt;
	}

	/**
	 * Determine if any of the notes in the chord are held from the previous chord
	 * @return true if any of the notes are held from the previous chord
	 */
	public boolean hasHold(){
		for (Boolean hold : holds){
			if (hold)
				return true;
		}
		return false;
	}
	
	/**
	 * Finds the highest fret being played on any string
	 * @return the highest fret being played on any string in the chord
	 * @throws NoHighestFretException if no strings are being played unopen
	 */
	public int getHighestFret() throws NoHighestFretException{
		Integer high = null;
		for (Integer note : notes){
			if (note == null)
				continue;
			if ((high == null && note > 0) || (note > 0 && note > high)){
				high = note;
			}
		}
		if (high == null)
			throw new NoHighestFretException(this);
		return high;
	}
	
	/**
	 * Write the chord. Takes the form:
	 * (1,2,X,0,1,[3])
	 * 
	 * Where X is a rest
	 * [#] is a note held from the previous chord
	 */
	public String toString(){
		String s = "(";
		for (int i = 0; i < notes.length; i++){
			if (holds[i] == true){
				s += (notes[i] == null) ? "X" : "[" + notes[i] + "]";
			}else{
				s += (notes[i] == null) ? "X" : notes[i];
			}
			if (i < notes.length-1)
				s+= ",";
		}
		s += ")";
		return s;
	}
	
	public Integer[] getNotes(){
		return notes;
	}
	
	public Chord(Integer notes[], boolean holds[]) {
		super();
		this.notes = new Integer[GuitarModel.NUM_STRINGS];
		this.holds = new boolean[GuitarModel.NUM_STRINGS];
		
		System.arraycopy(notes, 0, this.notes, 0, notes.length);
		System.arraycopy(holds, 0, this.holds, 0, holds.length);
	}
	
	public Chord(Integer notes[]){
		super();
		this.notes = new Integer[GuitarModel.NUM_STRINGS];
		System.arraycopy(notes, 0, this.notes, 0, notes.length);

		this.holds = new boolean[GuitarModel.NUM_STRINGS];
		Arrays.fill(this.holds, false);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(holds);
		result = prime * result + Arrays.hashCode(notes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chord other = (Chord) obj;
		if (!Arrays.equals(holds, other.holds))
			return false;
		if (!Arrays.equals(notes, other.notes))
			return false;
		return true;
	}
	
	

}
